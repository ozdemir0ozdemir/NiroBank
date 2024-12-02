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
    ResponseEntity<Void> saveUser(@RequestBody SaveUserRequest request) {
        this.userService().saveUser(request.username());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(request.username())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    ResponseEntity<PagedResponse<User>> findAllUsers(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        page = Math.max(0, page - 1);
        size = Math.min(5, size);
        return ResponseEntity.ok(PagedResponse.usersPage(this.userService.findAllUsers(page, size)));
    }

    @GetMapping("/{username}")
    ResponseEntity<Response<User>> findUserByUsername(@PathVariable String username) {
        return this.userService()
                .findUserByUsername(username)
                .map(user -> ResponseEntity.ok(Response.userFound(user)))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(Response.userNotFound()));
    }


}
