package ozdemir0ozdemir.userservice.bridge;

import ozdemir0ozdemir.userservice.domain.Role;

public record User(Long id, String username, Role role) {
}
