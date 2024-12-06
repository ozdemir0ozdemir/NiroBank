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
    private long id;
    private String tokenId;
    private String username;
    private String token;
    private Instant expiresAt;
    private TokenStatus tokenStatus;

    static TokenEntity of(String username, String token, Instant expiresAt) {
        return new TokenEntity()
                .setTokenId(username + ":" + UUID.randomUUID())
                .setUsername(username)
                .setToken(token)
                .setExpiresAt(expiresAt)
                .setTokenStatus(TokenStatus.ACCEPTABLE);
    }

}
