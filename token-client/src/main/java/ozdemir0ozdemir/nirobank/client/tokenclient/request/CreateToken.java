package ozdemir0ozdemir.nirobank.client.tokenclient.request;

import ozdemir0ozdemir.common.user.Role;

public record CreateToken(String username, Role role) {
}
