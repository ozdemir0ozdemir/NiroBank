package ozdemir0ozdemir.userservice.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ozdemir0ozdemir.userservice.bridge.User;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.UserService;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserService userService;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userService.deleteAll();
    }

    @Test
    void should_SaveNewUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest("ozdemir", "ozdemir");

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, containsString("/api/v1/users/ozdemir"));

        Optional<User> userOptional = this.userService.findUserByUsername("ozdemir");

        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.id()).isNotNull();
        assertThat(user.username()).isEqualTo("ozdemir");
        assertThat(user.role()).isEqualTo(Role.USER);

    }

    @Test
    void shouldNot_SaveSameUsernameTwice() {

        userService.saveUser("ozdemir", "ozdemir");

        RegisterUserRequest request = new RegisterUserRequest("ozdemir", "ozdemir");

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asPrettyString();
    }


}