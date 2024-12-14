package ozdemir0ozdemir.userservice.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ozdemir0ozdemir.userservice.WithPostgresContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:test-users-data.sql")
class UserRepositoryTest implements WithPostgresContainer {

    @Autowired
    private UserRepository userRepository;

    private static final int TOTAL_USER_COUNT = 20;
    private static final int TOTAL_ROLE_USER_COUNT = 7;
    private static final int TOTAL_ROLE_ADMIN_COUNT = 6;
    private static final int TOTAL_ROLE_MANAGER_COUNT = 7;

    @Test
    void should_saveUser() {
        UserEntity request = new UserEntity()
                .setUsername("test-username")
                .setPassword("test-password")
                .setRole(Role.ADMIN);

        UserEntity savedEntity = userRepository.save(request);

        Page<UserEntity> userEntitiesPage =
                userRepository.findAll(PageRequest.of(0, TOTAL_USER_COUNT + 1));

        Optional<UserEntity> foundEntity = userEntitiesPage
                .stream()
                .filter(ent -> ent.getUsername().equals("test-username"))
                .findFirst();

        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_USER_COUNT + 1);
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(foundEntity.isPresent()).isTrue();
        assertThat(savedEntity).isEqualTo(foundEntity.get());
    }

    @Test
    void should_findAllUsers() {

        Page<UserEntity> userEntitiesPage =
                userRepository.findAll(PageRequest.of(0, 10));

        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_USER_COUNT);
        assertThat(userEntitiesPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    void should_findAllByRole_roleIs_ADMIN() {

        Page<UserEntity> userEntitiesPage =
                userRepository.findAllByRole(Role.ADMIN, PageRequest.of(0, TOTAL_USER_COUNT));

        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_ROLE_ADMIN_COUNT);
    }

    @Test
    void should_findAllByRole_roleIs_USER() {

        Page<UserEntity> userEntitiesPage =
                userRepository.findAllByRole(Role.USER, PageRequest.of(0, TOTAL_USER_COUNT));

        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_ROLE_USER_COUNT);
    }

    @Test
    void should_findAllByRole_roleIs_MANAGER() {

        Page<UserEntity> userEntitiesPage =
                userRepository.findAllByRole(Role.MANAGER, PageRequest.of(0, TOTAL_USER_COUNT));

        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_ROLE_MANAGER_COUNT);
    }

    @Test
    void should_findByUsername() {

        // from test-users-data.sql
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByUsername("Starlight123");

        assertThat(optionalUserEntity.isPresent()).isTrue();
        assertThat(optionalUserEntity.get().getUsername()).isEqualTo("Starlight123");
        assertThat(optionalUserEntity.get().getPassword()).isEqualTo("Twinkle@2024");
        assertThat(optionalUserEntity.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void should_not_findByUsername_when_userNotExist() {

        // from test-users-data.sql
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByUsername("test-username");

        assertThat(optionalUserEntity.isPresent()).isFalse();
    }

    @Test
    void should_findByUserId() {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(1L);

        assertThat(optionalUserEntity.isPresent()).isTrue();
        assertThat(optionalUserEntity.get().getId()).isNotNull().isNotNegative();
        assertThat(optionalUserEntity.get().getUsername()).isNotNull().isNotBlank();
        assertThat(optionalUserEntity.get().getPassword()).isNotNull().isNotBlank();
        assertThat(optionalUserEntity.get().getRole()).isNotNull();
    }

    @Test
    void should_not_findByUserId_when_userNotExist() {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findById(TOTAL_USER_COUNT + 10L);

        assertThat(optionalUserEntity.isPresent()).isFalse();
    }

    @Test
    void should_findByUsernameAndRole() {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByUsernameAndRole("Starlight123", Role.USER);

        assertThat(optionalUserEntity.isPresent()).isTrue();
        assertThat(optionalUserEntity.get().getId()).isNotNull().isNotNegative();
        assertThat(optionalUserEntity.get().getUsername()).isEqualTo("Starlight123");
        assertThat(optionalUserEntity.get().getPassword()).isEqualTo("Twinkle@2024");
        assertThat(optionalUserEntity.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void should_findByUsernameAndRole_when_roleIsWrong() {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByUsernameAndRole("Starlight123", Role.ADMIN);

        assertThat(optionalUserEntity.isPresent()).isFalse();
    }

    @Test
    void should_findByUsernameAndRole_when_userNotExist() {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByUsernameAndRole("test-username", Role.USER);

        assertThat(optionalUserEntity.isPresent()).isFalse();
    }

    @Test
    void should_changePasswordByUsername() {
        int affectedRowCount = userRepository
                .changePasswordByUsername("Starlight123", "test-password");

        Optional<UserEntity> userEntity =
                userRepository.findByUsername("Starlight123");

        assertThat(affectedRowCount).isEqualTo(1);
        assertThat(userEntity.isPresent()).isTrue();
        assertThat(userEntity.get().getUsername()).isEqualTo("Starlight123");
        assertThat(userEntity.get().getPassword()).isEqualTo("test-password");
        assertThat(userEntity.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void should_not_changePasswordByUsername_when_userNotExist() {
        int affectedRowCount = userRepository
                .changePasswordByUsername("test-username", "test-password");

        assertThat(affectedRowCount).isEqualTo(0);
    }

    @Test
    void should_changeRoleByUsername() {
        int affectedRowCount = userRepository
                .changeUserRoleByUsername("Starlight123", Role.ADMIN);

        Optional<UserEntity> userEntity =
                userRepository.findByUsername("Starlight123");

        assertThat(affectedRowCount).isEqualTo(1);
        assertThat(userEntity.isPresent()).isTrue();
        assertThat(userEntity.get().getUsername()).isEqualTo("Starlight123");
        assertThat(userEntity.get().getPassword()).isEqualTo("Twinkle@2024");
        assertThat(userEntity.get().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void should_not_changeRoleByUsername_when_userNotExist() {
        int affectedRowCount = userRepository
                .changeUserRoleByUsername("test-username", Role.ADMIN);

        assertThat(affectedRowCount).isEqualTo(0);
    }

    @Test
    void should_deleteUserByUserId() {

        int affectedRowCount = userRepository.deleteById(1L);

        Page<UserEntity> userEntitiesPage =
                userRepository.findAll(PageRequest.of(0, TOTAL_USER_COUNT));

        assertThat(affectedRowCount).isEqualTo(1);
        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_USER_COUNT - 1);
    }

    @Test
    void should_not_deleteUserByUserId_when_userNotExit() {

        int affectedRowCount = userRepository.deleteById(TOTAL_USER_COUNT + 10L);

        Page<UserEntity> userEntitiesPage =
                userRepository.findAll(PageRequest.of(0, TOTAL_USER_COUNT));

        assertThat(affectedRowCount).isEqualTo(0);
        assertThat(userEntitiesPage.getTotalElements()).isEqualTo(TOTAL_USER_COUNT);
    }

}