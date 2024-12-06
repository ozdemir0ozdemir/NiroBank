package ozdemir0ozdemir.nirobank.tokenservice.domain;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenJpaRepository repository;
    private final JwtService jwtService;

    public AccessToken createTokens(String username, List<String> authorities)  {
        // If user has a refresh token already then revoke it
        Optional<TokenEntity> oldToken = this.repository.findByUsername(username);
        if(oldToken.isPresent()){
            String oldTokenId = this.jwtService.getClaimsFrom(oldToken.get().getToken())
                    .getId();
            this.repository.revokeTokenByTokenId(oldTokenId);
        }

        // Generate Access and Refresh Token
        String accessToken = this.jwtService.generateJwtFor(username, authorities);
        String token = this.jwtService.generateRefreshJwtFor(username, authorities);

        // Store refresh token in postgres
        Claims tokenClaims = this.jwtService.getClaimsFrom(token);

        TokenEntity te = new TokenEntity()
                .setTokenId(tokenClaims.getId())
                .setToken(token)
                .setUsername(username)
                .setTokenStatus(TokenStatus.ACCEPTABLE)
                .setExpiresAt(tokenClaims.getExpiration().toInstant());

        this.repository.save(te);

        // Return access token and refresh token id to the client
        return buildAccessTokenResult(accessToken, tokenClaims.getId());
    }

    public AccessToken refreshToken(String tokenId) {
        Optional<TokenEntity> optionalToken = this.repository.findByTokenId(tokenId);
        if(optionalToken.isEmpty()){
           throw new RuntimeException("Refresh token not found"); // FIXME: Throw proper exception
        }

        // If refresh token exists then generate access token from it
        Claims tokenClaims = this.jwtService.getClaimsFrom(optionalToken.get().getToken());

        String accessToken = this.jwtService.generateJwtFor(
                tokenClaims.getSubject(),
                tokenClaims.get(JwtService.USER_AUTHORITIES, String[].class));

        return buildAccessTokenResult(accessToken, tokenClaims.getId());
    }

    // TODO: throw an exception if any token not found
    public Optional<Token> findByTokenId(String tokenId) {
        return this.repository.findByTokenId(tokenId)
                .map(this::entityToToken);
    }

    // TODO: throw an exception if any token not found
    public Optional<Token> findByUsername(String username) {
        return this.repository.findByUsername(username)
                .map(this::entityToToken);
    }

    public Page<Token> findAll(int pageNumber, int pageSize) {
        return this.repository.findAll(PageRequest.of(pageNumber, pageSize))
                .map(this::entityToToken);
    }

    public Page<Token> findAllExpiredTokensByTokenStatus(TokenStatus status, int pageNumber, int pageSize) {
        return this.repository.findAllExpiredTokensByTokenStatus(status, PageRequest.of(pageNumber, pageSize))
                .map(this::entityToToken);
    }

    public Page<Token> findAllByUsername(String username, int pageNumber, int pageSize) {
        return this.repository.findAllByUsername(username, PageRequest.of(pageNumber, pageSize))
                .map(this::entityToToken);
    }

    public Page<Token> findAllByTokenStatus(TokenStatus status, int pageNumber, int pageSize) {
        return this.repository.findAllByTokenStatus(status, PageRequest.of(pageNumber, pageSize))
                .map(this::entityToToken);
    }

    public Page<Token> findAllByUsernameAndTokenStatus(String username, TokenStatus status, int pageNumber, int pageSize) {
        return this.repository.findAllByUsernameAndTokenStatus(username, status, PageRequest.of(pageNumber, pageSize))
                .map(this::entityToToken);
    }

    public void revokeTokenByTokenId(String tokenId) {
        this.repository.revokeTokenByTokenId(tokenId);
    }

    public void deleteTokenByTokenId(String tokenId) {
        this.repository.deleteTokenByTokenId(tokenId);
    }


    Token entityToToken(TokenEntity entity) {
        return new Token(
                entity.getTokenId(),
                entity.getToken(),
                entity.getUsername(),
                entity.getExpiresAt(),
                entity.getTokenStatus());
    }

    AccessToken buildAccessTokenResult(String accessToken, String refreshTokenId) {
        Claims claims = this.jwtService.getClaimsFrom(accessToken);

        return new AccessToken(
                refreshTokenId,
                accessToken,
                claims.getSubject(),
                claims.getExpiration().toInstant(),
                TokenStatus.ACCEPTABLE
        );
    }
}
