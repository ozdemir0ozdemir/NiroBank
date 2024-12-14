package ozdemir0ozdemir.userservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.userservice.exception.UserNotFoundException;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long saveUser(String username, String password) {
        return this.saveUser(username, password, Role.USER).getId();
    }

    public UserEntity saveUser(String username, String password, Role role) {

        boolean usernameFound = this.userRepository.findByUsername(username)
                .isPresent();

        if (usernameFound) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        return this.userRepository
                .save(new UserEntity()
                        .setUsername(username)
                        .setPassword(passwordEncoder.encode(password))
                        .setRole(role));
    }

    // Read Operations
    public User findUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .map(UserService::entityToUser)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Page<User> findUsersByRole(int pageNumber, int pageSize, Role role) {
        return this.userRepository
                .findAllByRole(role, PageRequest.of(pageNumber, pageSize))
                .map(UserService::entityToUser);
    }

    public User findUserByUsernameAndRole(String username, Role role) {
        return this.userRepository
                .findByUsernameAndRole(username, role)
                .map(UserService::entityToUser)
                .orElseThrow(() -> new UserNotFoundException("User with role not found"));
    }

    public Page<User> findAllUsers(int pageNumber, int pageSize) {
        return this.userRepository
                .findAll(PageRequest.of(pageNumber, pageSize))
                .map(UserService::entityToUser);
    }

    public Optional<User> findUserById(Long userId) {
        return this.userRepository
                .findById(userId)
                .map(UserService::entityToUser);
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        Optional<UserEntity> entity = this.userRepository
                .findByUsername(username)
                .stream()
                .findFirst();

        if (entity.isPresent() && passwordEncoder.matches(password, entity.get().getPassword())) {
            return entity.map(UserService::entityToUser);
        }
        return Optional.empty();
    }

    // Update Operations
    public void changeUserRoleByUsernameAndUserId(Role role, String username) {
        this.userRepository
                .changeUserRoleByUsername(username, role);
    }

    public void changeUserPassword(String username, String password) {
        this.userRepository
                .changePasswordByUsername(username, passwordEncoder.encode(password));
    }

    // Delete Operations
    public void deleteUserByUserId(Long id) {
        this.userRepository.deleteById(id);
    }

    // Static Helper
    private static User entityToUser(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getRole());
    }
}
