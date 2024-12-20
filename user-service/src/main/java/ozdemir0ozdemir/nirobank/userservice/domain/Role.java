package ozdemir0ozdemir.nirobank.userservice.domain;

import lombok.Getter;

import java.util.List;

@Getter
public enum Role {
    USER("USER:read"),
    MANAGER("USER:read", "USER:write", "ADMIN:read"),
    ADMIN("USER:read", "USER:write", "ADMIN:read", "ADMIN:write");

    private final List<String> permissions;

    Role(String... permissions) {
        this.permissions = List.of(permissions);
    }
}
