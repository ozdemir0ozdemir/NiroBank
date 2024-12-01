package ozdemir0zdemir.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0zdemir.userservice.model.User;
import ozdemir0zdemir.userservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(String username) {
        this.saveUser(username, List.of("USER"));
    }

    public User findUserById(Long userId) {
        return this.userRepository
                .getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // FIXME: Customize Exception
    }

    public User findUserByUsername(String username) {
        return this.userRepository
                .getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")); // FIXME: Customize Exception
    }

    private void saveUser(String username, List<String> authorities) {
        this.userRepository.saveUser(username, authorities);
    }


}
