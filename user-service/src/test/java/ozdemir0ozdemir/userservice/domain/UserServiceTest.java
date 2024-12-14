package ozdemir0ozdemir.userservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor;


    @Test
    void should_saveUser_Successfully() {

        // Given
        final String USERNAME = "username";
        final String PASSWORD = "password";
        final Role ROLE = Role.USER;

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any()))
                .thenReturn(PASSWORD);

        // When
        userService.saveUser(USERNAME, PASSWORD, ROLE);

        // Then
        verify(repository, times(1))
                .save(userEntityArgumentCaptor.capture());

        UserEntity arg = userEntityArgumentCaptor.getValue();

        assertThat(arg).isNotNull();
        assertThat(arg.getUsername()).isEqualTo(USERNAME);
        assertThat(arg.getPassword()).isEqualTo(PASSWORD);
        assertThat(arg.getRole()).isEqualTo(ROLE);
    }

    @Test
    void should_not_saveUser_while_usernameAlreadyExists() {

        // Given
        final String USERNAME = "username";
        final String PASSWORD = "password";
        final Role ROLE = Role.USER;

        final UserEntity givenEntity = new UserEntity(1L, USERNAME, PASSWORD, ROLE);

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.of(givenEntity));

        // When
        assertThatThrownBy(() -> userService.saveUser(USERNAME, PASSWORD, ROLE))
                .isInstanceOf(UsernameAlreadyExistsException.class);

    }

    @Test
    void should_findUserByUsernameAndPassword_successfully() {
        // Given
        final Long ID = 1L;
        final String USERNAME = "username";
        final String PASSWORD = "password";
        final Role ROLE = Role.USER;

        final UserEntity givenEntity = new UserEntity(ID, USERNAME, PASSWORD, ROLE);

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.of(givenEntity));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        // When
        Optional<User> returnedUser = userService.findUserByUsernameAndPassword(USERNAME, PASSWORD);

        assertThat(returnedUser.get()).isNotNull();
        assertThat(returnedUser.get().id()).isEqualTo(ID);
        assertThat(returnedUser.get().username()).isEqualTo(USERNAME);
        assertThat(returnedUser.get().role()).isEqualTo(ROLE);
    }

    @Test
    void should_not_findUserByUsernameAndPassword_while_passwords_not_matches() {
        // Given
        final Long ID = 1L;
        final String USERNAME = "username";
        final String PASSWORD = "password";
        final Role ROLE = Role.USER;

        final UserEntity givenEntity = new UserEntity(ID, USERNAME, PASSWORD, ROLE);

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.of(givenEntity));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        // When
        Optional<User> returnedUser = userService.findUserByUsernameAndPassword(USERNAME, PASSWORD);

        assertThat(returnedUser.isEmpty()).isTrue();
    }

    @Test
    void should_not_findUserByUsernameAndPassword_while_userNotExist() {
        // Given
        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        // When
        Optional<User> returnedUser = userService.findUserByUsernameAndPassword("USERNAME", "PASSWORD");

        assertThat(returnedUser.isEmpty()).isTrue();
    }
}