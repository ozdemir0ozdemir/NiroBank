package ozdemir0ozdemir.nirobank.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JwtCondition extends SpringBootCondition {


    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

        ConditionMessage.Builder message = ConditionMessage.forCondition("Jwt");

        Resource publicPem = context.getResourceLoader()
                .getResource("classpath:public.pem");

        if(!publicPem.exists()){
            return ConditionOutcome.noMatch(message.didNotFind("public.pem").atAll());
        }

        return ConditionOutcome.match("public.pem found");
    }
}
