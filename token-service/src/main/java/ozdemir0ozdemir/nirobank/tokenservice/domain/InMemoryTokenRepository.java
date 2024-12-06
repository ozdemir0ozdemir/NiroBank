package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
class InMemoryTokenRepository {

    private final Set<TokenEntity> tokens;

    public InMemoryTokenRepository() {

        this.tokens = new HashSet<>();
    }

    public InMemoryTokenRepository(Set<TokenEntity> tokens) {

        this.tokens = tokens;
    }

    // Create Operations
    String saveToken(TokenEntity entity) {

        this.tokens.add(entity);
        return entity.getTokenId();
    }

    // Read Operations
    Optional<TokenEntity> findTokenByTokenId(String tokenId) {
        return this.tokens.stream()
                .filter(entity -> entity.getTokenId().equals(tokenId))
                .findFirst();
    }

    Set<TokenEntity> findTokensByUsername(String username) {
        return this.tokens.stream()
                .filter(entity -> entity.getUsername().equals(username))
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
                .filter(entity -> entity.getTokenStatus().equals(status))
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
                .filter(entity -> entity.getUsername().equals(username))
                .filter(entity -> entity.getTokenStatus().equals(status))
                .collect(Collectors.toSet());
    }

    Set<TokenEntity> findExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        return this.tokens.stream()
                .filter(entity -> entity.getExpiresAt().isAfter(now))
                .collect(Collectors.toSet());
    }

    // Update Operations
    void revokeTokenByTokenId(String tokenId) {
        this.tokens.stream()
                .filter(entity -> entity.getTokenId().equals(tokenId))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    void revokeTokenByUsername(String username) {
        this.tokens.stream()
                .filter(entity -> entity.getUsername().equals(username))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    void revokeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        this.tokens.stream()
                .filter(entity -> entity.getExpiresAt().isBefore(now))
                .forEach(entity -> entity.setTokenStatus(TokenStatus.REVOKED));
    }

    // Delete Operations
    void deleteRevokedTokens() {

        this.tokens
                .stream()
                .filter(entity -> entity.getTokenStatus().equals(TokenStatus.REVOKED))
                .collect(Collectors.toSet())
                .forEach(this.tokens::remove);
    }
}
