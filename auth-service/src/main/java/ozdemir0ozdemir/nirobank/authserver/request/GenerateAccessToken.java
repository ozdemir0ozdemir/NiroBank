package ozdemir0ozdemir.nirobank.authserver.request;

import java.util.List;

public record GenerateAccessToken(String username, List<String> authorities) {
}
