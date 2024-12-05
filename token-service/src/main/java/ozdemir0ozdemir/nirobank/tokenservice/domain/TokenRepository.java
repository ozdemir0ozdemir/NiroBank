package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
class TokenRepository {

    private final Set<TokenEntity> tokens;

    public TokenRepository() {

        this.tokens = new HashSet<>();
    }

    public TokenRepository(Set<TokenEntity> tokens) {

        this.tokens = tokens;
    }

    // Create Operations
    String saveToken(TokenEntity entity) {

        this.tokens.add(entity);
        return entity.tokenId();
    }

    // Read Operations
    Optional<TokenEntity> findTokenByTokenId(String tokenId) {
        return this.tokens.stream()
                .filter(entity -> entity.tokenId().equals(tokenId))
                .findFirst();
    }

    Set<TokenEntity> findTokensByUsername(String username) {
        return this.tokens.stream()
                .filter(entity -> entity.username().equals(username))
                .collect(Collectors.toSet());
    }

    Set<TokenEntity> findAcceptableTokens() {

        return this.findTokensByTokenStatus(TokenStatus.ACCEPTABLE);
    }

    Set<TokenEntity> findRevokedTokens() {

        return this.findTokensByTokenStatus(TokenStatus.REVOKED);
    }

    Set<TokenEntity> findTokensByTokenStatus(TokenStatus status) {
        return this.tokens.stream()
                .filter(entity -> entity.tokenStatus().equals(status))
                .collect(Collectors.toSet());
    }

    Set<TokenEntity> findAcceptableTokensByUsername(String username) {
        return this.findTokensByUsernameAndTokenStatus(username, TokenStatus.ACCEPTABLE);
    }

    Set<TokenEntity> findRevokedTokensByUsername(String username) {
        return this.findTokensByUsernameAndTokenStatus(username, TokenStatus.REVOKED);
    }

    Set<TokenEntity> findTokensByUsernameAndTokenStatus(String username, TokenStatus status) {
        return this.tokens.stream()
                .filter(entity -> entity.username().equals(username))
                .filter(entity -> entity.tokenStatus().equals(status))
                .collect(Collectors.toSet());
    }

    Set<TokenEntity> findExpiredTokens() {
        Instant now = Instant.now();
        return this.tokens.stream()
                .filter(entity -> entity.expiresAt().isAfter(now))
                .collect(Collectors.toSet());
    }

    // Update Operations
    void revokeTokenByTokenId(String tokenId) {
        this.tokens.stream()
                .filter(entity -> entity.tokenId().equals(tokenId))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    void revokeTokenByUsername(String username) {
        this.tokens.stream()
                .filter(entity -> entity.username().equals(username))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    void revokeExpiredTokens() {
        Instant now = Instant.now();
        this.tokens.stream()
                .filter(entity -> entity.expiresAt().isAfter(now))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    // Delete Operations
    void deleteRevokedTokens() {

        this.tokens
                .stream()
                .filter(entity -> entity.tokenStatus().equals(TokenStatus.REVOKED))
                .collect(Collectors.toSet())
                .forEach(this.tokens::remove);
    }
}
