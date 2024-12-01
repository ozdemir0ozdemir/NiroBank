package ozdemir0ozdemir.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.common.User;
import ozdemir0ozdemir.userservice.request.SaveUser;
import ozdemir0ozdemir.userservice.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService) {

    @GetMapping("/{username}")
    ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        User user = this.userService().findUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    ResponseEntity<Void> saveUser(@RequestBody SaveUser request) {
        this.userService().saveUser(request.username());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(request.username())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
