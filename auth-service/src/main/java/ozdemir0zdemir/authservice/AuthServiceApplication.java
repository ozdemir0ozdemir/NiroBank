package ozdemir0zdemir.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ozdemir0ozdemir.common.util.JwtConfig;
import ozdemir0ozdemir.common.util.JwtUtil;

@SpringBootApplication
@EnableFeignClients(basePackages = "ozdemir0ozdemir.nirobank.client")
@Import({JwtConfig.class, JwtUtil.class})
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}