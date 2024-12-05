package ozdemir0ozdemir.nirobank.client.userclient.request;


import ozdemir0ozdemir.nirobank.client.userclient.Role;

public record ChangeUserRole(String username, Role role) {
}
