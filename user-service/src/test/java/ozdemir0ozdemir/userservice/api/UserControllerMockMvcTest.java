package ozdemir0ozdemir.userservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.response.ResponseStatus;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.User;
import ozdemir0ozdemir.userservice.domain.UserService;
import ozdemir0ozdemir.userservice.exception.UserNotFoundException;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;
import ozdemir0ozdemir.userservice.request.RegisterUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest({UserController.class})
@AutoConfigureMockMvc
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    private static final int TOTAL_USER_COUNT = 20;
    private static final int TOTAL_ROLE_USER_COUNT = 7;
    private static final int TOTAL_ROLE_ADMIN_COUNT = 6;
    private static final int TOTAL_ROLE_MANAGER_COUNT = 7;

    @Test
    void should_saveUser_and_returnProperLocationHeader() throws Exception {
        RegisterUser request = new RegisterUser("test-username", "test-password");

        when(userService.saveUser(request.username(), request.password()))
                .thenReturn(1L);

        var postRequest = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        String responseString = mvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(redirectedUrlPattern("**/api/v1/users/1"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        @SuppressWarnings("unchecked")
        Response<Void> userResponse = mapper.readValue(responseString, Response.class);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(userResponse.getMessage()).isEqualTo("User saved successfully");
    }

    @Test
    void should_not_saveUser_when_usernameAlreadyExists() throws Exception {
        final String message = "Username already exists";
        final RegisterUser request = new RegisterUser("test-username", "test-password");

        when(userService.saveUser(request.username(), request.password()))
                .thenThrow(new UsernameAlreadyExistsException(message));

        var postRequest = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        String responseString = mvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION))
                .andReturn()
                .getResponse()
                .getContentAsString();

        @SuppressWarnings("unchecked")
        Response<Void> userResponse = mapper.readValue(responseString, Response.class);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getStatus()).isEqualTo(ResponseStatus.FAILED);
        assertThat(userResponse.getMessage()).isEqualTo(message);

    }

    @Test
    void should_getAllUsers_when_searchUsers() throws Exception {
        // given
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(usersList()));

        // when
        var getRequest = get("/api/v1/users");

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(usersPagedResponse.getPayload().size()).isEqualTo(20);

        verify(userService, times(1)).findAllUsers(0, 10);
    }

    @Test
    void should_getUserByUsername_when_searchUsers_only_with_username_and_userExist() throws Exception {
        //given
        final User requestUser = new User(1L, "test-username", Role.USER);
        when(userService.findUserByUsername("test-username"))
                .thenReturn(requestUser);

        //when
        var getRequest = get("/api/v1/users")
                .param("username", "test-username");

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        //then
        verify(userService, times(1))
                .findUserByUsername("test-username");

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(usersPagedResponse.getTotalItem()).isEqualTo(1L);
        assertThat(usersPagedResponse.getPayload().getFirst()).isEqualTo(requestUser);
    }

    @Test
    void should_not_getUserByUsername_when_searchUsers_only_with_username_and_userDoesNotExist() throws Exception {
        //given
        when(userService.findUserByUsername("test-username"))
                .thenThrow(new UserNotFoundException("User not found"));

        //when
        var getRequest = get("/api/v1/users")
                .param("username", "test-username");

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        //then
        verify(userService, times(1))
                .findUserByUsername("test-username");

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.FAILED);
        assertThat(usersPagedResponse.getTotalItem()).isEqualTo(0L);
    }

    @Test
    void should_getUsersByRole_when_searchUsers_only_with_role() throws Exception {
        //given
        List<User> usersWithRoleUser = usersList().stream()
                .filter(u -> u.role().equals(Role.USER))
                .toList();

        when(userService.findUsersByRole(0, 10, Role.USER))
                .thenReturn(new PageImpl<>(usersWithRoleUser));

        //when
        var getRequest = get("/api/v1/users")
                .param("role", Role.USER.name());

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        //then
        verify(userService, times(1))
                .findUsersByRole(0, 10, Role.USER);

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(usersPagedResponse.getTotalItem()).isGreaterThan(0);
    }

    @Test
    void should_getUserByUsername_when_searchUsers_with_username_and_role_and_userExist() throws Exception {
        //given
        User resultUser = usersList().stream().findAny().get();

        when(userService.findUserByUsernameAndRole(resultUser.username(), resultUser.role()))
                .thenReturn(resultUser);

        //when
        var getRequest = get("/api/v1/users")
                .param("username", resultUser.username())
                .param("role", resultUser.role().name());

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        //then
        verify(userService, times(1))
                .findUserByUsernameAndRole(resultUser.username(), resultUser.role());

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(usersPagedResponse.getTotalItem()).isEqualTo(1L);
        assertThat(usersPagedResponse.getPayload().getFirst()).isEqualTo(resultUser);
    }

    @Test
    void should_not_getUserByUsername_when_searchUsers_with_username_and_role_and_userDoesNotExist() throws Exception {
        //given
        final String message = "User with role not found";

        when(userService.findUserByUsernameAndRole(any(), any()))
                .thenThrow(new UserNotFoundException("User with role not found"));

        //when
        var getRequest = get("/api/v1/users")
                .param("username", "test-username")
                .param("role", Role.USER.name());

        String responseString = mvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<User> usersPagedResponse = mapper.readValue(responseString, new TypeReference<>() {
        });

        //then
        verify(userService, times(1))
                .findUserByUsernameAndRole(any(), any());

        assertThat(usersPagedResponse.getStatus()).isEqualTo(ResponseStatus.FAILED);
        assertThat(usersPagedResponse.getTotalItem()).isEqualTo(0L);
        assertThat(usersPagedResponse.getPayload()).isNull();
    }

    @Test
    void should_pageMin0_sizeMin5_when_searchUsers() throws Exception {
        // given
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(usersList()));

        // when
        var getRequest = get("/api/v1/users")
                .param("page", "-10")
                .param("size", "-10");

        mvc.perform(getRequest)
                .andExpect(status().isOk());

        // then
        verify(userService, times(1)).findAllUsers(0, 5);
    }

    @Test
    void should_sizeMax50_when_searchUsers() throws Exception {
        // given
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(usersList()));

        // when
        var getRequest = get("/api/v1/users")
                .param("size", "2000");

        mvc.perform(getRequest)
                .andExpect(status().isOk());

        // then
        verify(userService, times(1)).findAllUsers(0, 50);
    }

    // Helpers
    private List<User> usersList() {
        return List.of(
                new User(1L, "Starlight123", Role.USER),
                new User(2L, "MoonWalker", Role.MANAGER),
                new User(3L, "CosmicDreamer", Role.ADMIN),
                new User(4L, "NeonNinja", Role.USER),
                new User(5L, "SolarFlare", Role.MANAGER),
                new User(6L, "QuantumLeap", Role.ADMIN),
                new User(7L, "PixelPioneer", Role.USER),
                new User(8L, "EclipseMaster", Role.MANAGER),
                new User(9L, "GalacticHero", Role.ADMIN),
                new User(10L, "MysticMage", Role.USER),
                new User(11L, "CyberSamurai", Role.MANAGER),
                new User(12L, "AuroraBorealis", Role.ADMIN),
                new User(13L, "EchoPhoenix", Role.USER),
                new User(14L, "ZenithZero", Role.MANAGER),
                new User(15L, "VelocityViper", Role.ADMIN),
                new User(16L, "NebulaKnight", Role.USER),
                new User(17L, "DigitalDragon", Role.MANAGER),
                new User(18L, "StormStriker", Role.ADMIN),
                new User(19L, "AetherArtist", Role.USER),
                new User(20L, "NimbusNavigator", Role.MANAGER)
        );
    }

}