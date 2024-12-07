package ozdemir0ozdemir.nirobank.client.tokenclient.request;

import ozdemir0ozdemir.nirobank.client.userclient.Role;

public record CreateToken(String username, Role role) {
}
