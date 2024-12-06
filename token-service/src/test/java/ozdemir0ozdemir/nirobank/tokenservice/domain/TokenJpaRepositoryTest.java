package ozdemir0ozdemir.nirobank.tokenservice.domain;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TokenJpaRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres
            = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TokenJpaRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void should_SaveNewTokenEntity() throws Exception {

        TokenEntity notSaved = TokenEntity.of("username", "refreshToken", nowPlusMinutes(10L));

        TokenEntity saved = this.repository.save(notSaved);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("username");
        assertThat(saved.getToken()).isEqualTo("refreshToken");
        assertThat(saved.getTokenId()).isNotNull().isNotBlank();
        assertThat(saved.getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

        TokenEntity check = this.repository.findByTokenId(saved.getTokenId()).get();

        assertThat(check).isEqualTo(saved);
    }

    @Test
    void should_findAllExpiredTokensByStatus() {
        // Given
        for (int i = 0; i < 10; i++) {
            TokenEntity notSaved = TokenEntity.of("username" + i, "refreshToken" + i, nowMinusMinutes(10L));
            this.repository.save(notSaved);
        }
        // When
        Page<TokenEntity> tokens = this.repository
                .findAllExpiredTokensByTokenStatus(TokenStatus.ACCEPTABLE, PageRequest.of(0, 10));

        // Then
        assertThat(tokens.getTotalElements()).isEqualTo(10);

        LocalDateTime now = LocalDateTime.now();
        tokens.forEach(entity -> assertThat(entity.getExpiresAt().isBefore(now)).isTrue() );
    }

    @Test
    void should_RevokeTokenByTokenId() {
        //When
        TokenEntity notSaved = TokenEntity.of("username", "refreshToken", nowPlusMinutes(10L));
        TokenEntity saved = this.repository.save(notSaved);
        assertThat(saved.getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

        this.repository.revokeTokenByTokenId(saved.getTokenId());
        TokenEntity revoked = this.repository.findByTokenId(saved.getTokenId()).get();
        assertThat(revoked.getTokenStatus()).isEqualTo(TokenStatus.REVOKED);
    }

    @Test
    void should_DeleteTokenByTokenId() {
        //When
        TokenEntity notSaved = TokenEntity.of("username", "refreshToken", nowPlusMinutes(10L));
        TokenEntity saved = this.repository.save(notSaved);
        assertThat(saved.getTokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);

        this.repository.deleteTokenByTokenId(saved.getTokenId());
        Optional<TokenEntity> mustNull = this.repository.findByTokenId(saved.getTokenId());
        assertThat(mustNull.isPresent()).isFalse();
    }



    private static LocalDateTime nowPlusMinutes(Long minutes) {
        return LocalDateTime.now().plus(minutes, ChronoUnit.MINUTES);
    }

    private static LocalDateTime nowMinusMinutes(Long minutes) {
        return LocalDateTime.now().minus(minutes, ChronoUnit.MINUTES);
    }
}