package ozdemir0ozdemir.userservice.repository;

import org.springframework.stereotype.Repository;
import ozdemir0ozdemir.userservice.model.UserEntity;

import java.util.*;

@Repository
public class UserRepository {

    private final Set<UserEntity> users = new HashSet<>();
    private final Random random = new Random();

    public void saveUser(String username, List<String> authorities) {
        this.users.add(new UserEntity(random.nextLong(), username, authorities));
    }

    public Optional<UserEntity> getUserById(Long userId) {
        return this.users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return this.users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }



}
