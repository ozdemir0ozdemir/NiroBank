package ozdemir0ozdemir.nirobank.tokenservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
class RefreshTokenEntity {

    @Id
    @SequenceGenerator(name = "refresh_tokens_id_gen", sequenceName = "refresh_tokens_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "refresh_tokens_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "refresh_token_id", updatable = false)
    private Long id;

    @Column(name = "ref_id", nullable = false, updatable = false)
    private String referenceId;

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Column(name = "refresh_token", nullable = false, updatable = false)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp expiresAt;

    @Column(name = "refresh_token_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus refreshTokenStatus = RefreshTokenStatus.ACCEPTABLE;

    static RefreshTokenEntity of(String username, String token, Timestamp expiresAt) {
        return new RefreshTokenEntity()
                .setReferenceId(username + ":" + UUID.randomUUID())
                .setUsername(username)
                .setRefreshToken(token)
                .setExpiresAt(expiresAt)
                .setRefreshTokenStatus(RefreshTokenStatus.ACCEPTABLE);
    }

}
