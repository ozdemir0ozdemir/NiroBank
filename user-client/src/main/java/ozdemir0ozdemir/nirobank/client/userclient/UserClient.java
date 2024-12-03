package ozdemir0ozdemir.nirobank.client.userclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8080/api/v1/users", name = "Users")
public interface UserClient {

    @PostMapping
    void registerUser(@RequestBody RegisterUserRequest request);

    @GetMapping("/{username}")
    Response<User> findUserByUsername(@PathVariable String username);

}
