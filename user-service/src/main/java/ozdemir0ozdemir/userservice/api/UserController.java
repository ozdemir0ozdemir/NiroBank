package ozdemir0ozdemir.userservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.userservice.bridge.PagedResponse;
import ozdemir0ozdemir.userservice.bridge.Response;
import ozdemir0ozdemir.userservice.bridge.User;
import ozdemir0ozdemir.userservice.domain.UserService;

import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService) {

    @PostMapping
    ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequest request) {
        this.userService.saveUser(request.username(), request.password());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(request.username())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    ResponseEntity<Void> login() {
        throw new UnsupportedOperationException("Use token-service to implement login");
    }

    @GetMapping
    ResponseEntity<PagedResponse<User>> findAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        page = Math.max(0, page - 1);
        size = Math.min(Math.max(5, size), 50);
        return ResponseEntity.ok(PagedResponse.usersPage(this.userService.findAllUsers(page, size)));
    }

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
