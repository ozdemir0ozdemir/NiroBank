package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.util.Date;

public record Token(String tokenId,
                    String Token,
                    String username,
                    Date expiresAt,
                    RefreshTokenStatus tokenStatus) {}
