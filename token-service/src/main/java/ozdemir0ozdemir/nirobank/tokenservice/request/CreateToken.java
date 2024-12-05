package ozdemir0ozdemir.nirobank.tokenservice.request;

import ozdemir0ozdemir.nirobank.client.userclient.Role;

public record CreateToken(String username, Role role) {
}
