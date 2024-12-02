package ozdemir0ozdemir.userservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ozdemir0ozdemir.userservice.domain.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void should_RegisterNewUser_when_RequestIsValid() throws Exception {

        final String username = "user1";
        final String password = "user1-password";

        RegisterUserRequest requestBody = new RegisterUserRequest(username, password);

        var post = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestBody));

        mvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("/api/v1/users/" + username)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_KeepPageNumberEqualOrGreaterThenZero_when_AllUsersRequested() throws Exception {
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(Mockito.mock(Page.class));

        mvc.perform(get("/api/v1/users?page=-10"))
                .andExpect(status().isOk());
        verify(userService).findAllUsers(eq(0), anyInt());
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_KeepPageSizeEqualOrGreaterThan5_when_AllUsersRequested() throws Exception {
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(Mockito.mock(Page.class));

        mvc.perform(get("/api/v1/users?size=-10"))
                .andExpect(status().isOk());
        verify(userService).findAllUsers(anyInt(), eq(5));
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_KeepPageSizeEqualOrLessThan50_when_AllUsersRequested() throws Exception {
        when(userService.findAllUsers(anyInt(), anyInt()))
                .thenReturn(Mockito.mock(Page.class));

        mvc.perform(get("/api/v1/users?size=123"))
                .andExpect(status().isOk());
        verify(userService).findAllUsers(anyInt(), eq(50));
    }
}