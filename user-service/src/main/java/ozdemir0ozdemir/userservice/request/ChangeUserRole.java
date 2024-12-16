package ozdemir0ozdemir.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ozdemir0ozdemir.userservice.domain.Role;

public record ChangeUserRole(
        @NotBlank(message = "Username cannot be blank or null")
        @Size(min = 4, max = 30, message = "Username length must be in range 4-30")
        String username,

        @NotBlank(message = "Role cannot be blank or null")
        @Pattern(regexp = "USER|MANAGER|ADMIN")
        Role role) {
}
