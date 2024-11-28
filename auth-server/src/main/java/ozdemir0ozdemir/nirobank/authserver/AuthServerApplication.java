package ozdemir0ozdemir.nirobank.authserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ozdemir0ozdemir.nirobank.authserver.configuration.BearerTokenConfiguration;
import ozdemir0ozdemir.nirobank.authserver.service.BearerTokenService;

import java.nio.charset.Charset;

@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    CommandLineRunner lineRunner(BearerTokenService service) {
        return args -> {
            System.out.println("Token: " + service.generateToken());
        };
    }

}
