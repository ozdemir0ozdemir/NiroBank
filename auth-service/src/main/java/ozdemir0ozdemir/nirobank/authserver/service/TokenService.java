package ozdemir0ozdemir.nirobank.authserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.authserver.exception.TokenGenerationException;
import ozdemir0ozdemir.nirobank.authserver.exception.TokenNotFoundException;
import ozdemir0ozdemir.nirobank.authserver.model.Token;
import ozdemir0ozdemir.nirobank.authserver.repository.TokenRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public Token generateTokenSet(String username) {

        // TODO: Query username in user-service and get authorities from user-service
        Optional<Token> optionalOldToken = this.tokenRepository
                .findTokenByUsername(username);

        if(optionalOldToken.isPresent()){
            throw new TokenGenerationException("You have an access token");
        }

        return this.createNewTokenSetFor(username, List.of());  // FIXME: Authorities is empty
    }

    public Token refreshOrGetTokenSet(String username, String refreshToken) {

        // TODO: Query username in user-service and get authorities from user-service
        Token oldToken = this.tokenRepository
                .findTokenByUsernameAndRefreshToken(username, refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found"));

        boolean isAccessTokenExpired = this.jwtService.isTokenExpired(oldToken.accessToken());
        boolean isRefreshTokenExpired = this.jwtService.isTokenExpired(oldToken.refreshToken());

        if (!isAccessTokenExpired) {
            return oldToken;
        }
        else if (isRefreshTokenExpired) {
            this.tokenRepository.revokeToken(username, oldToken);
            throw new TokenNotFoundException("Refresh token is expired. You can generate a new token set");
        }

        // FIXME: Authorities is empty
        return this.createNewTokenSetFor(username, List.of());
    }

    private Token createNewTokenSetFor(String username, List<String> authorities) {
        String newAccessToken = this.jwtService.generateBearerTokenFor(username, authorities);
        String newRefreshToken = this.jwtService.generateRefreshTokenFor(username);

        this.tokenRepository.saveToken(username, newAccessToken, newRefreshToken);
        return new Token(newAccessToken, newRefreshToken);
    }
}
