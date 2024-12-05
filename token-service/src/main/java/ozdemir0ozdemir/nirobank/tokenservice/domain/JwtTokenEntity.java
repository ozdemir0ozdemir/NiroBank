package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.Instant;

public record JwtTokenEntity(String tokenId,
                             String token,
                             Instant expiredAt,
                             TokenStatus tokenStatus) {


}
