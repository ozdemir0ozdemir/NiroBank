package ozdemir0ozdemir.nirobank.tokenservice.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@Accessors(chain = true)
class TokenEntity {
    private String tokenId;
    private String username;
    private String token;
    private Instant expiresAt;
    private TokenStatus tokenStatus;

    static TokenEntity of(String username, String token, Instant expiredAt) {
        return new TokenEntity(
                username + ":" + UUID.randomUUID().toString(),
                username,
                token,
                expiredAt,
                TokenStatus.ACCEPTABLE);
    }

}
