package ozdemir0ozdemir.nirobank.jwt;

import java.util.List;
import java.util.Set;

public record Token(String jwtId,
                    String issuer,
                    Set<String> audiences,
                    Long issuedAt,
                    Long expiresAt,
                    String username,
                    List<String> authorities) {
}
