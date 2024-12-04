package ozdemir0ozdemir.userservice.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.userservice.bridge.PagedResponse;
import ozdemir0ozdemir.userservice.bridge.Response;
import ozdemir0ozdemir.userservice.bridge.User;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.UserService;

import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService) {

    // TODO: Implement all CRUD Functionality
    // 1 - Create User
    // 2 - Get All Users
    // 3 - Make all users endpoint using search (username & role)
    // 4 - Get a User by userId
    // 5 - Delete By userId
    // 6 - Change user's role by userId
    // 7 - Change user's password by userId


    @PostMapping
    ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequest request) {
        this.userService.saveUser(request.username(), request.password());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(request.username())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    
    @GetMapping
    ResponseEntity<PagedResponse<User>> findAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                     @RequestParam(name = "username", required = false) String username,
                                                     @RequestParam(name = "role", required = false) Role role) {

        page = Math.max(0, page - 1);
        size = Math.min(Math.max(5, size), 50);

        Page<User> usersPage;

        if(username != null && role != null){
            usersPage = this.userService.findUserByUsernameAndRole(username, role);
        }
        else if(username == null && role != null){
            usersPage = this.userService.findUsersByRole(page, size, role);
        }
        else if(username != null && role == null){
            usersPage = this.userService.findUserByUsername(username);
        }
        else {
            usersPage = this.userService.findAllUsers(page, size);
        }

        return ResponseEntity.ok(PagedResponse.of(usersPage));
    }

    // FIXME: change path variable to user-id
    @GetMapping("/{username}")
    ResponseEntity<Response<User>> findUserByUsername(@PathVariable String username) {
        return this.userService()
                .findUserByUsername(username)
                .map(user -> ResponseEntity.ok(Response.found(user)))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(Response.notFound()));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUserByUserId(@PathVariable Long userId) {
        this.userService.deleteUserByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
