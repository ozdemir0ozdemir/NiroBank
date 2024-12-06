package ozdemir0ozdemir.nirobank.tokenservice.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
class TokenEntity {

    @Id
    @SequenceGenerator(name = "tokens_id_gen", sequenceName = "tokens_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "tokens_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "token_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus = TokenStatus.ACCEPTABLE;

    static TokenEntity of(String username, String token, Instant expiresAt) {
        return new TokenEntity()
                .setTokenId(username + ":" + UUID.randomUUID())
                .setUsername(username)
                .setToken(token)
                .setExpiresAt(expiresAt)
                .setTokenStatus(TokenStatus.ACCEPTABLE);
    }

}
