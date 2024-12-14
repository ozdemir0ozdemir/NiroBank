package ozdemir0ozdemir.userservice.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.User;
import ozdemir0ozdemir.userservice.domain.UserService;
import ozdemir0ozdemir.userservice.request.ChangeUserPassword;
import ozdemir0ozdemir.userservice.request.ChangeUserRole;
import ozdemir0ozdemir.userservice.request.Login;
import ozdemir0ozdemir.userservice.request.RegisterUser;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService) {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // CRUD
    @PostMapping
    ResponseEntity<Response<Void>> saveNewUser(@RequestBody RegisterUser request) {
        Long savedUserId = this.userService.saveUser(request.username(), request.password());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(savedUserId)
                .toUri();

        return ResponseEntity.created(location).body(Response.succeeded("User saved successfully"));
    }

    @GetMapping
    ResponseEntity<PagedResponse<User>> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                     @RequestParam(name = "username", required = false) String username,
                                                     @RequestParam(name = "role", required = false) Role role) {

        int pageNumber = Math.max(0, page - 1);
        int pageSize = Math.min(Math.max(5, size), 50);

        Page<User> usersPage;

        if(username == null && role == null){
            usersPage = userService.findAllUsers(pageNumber, pageSize);
        }
        else if(username == null){
            usersPage = userService.findUsersByRole(pageNumber, pageSize, role);
        }
        else if(role == null){
            usersPage = new PageImpl<>(List.of(userService.findUserByUsername(username)));
        }
        else {
            usersPage = new PageImpl<>(List.of(userService.findUserByUsernameAndRole(username, role)));
        }

        return ResponseEntity.ok(PagedResponse.succeeded(usersPage, usersPage.get() + " User(s) found"));
    }

    @GetMapping("/{userId}")
    ResponseEntity<Response<User>> getUserByUserId(@PathVariable Long userId) {
        return this.userService()
                .findUserById(userId)
                .map(user -> Response.succeeded(user, "User found"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(Response.failed("User not found")));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUserByUserId(@PathVariable Long userId) {
        this.userService.deleteUserByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    // ACTIONS
    @PostMapping("/login")
    ResponseEntity<Response<User>> login(@RequestBody Login request) {
        return this.userService()
                .findUserByUsernameAndPassword(request.username(), request.password())
                .map(user -> Response.succeeded(user, "User found"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(Response.failed("User not found")));
    }

    @PatchMapping("/{userId}/change-role")
    ResponseEntity<Void> changeUserRoleByUserId(@PathVariable Long userId, @RequestBody ChangeUserRole request) {

        this.userService.changeUserRoleByUsernameAndUserId(request.role(), request.username());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/change-password")
    ResponseEntity<Void> changeUserPassword(@PathVariable Long userId, @RequestBody ChangeUserPassword request) {

        this.userService.changeUserPassword(request.username(), request.password());
        return ResponseEntity.noContent().build();
    }
}
