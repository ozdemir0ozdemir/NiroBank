package ozdemir0ozdemir.nirobank.client.tokenclient.request;


import ozdemir0ozdemir.nirobank.common.user.Role;

public record GenerateToken(String username, Role role) {
}
