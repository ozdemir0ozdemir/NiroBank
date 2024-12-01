package ozdemir0ozdemir.nirobank.authserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ozdemir0ozdemir.common.User;

@FeignClient(url = "http://localhost:8080/api/v1/users", name = "Users")
public interface UserClient {

    @GetMapping("/{username}")
    User findUserByUsername(@PathVariable String username);

    @PostMapping
    Void saveUser(@RequestBody SaveUser request);
}
