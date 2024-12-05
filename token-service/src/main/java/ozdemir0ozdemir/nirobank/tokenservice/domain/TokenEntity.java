package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.Instant;

public record TokenEntity(String tokenId,
                          String token,
                          Instant expiredAt,
                          TokenStatus tokenStatus) {


}
