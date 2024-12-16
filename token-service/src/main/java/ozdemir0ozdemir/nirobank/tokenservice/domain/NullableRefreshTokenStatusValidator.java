package ozdemir0ozdemir.nirobank.tokenservice.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableRefreshTokenStatusValidator implements ConstraintValidator<NullableValidRefreshTokenStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        try {
            RefreshTokenStatus.valueOf(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
