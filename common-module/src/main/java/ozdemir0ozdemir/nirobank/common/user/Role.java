package ozdemir0ozdemir.nirobank.common.user;

import java.util.List;

public enum Role {
    USER("USER:read"),
    MANAGER("USER:read", "USER:write", "ADMIN:read"),
    ADMIN("USER:read", "USER:write", "ADMIN:read", "ADMIN:write");

    private final List<String> permissions;

    Role(String... permissions) {
        this.permissions = List.of(permissions);
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
