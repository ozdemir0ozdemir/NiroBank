package ozdemir0ozdemir.nirobank.tokenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * In this application, the term 'Token' refers to the 'Refresh Token'.
 * The 'Access Token' will also be specified throughout the code.
 *
 * */
@SpringBootApplication
public class TokenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenServiceApplication.class, args);
    }

}
