package ozdemir0ozdemir.nirobank.authserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8080/api/v1/users", name = "Users")
public interface UserClient {

    @GetMapping("/{username}")
    ResponseEntity<User> findUserByUsername(@PathVariable String username);

    @PostMapping
    ResponseEntity<Void> saveUser(@RequestBody SaveUser request);
}
