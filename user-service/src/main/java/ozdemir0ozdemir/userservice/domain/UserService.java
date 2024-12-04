package ozdemir0ozdemir.userservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;

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

        boolean usernameFound = !this.userRepository.findByUsername(username)
                .toList()
                .isEmpty();

        if(usernameFound){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

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
                .map(UserService::entityToUser);
    }

    public Page<User> findUsersByRole(int pageNumber, int pageSize, Role role) {
        return this.userRepository
                .findByRole(role, PageRequest.of(pageNumber, pageSize))
                .map(UserService::entityToUser);
    }

    public Page<User> findUserByUsernameAndRole(String username, Role role) {
        return this.userRepository
                .findByUsernameAndRole(username, role)
                .map(UserService::entityToUser);
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

    // Update Operations
    public void changeUserRoleByUsernameAndUserId(Role role, String username, Long userId) {
        this.userRepository
                .changeUserRoleByUsernameAndUserId(role, username, userId);
    }

    public void changeUserPassword(String username, String password) {
        this.userRepository
                .changePasswordByUsername(username, passwordEncoder.encode(password));
    }

    // Delete Operations
    public void deleteUserByUserId(Long id) {
        this.userRepository.deleteById(id);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }

    // Static Helper
    private static User entityToUser(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getRole());
    }
}
