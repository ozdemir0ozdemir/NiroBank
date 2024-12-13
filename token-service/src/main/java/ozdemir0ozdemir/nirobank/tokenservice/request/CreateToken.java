package ozdemir0ozdemir.nirobank.tokenservice.request;


import ozdemir0ozdemir.common.user.Role;

public record CreateToken(String username, Role role) {
}
