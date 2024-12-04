package ozdemir0ozdemir.userservice.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres
            = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void should_SaveUser_successfully() {

        UserEntity entity = new UserEntity()
                .setUsername("user1")
                .setPassword("password")
                .setRole(Role.USER);

        userRepository.save(entity);

        UserEntity mustExist = userRepository.findByUsername("user1", PageRequest.of(0, 1))
                .getContent()
                .getFirst();

        assertThat(mustExist.getId()).isNotNull().isNotNegative();
        assertThat(mustExist.getUsername()).isEqualTo("user1");
        assertThat(mustExist.getPassword()).isEqualTo("password");
        assertThat(mustExist.getRole()).isEqualTo(Role.USER);

    }

    @Test
    void should_FindUserByUserId() {

        UserEntity entity = new UserEntity()
                .setUsername("user1")
                .setPassword("password")
                .setRole(Role.USER);

        Long savedId = userRepository.save(entity).getId();
        assertThat(savedId).isNotNull().isNotNegative();

        UserEntity savedEntity = userRepository.findById(savedId).get();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(savedId);
        assertThat(savedEntity.getUsername()).isEqualTo("user1");
        assertThat(savedEntity.getPassword()).isEqualTo("password");
        assertThat(savedEntity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void should_FindUserByUsername() {

        UserEntity entity = new UserEntity()
                .setUsername("user1")
                .setPassword("password")
                .setRole(Role.USER);

        userRepository.save(entity).getId();

        UserEntity savedEntity = userRepository
                .findByUsername(entity.getUsername(), PageRequest.of(0, 1))
                .getContent()
                .getFirst();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getUsername()).isEqualTo("user1");
        assertThat(savedEntity.getPassword()).isEqualTo("password");
        assertThat(savedEntity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void should_FindUsersByRole() {

        final int TOTAL_ENTITY = 5;
        for (int i = 0; i < TOTAL_ENTITY; i++) {
            userRepository.save(new UserEntity()
                    .setUsername("user" + i)
                    .setPassword("password")
                    .setRole(Role.USER));
        }

        List<UserEntity> entities = this.userRepository
                .findByRole(Role.USER, PageRequest.of(0, 10))
                .getContent();

        assertThat(entities.size()).isEqualTo(TOTAL_ENTITY);

        for (int i = 0; i < TOTAL_ENTITY; i++) {
            assertThat(entities.get(i).getRole()).isEqualTo(Role.USER);
        }
    }

    @Test
    void should_FindUserByUsernameAndRole() {

        UserEntity entity1 = new UserEntity()
                .setUsername("admin1")
                .setPassword("password")
                .setRole(Role.ADMIN);

        UserEntity entity2 = new UserEntity()
                .setUsername("admin2")
                .setPassword("password")
                .setRole(Role.ADMIN);


        this.userRepository.save(entity1);
        this.userRepository.save(entity2);

        List<UserEntity> entities = this.userRepository
                .findByUsernameAndRole("admin1", Role.ADMIN, PageRequest.of(0, 10))
                .getContent();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.getFirst().getUsername()).isEqualTo("admin1");
        assertThat(entities.getFirst().getPassword()).isEqualTo("password");
        assertThat(entities.getFirst().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void should_ChangePasswordByUsername() {

        UserEntity entity1 = new UserEntity()
                .setUsername("admin1")
                .setPassword("password")
                .setRole(Role.ADMIN);

        Long savedId = this.userRepository.save(entity1).getId();

        this.userRepository.changePasswordByUsername(entity1.getUsername(), "secret");

        UserEntity updated = this.userRepository.findById(savedId).get();
        assertThat(updated.getUsername()).isEqualTo(entity1.getUsername());
        assertThat(updated.getPassword()).isEqualTo("secret");
    }

    @Test
    void should_ChangeRoleByUsername() {

        UserEntity entity = new UserEntity()
                .setUsername("user")
                .setPassword("password")
                .setRole(Role.USER);

        Long savedId = this.userRepository.save(entity).getId();

        this.userRepository.changeUserRoleByUsername(entity.getUsername(), Role.MANAGER);

        UserEntity updated = this.userRepository.findById(savedId).get();
        assertThat(updated.getUsername()).isEqualTo(entity.getUsername());
        assertThat(updated.getPassword()).isEqualTo("password");
        assertThat(updated.getRole()).isEqualTo(Role.MANAGER);
    }
}