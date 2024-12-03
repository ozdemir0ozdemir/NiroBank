package ozdemir0ozdemir.nirobank.tokenservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.client.userclient.User;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.tokenservice.bridge.Token;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenGenerationException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserClient userClient;

    public Token generateTokenSet(String username) {

        // FIXME: turn to login request
        User user = userClient.findUserByUsername(username).getObject();

        Optional<Token> optionalOldToken = this.tokenRepository
                .findTokenByUsername(username);

        if(optionalOldToken.isPresent()){
            throw new TokenGenerationException("You have an access token");
        }

        return this.createNewTokenSetFor(username, user.role().getPermissions());
    }

    public Token refreshOrGetTokenSet(String username, String refreshToken) {

        User user = userClient.findUserByUsername(username).getObject();

        Token oldToken = this.tokenRepository
                .findTokenByUsernameAndRefreshToken(username, refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found"));

        boolean isAccessTokenExpired = this.jwtService.isTokenExpired(oldToken.accessToken());
        boolean isRefreshTokenExpired = this.jwtService.isTokenExpired(oldToken.refreshToken());

        if (!isAccessTokenExpired) {
            return oldToken;
        }
        else if (!isRefreshTokenExpired) {
            this.tokenRepository.revokeToken(username, oldToken);
            return this.createNewTokenSetFor(username, user.role().getPermissions());
        }

        this.tokenRepository.revokeToken(username, oldToken);
        throw new TokenNotFoundException("Refresh token is expired. You should generate a new token set");
    }

    private Token createNewTokenSetFor(String username, List<String> authorities) {
        String newAccessToken = this.jwtService.generateBearerTokenFor(username, authorities);
        String newRefreshToken = this.jwtService.generateRefreshTokenFor(username);

        this.tokenRepository.saveToken(username, newAccessToken, newRefreshToken);
        return new Token(newAccessToken, newRefreshToken);
    }
}
