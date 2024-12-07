package ozdemir0ozdemir.nirobank.client.tokenclient;

import java.util.Date;

public record Token(String tokenId,
                    String Token,
                    String username,
                    Date expiresAt,
                    TokenStatus tokenStatus) {}
