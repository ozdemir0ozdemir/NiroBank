package ozdemir0ozdemir.nirobank.tokenservice.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullableRefreshTokenStatusValidator.class)
public @interface NullableValidRefreshTokenStatus {

    String regexp() default "ACCEPTABLE|REVOKED";

    String message() default "Role must be one of them {regexp}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
