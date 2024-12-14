package ozdemir0ozdemir.nirobank.tokenservice.request;


import ozdemir0ozdemir.common.user.Role;

public record GenerateToken(String username, Role role) {
}
