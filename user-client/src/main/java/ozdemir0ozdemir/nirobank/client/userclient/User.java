package ozdemir0ozdemir.nirobank.client.userclient;

import ozdemir0ozdemir.common.user.Role;

public record User(Long id, String username, Role role) {
}
