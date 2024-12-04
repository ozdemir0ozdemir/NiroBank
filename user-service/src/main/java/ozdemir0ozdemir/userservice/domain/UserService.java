package ozdemir0ozdemir.userservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.userservice.bridge.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(String username, String password) {
        this.saveUser(username, password, Role.USER);
    }

    public void saveUser(String username, String password, Role role) {
        this.userRepository
                .save(new UserEntity()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role));
    }

    // Read Operations
    public Page<User> findUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
    }

    public Page<User> findUsersByRole(int pageNumber, int pageSize, Role role) {
        return this.userRepository
                .findByRole(role, PageRequest.of(pageNumber, pageSize))
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
    }

    public Page<User> findUserByUsernameAndRole(String username, Role role) {
        return this.userRepository
                .findByUsernameAndRole(username, role)
                .map(entity -> new User(entity.getId(), entity.getUsername(), entity.getRole()));
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



    public void deleteUserByUserId(Long id) {
        this.userRepository.deleteById(id);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }
}
