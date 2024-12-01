package ozdemir0ozdemir.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ozdemir0ozdemir.userservice.service.UserService;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner addExampleUsersRunner(UserService userService) {
        return args -> {
            userService.saveUser("spring-user");
            userService.saveUser("user");
        };
    }

}
