package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class InMemoryAccessTokenRepositoryUnitTest {

    private static final Set<TokenEntity> entities = new HashSet<>();

    private static final InMemoryTokenRepository sut = new InMemoryTokenRepository(entities);

    private static final String USER = "user";
    private static final String TOKEN = "token";

    @BeforeEach
    void beforeEach() {
        Instant expiresAt = nowPlus30m();
        Instant expired = nowMinus30m();

        // Valid Tokens
        for (int i = 0; i < 10; i++) {
            entities.add(TokenEntity.of(USER + i, TOKEN + i, expiresAt));
        }

        // Expired Tokens
        for (int i = 10; i < 17; i++) {
            entities.add(TokenEntity.of(USER + i, TOKEN + i, expired));
        }
    }

    @AfterEach
    void afterEach() {
        entities.clear();
    }

    @Test
    void should_FindTokenByUsername() throws Exception {

        TokenEntity entity = sut.findTokensByUsername(USER + "0")
                .stream()
                .findFirst()
                .get();

        assertThat(entity).isNotNull();
        assertThat(entity.getUsername()).isEqualTo(USER + "0");
        assertThat(entity.getToken()).isEqualTo(TOKEN + "0");
        assertThat(entity.getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);
        assertThat(entity.getExpiresAt()).isAfter(Instant.now().plus(29L, ChronoUnit.MINUTES));
    }

    @Test
    void should_SaveTokenEntity() throws Exception {

        TokenEntity entity = TokenEntity.of("admin", "admin:token", nowPlus30m());

        String tokenId = sut.saveToken(entity);

        TokenEntity actualEntity = sut.findTokenByTokenId(tokenId).get();

        assertThat(actualEntity).isNotNull();
        assertThat(actualEntity.getUsername()).isEqualTo("admin");
        assertThat(actualEntity.getToken()).isEqualTo("admin:token");
        assertThat(actualEntity.getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

    }

    @Test
    void should_RevokeTokenByTokenId() {

        String tokenId = sut.saveToken(TokenEntity.of("admin", "admintoken", nowPlus30m()));
        assertThat(sut.findTokenByTokenId(tokenId).get().getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

        sut.revokeTokenByTokenId(tokenId);
        assertThat(sut.findTokenByTokenId(tokenId).get().getTokenStatus()).isEqualTo(TokenStatus.REVOKED);
    }

    @Test
    void should_RevokeTokenByUsername() {

        String tokenId = sut.saveToken(TokenEntity.of("admin", "admintoken", nowPlus30m()));
        assertThat(sut.findTokenByTokenId(tokenId).get().getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

        sut.revokeTokenByUsername("admin");
        assertThat(sut.findTokenByTokenId(tokenId).get().getTokenStatus()).isEqualTo(TokenStatus.REVOKED);
    }

    @Test
    void should_RevokeExpiredTokens() {

        int beforeRevokedCount = entities
                .stream()
                .filter(entity -> entity.getTokenStatus().equals(TokenStatus.ACCEPTABLE))
                .collect(Collectors.toSet()).size();

        sut.revokeExpiredTokens();

        int afterRevokedCount = entities
                .stream()
                .filter(entity -> entity.getTokenStatus().equals(TokenStatus.ACCEPTABLE))
                .collect(Collectors.toSet()).size();

        assertThat(beforeRevokedCount).isNotEqualTo(afterRevokedCount);
        assertThat(afterRevokedCount).isEqualTo(10);
    }

    @Test
    void should_DeleteRevokedTokens() {

       int beforeDeleteRevokedTokenCount = entities.size();

        sut.revokeExpiredTokens();
        sut.deleteRevokedTokens();

        assertThat(entities.size()).isNotEqualTo(beforeDeleteRevokedTokenCount);
        assertThat(entities.size()).isEqualTo(10);
    }

    private static Instant nowPlus30m() {
        return Instant.now().plus(30L, ChronoUnit.MINUTES);
    }

    private static Instant nowMinus30m() {
        return Instant.now().minus(30L, ChronoUnit.MINUTES);
    }

}