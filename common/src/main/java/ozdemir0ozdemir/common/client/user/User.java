package ozdemir0ozdemir.common.client.user;

import java.util.List;

public record User(Long id, String username, List<String> authorities) {
}
