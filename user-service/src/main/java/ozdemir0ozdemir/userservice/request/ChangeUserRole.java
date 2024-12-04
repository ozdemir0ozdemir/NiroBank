package ozdemir0ozdemir.userservice.request;

import ozdemir0ozdemir.userservice.domain.Role;

public record ChangeUserRole(String username, Role role) {
}
