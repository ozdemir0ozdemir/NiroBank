package ozdemir0ozdemir.userservice.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.response.ResponseStatus;
import ozdemir0ozdemir.userservice.domain.User;
import ozdemir0ozdemir.userservice.domain.UserService;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;
import ozdemir0ozdemir.userservice.request.RegisterUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest({UserController.class})
@AutoConfigureMockMvc
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

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

}