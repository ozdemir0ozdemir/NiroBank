package ozdemir0ozdemir.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.UserService;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
    CommandLineRunner addExampleUsersRunner(UserService userService) {
        return _ -> {
            boolean isAnyUserExists  = userService.findAllUsers(0, 20).getTotalElements() > 0L;
            if(!isAnyUserExists){
                userService.saveUser("admin", "admin", Role.ADMIN);
                userService.saveUser("manager", "manager", Role.MANAGER);
                for(int i = 0; i < 2; i++) {
                    userService.saveUser("user"+i, "user", Role.USER);
                }
            }
        };
    }

}
