package ozdemir0ozdemir.nirobank.client.tokenclient;

import java.util.Date;

public record AccessToken(String refreshTokenId,
                          String accessToken,
                          String username,
                          Date expiresAt,
                          TokenStatus tokenStatus) {}
