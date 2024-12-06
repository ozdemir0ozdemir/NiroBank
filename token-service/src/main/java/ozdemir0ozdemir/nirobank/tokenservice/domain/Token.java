package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.LocalDateTime;

public record Token(String tokenId,
                    String Token,
                    String username,
                    LocalDateTime expiresAt,
                    TokenStatus tokenStatus) {}
