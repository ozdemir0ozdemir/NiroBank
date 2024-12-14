package ozdemir0ozdemir.userservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.response.ResponseStatus;
import ozdemir0ozdemir.userservice.WithPostgresContainer;
import ozdemir0ozdemir.userservice.domain.Role;
import ozdemir0ozdemir.userservice.domain.User;
import ozdemir0ozdemir.userservice.exception.UserNotFoundException;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;
import ozdemir0ozdemir.userservice.request.RegisterUser;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:test-users-data.sql")
class UserControllerIntegrationTest implements WithPostgresContainer {

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private Integer PORT;

    private final String BASE = "http://localhost";

    private final String VALID_USERNAME = "Starlight123";
    private final String VALID_PASSWORD = "Twinkle@2024";
    private final String INVALID_USERNAME = "Invalid-User";
    private final String INVALID_PASSWORD = "Invalid-Password";

    private static final int TOTAL_USER_COUNT = 20;
    private static final int TOTAL_ROLE_USER_COUNT = 7;
    private static final int TOTAL_ROLE_ADMIN_COUNT = 6;
    private static final int TOTAL_ROLE_MANAGER_COUNT = 7;


    @Test
    void should_saveUser_and_returnProperLocationHeader() throws Exception {
        RegisterUser request = new RegisterUser("test-username", "test-password");

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(request))
                .when()
                .post(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .header(HttpHeaders.LOCATION, containsString(BASE + ":" + PORT + "/api/v1/users/"))
                .body("respondAt", notNullValue())
                .body("status", equalTo(ResponseStatus.SUCCEEDED.name()))
                .body("message", equalTo("User saved successfully"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void should_not_saveUser_when_usernameAlreadyExists() throws Exception {
        final String message = "Username already exists";
        final RegisterUser request = new RegisterUser(VALID_USERNAME, VALID_PASSWORD);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(request))
                .when()
                .post(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("status", equalTo(ResponseStatus.FAILED.name()))
                .body("message", equalTo(message))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void should_getAllUsers_when_searchUsers() throws Exception {

        get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("payload", hasSize(10))
                .body("status", equalTo(ResponseStatus.SUCCEEDED.name()))
                .body("message", equalTo("20 User(s) found"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_getUserByUsername_when_searchUsers_only_with_username_and_userExist() throws Exception {
        given()
                .param("username", VALID_USERNAME)
                .get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("payload", hasSize(1))
                .body("payload.username[0]", equalTo(VALID_USERNAME))
                .body("status", equalTo(ResponseStatus.SUCCEEDED.name()))
                .body("message", equalTo("1 User(s) found"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_not_getUserByUsername_when_searchUsers_only_with_username_and_userDoesNotExist() throws Exception {
        given()
                .accept(MediaType.APPLICATION_JSON.toString())
                .param("username", INVALID_USERNAME)
                .get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("status", equalTo(ResponseStatus.FAILED.name()))
                .body("message", equalTo("User not found"))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_getUsersByRole_when_searchUsers_only_with_role() throws Exception {
        given()
                .param("role", Role.USER)
                .get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("payload", hasSize(TOTAL_ROLE_USER_COUNT))
                .body("respondAt", notNullValue())
                .body("status", equalTo(ResponseStatus.SUCCEEDED.name()))
                .body("message", equalTo(TOTAL_ROLE_USER_COUNT + " User(s) found"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_getUserByUsername_when_searchUsers_with_username_and_role_and_userExist() throws Exception {
        given()
                .param("username", VALID_USERNAME)
                .param("role", Role.USER.name())
                .get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("payload", hasSize(1))
                .body("payload.username[0]", equalTo(VALID_USERNAME))
                .body("status", equalTo(ResponseStatus.SUCCEEDED.name()))
                .body("message", equalTo("1 User(s) found"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_not_getUserByUsername_when_searchUsers_with_username_and_role_and_userDoesNotExist() throws Exception {
        given()
                .param("username", INVALID_USERNAME)
                .param("role", Role.USER.name())
                .get(BASE + ":" + PORT + "/api/v1/users")
                .then()
                .body("respondAt", notNullValue())
                .body("status", equalTo(ResponseStatus.FAILED.name()))
                .body("message", equalTo("User with role not found"))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}