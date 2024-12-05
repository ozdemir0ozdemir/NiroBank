package ozdemir0ozdemir.nirobank.tokenservice.domain;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
class TokenEntity {
    private String tokenId;
    private String username;
    private String token;
    private Instant expiresAt;

    @Setter
    private TokenStatus tokenStatus;

    static TokenEntity of(String username, String token, Instant expiredAt) {
        return new TokenEntity(
                username + ":" + UUID.randomUUID().toString(),
                username,
                token,
                expiredAt,
                TokenStatus.ACCEPTABLE);
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public String token() {
        return token;
    }

    public String tokenId() {
        return tokenId;
    }

    public TokenStatus tokenStatus() {
        return tokenStatus;
    }

    public String username() {
        return username;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
                "expiresAt=" + expiresAt +
                ", tokenId='" + tokenId + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", tokenStatus=" + tokenStatus +
                '}';
    }
}
