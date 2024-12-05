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
    private Instant expiredAt;

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

    public Instant expiredAt() {
        return expiredAt;
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

}
