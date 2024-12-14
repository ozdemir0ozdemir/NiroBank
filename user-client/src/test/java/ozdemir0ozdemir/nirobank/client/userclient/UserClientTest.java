package ozdemir0ozdemir.nirobank.client.userclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.response.ResponseStatus;
import ozdemir0ozdemir.common.user.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
class UserClientTest {

    private static ClientAndServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private UserClient userClient;

    @BeforeAll
    static void startServer() {
        mockServer = ClientAndServer.startClientAndServer(8080);
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }

    // This test is sufficient since there are no implementations.
    @Test
    void should_GetUserById() throws Exception {
        // Given
        Response<User> body =
               Response.succeeded(new User(1L, "user1", Role.USER), "User found");

        var request = request()
                .withMethod("GET")
                .withPath("/api/v1/users/1");

        var response = response()
                .withStatusCode(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(mapper.writeValueAsString(body));

        mockServer
                .when(request)
                .respond(response);

        // When
        Response<User> userResponse = userClient.getUserByUserId(1L).getBody();

        // Then
        assertThat(userResponse.getStatus()).isEqualTo(ResponseStatus.SUCCEEDED);
        assertThat(userResponse.getMessage()).isEqualTo("User found");
        assertThat(userResponse.getPayload().id()).isEqualTo(1L);
        assertThat(userResponse.getPayload().username()).isEqualTo("user1");
        assertThat(userResponse.getPayload().role()).isEqualTo(Role.USER);
        mockServer.verify(request, VerificationTimes.exactly(1));
    }
}