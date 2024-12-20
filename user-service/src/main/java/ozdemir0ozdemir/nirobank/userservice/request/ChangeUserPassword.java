package ozdemir0ozdemir.nirobank.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// FIXME: provide current password and new password
public record ChangeUserPassword(
        @NotBlank(message = "Username cannot be blank or null")
        @Size(min = 4, max = 30, message = "Username length must be in range 4-30")
        String username,

        @NotBlank(message = "Password cannot be blank or null")
        @Size(min = 4, max = 30, message = "Password length must be in range 4-30")
        String password) {
}
