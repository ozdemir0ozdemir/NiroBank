package ozdemir0ozdemir.nirobank.client.userclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.user.Role;
import ozdemir0ozdemir.nirobank.client.userclient.request.ChangeUserPassword;
import ozdemir0ozdemir.nirobank.client.userclient.request.ChangeUserRole;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;

@FeignClient(url = "http://"+"${user-service:localhost:8080}"+"/api/v1/users", name = "Users")
public interface UserClient {

    @PostMapping
    Response<Void> registerUser(@RequestBody RegisterUser request);

    @GetMapping
    PagedResponse<User> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(name = "username", required = false) String username,
                                                    @RequestParam(name = "role", required = false) Role role);

    @GetMapping("/{userId}")
    Response<User> getUserByUserId(@PathVariable Long userId);

    @PostMapping("/login")
    Response<User> login(@RequestBody Login request);

    @PatchMapping
    void changeUserRoleByUserId(@RequestBody ChangeUserRole request);

    @PatchMapping
    void changeUserPassword(@RequestBody ChangeUserPassword request);

    @DeleteMapping("/{userId}")
    void deleteUserByUserId(@PathVariable Long userId);

}
