package ozdemir0ozdemir.nirobank.authserver.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.authserver.model.Token;
import ozdemir0ozdemir.nirobank.authserver.repository.TokenRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public Token generateOrGetAccessToken(String username, List<String> authorities) {

        // TODO: Query username in user-service
        Optional<Token> optionalToken = this.tokenRepository
                .findTokenByUsername(username);

        if(optionalToken.isPresent()){
            Token token = optionalToken.get();
            String existingAccessToken = token.accessToken();
            String existingRefreshToken = token.refreshToken();

            try {
                this.jwtService.verifyTokenFor(existingAccessToken);
                return new Token(existingAccessToken, existingRefreshToken);
            }
            catch (ExpiredJwtException ex) {
                this.tokenRepository.revokeToken(username, token);
            }
        }

        String accessToken = this.jwtService.generateBearerTokenFor(username, authorities);
        String refreshToken = this.jwtService.generateRefreshTokenFor(username);

        this.tokenRepository.saveToken(username, accessToken, refreshToken);
        return new Token(accessToken, refreshToken);
    }
}
