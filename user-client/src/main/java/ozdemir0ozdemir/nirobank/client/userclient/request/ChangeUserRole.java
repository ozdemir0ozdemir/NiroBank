package ozdemir0ozdemir.nirobank.client.userclient.request;


import ozdemir0ozdemir.common.user.Role;

public record ChangeUserRole(String username, Role role) {
}
