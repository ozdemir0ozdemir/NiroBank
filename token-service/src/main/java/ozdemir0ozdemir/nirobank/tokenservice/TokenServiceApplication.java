package ozdemir0ozdemir.nirobank.tokenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * In this application, the term 'Token' refers to the 'Refresh Token'.
 * The 'Access Token' will also be specified throughout the code.
 *
 * */
@SpringBootApplication
@EnableFeignClients(basePackages = "ozdemir0ozdemir.nirobank.client")
public class TokenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenServiceApplication.class, args);
    }

}
