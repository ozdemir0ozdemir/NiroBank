package ozdemir0ozdemir.nirobank.accountservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ClockConfiguration {

     @Bean
     Clock clock(){
          return Clock.systemUTC();
     }
}
