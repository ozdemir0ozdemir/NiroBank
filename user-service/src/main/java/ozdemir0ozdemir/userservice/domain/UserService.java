package ozdemir0ozdemir.userservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.userservice.bridge.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(String username) {
        this.saveUser(username, Role.USER);
    }

    public void saveUser(String username, Role role) {
        this.userRepository
                .save(new UserEntity()
                .setUsername(username)
                .setRole(role));
    }

    public Page<User> findAllUsers(int pageNumber, int pageSize) {
        return this.userRepository
                .findAll(PageRequest.of(pageNumber, pageSize))
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
    }

    public Optional<User> findUserById(Long userId) {
        return this.userRepository
                .findById(userId)
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
    }

    public Optional<User> findUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
    }

}
