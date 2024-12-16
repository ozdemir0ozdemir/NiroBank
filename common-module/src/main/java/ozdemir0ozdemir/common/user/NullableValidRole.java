
package ozdemir0ozdemir.common.user;

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
@Constraint(validatedBy = NullableRoleValidator.class)
public @interface NullableValidRole {
    String regexp() default "USER|MANAGER|ADMIN";
    String message() default "Role must be one of them {regexp}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
