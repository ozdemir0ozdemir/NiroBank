package ozdemir0ozdemir.nirobank.client.userclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.user.Role;
import ozdemir0ozdemir.nirobank.client.userclient.request.ChangeUserPassword;
import ozdemir0ozdemir.nirobank.client.userclient.request.ChangeUserRole;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;

@FeignClient(url = "http://" + "${user-service:localhost:8080}" + "/api/v1/users", name = "Users")
public interface UserClient {

    // CRUD
    @PostMapping
    ResponseEntity<Response<Void>> saveNewUser(@RequestBody RegisterUser request);

    @GetMapping
    ResponseEntity<PagedResponse<User>> searchUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(name = "username", required = false) String username,
                                                    @RequestParam(name = "role", required = false) Role role);

    @GetMapping("/{userId}")
    ResponseEntity<Response<User>> getUserByUserId(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    ResponseEntity<Response<Void>> deleteUserByUserId(@PathVariable Long userId);

    // ACTIONS
    @PostMapping("/login")
    ResponseEntity<Response<User>> login(@RequestBody Login request);

    @PatchMapping("/{userId}/change-role")
    ResponseEntity<Response<Void>> changeUserRoleByUserId(@PathVariable Long userId, @RequestBody ChangeUserRole request);

    @PatchMapping("/{userId}/change-password")
    ResponseEntity<Response<Void>> changeUserPassword(@PathVariable Long userId, @RequestBody ChangeUserPassword request);

}
