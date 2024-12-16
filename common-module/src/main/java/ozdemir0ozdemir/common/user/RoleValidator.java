package ozdemir0ozdemir.common.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Role.valueOf(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }

    }
}
