package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.Instant;

public record Token(String tokenId,
                    String Token,
                    String username,
                    Instant expiresAt,
                    TokenStatus tokenStatus) {}
