package ozdemir0ozdemir.nirobank.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.util.Base64;

@Configuration
@Conditional(JwtCondition.class)
public class JwtConfiguration {

    private final Clock clock = Clock.systemUTC();

    @Bean
    Jwt jwt(ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource("classpath:public.pem");
            String publicKeyString = resource.getContentAsString(Charset.defaultCharset())
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");

            return new Jwt(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString)), clock);
        }
        catch (IOException ex) {
            throw new RuntimeException("Public Key cannot be read");
        }
    }
}
