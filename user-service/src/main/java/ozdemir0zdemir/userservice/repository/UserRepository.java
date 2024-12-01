package ozdemir0zdemir.userservice.repository;

import org.springframework.stereotype.Repository;
import ozdemir0zdemir.userservice.model.User;

import java.util.*;

@Repository
public class UserRepository {

    private final Set<User> users = new HashSet<>();
    private final Random random = new Random();

    public void saveUser(String username, List<String> authorities) {
        this.users.add(new User(random.nextLong(), username, authorities));
    }

    public Optional<User> getUserById(Long userId) {
        return this.users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    public Optional<User> getUserByUsername(String username) {
        return this.users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }



}
