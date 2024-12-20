package ozdemir0ozdemir.nirobank.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ozdemir0ozdemir.nirobank.common.user.ValidRole;
import ozdemir0ozdemir.nirobank.userservice.domain.Role;

public record ChangeUserRole(
        @NotBlank(message = "Username cannot be blank or null")
        @Size(min = 4, max = 30, message = "Username length must be in range 4-30")
        String username,

        @ValidRole(regexp = "USER|MANAGER|ADMIN", message = "Role must be one of them {USER | MANAGER | ADMIN}")
        Role role) {
}
