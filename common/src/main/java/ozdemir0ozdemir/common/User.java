package ozdemir0ozdemir.common;

import java.util.List;

public record User(Long id, String username, List<String> authorities) {
}
