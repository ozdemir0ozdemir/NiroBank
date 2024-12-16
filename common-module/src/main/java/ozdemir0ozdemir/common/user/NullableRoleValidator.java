package ozdemir0ozdemir.common.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableRoleValidator implements ConstraintValidator<NullableValidRole, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        try {
            Role.valueOf(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }

    }
}
