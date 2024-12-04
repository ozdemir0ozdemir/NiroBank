package ozdemir0ozdemir.nirobank.tokenservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ozdemir0ozdemir.nirobank.client.userclient.Response;
import ozdemir0ozdemir.nirobank.client.userclient.Role;
import ozdemir0ozdemir.nirobank.client.userclient.User;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.tokenservice.bridge.Token;
import ozdemir0ozdemir.nirobank.tokenservice.domain.JwtService;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenService;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenGenerationException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserClient userClient;

    @Test
    void should_GenerateNewAccessToken() throws Exception {

        User user = new User(1L, "user", Role.USER);
        when(userClient.findUserByUsername("user"))
                .thenReturn(Response.found(user));

        GenerateAccessToken request = new GenerateAccessToken("user");
        var post = post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        String response = mvc.perform(post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Token token = mapper.readValue(response, Token.class);

        Claims claims = jwtService.getClaimsFrom(token.accessToken());

        assertThat(claims.getSubject()).isEqualTo("user");
        assertThat(claims.getExpiration()).isAfter(Instant.now().plus(29L, ChronoUnit.MINUTES));

        this.tokenService.revokeTokenFor("user");
    }

    @Test
    void should_NotGenerateToken_when_GeneratingSecondTokenForSameUser() throws Exception {

        User user = new User(1L, "user", Role.USER);
        when(userClient.findUserByUsername("user"))
                .thenReturn(Response.found(user));

        this.tokenService.generateTokenSet("user");

        GenerateAccessToken request = new GenerateAccessToken("user");
        var post = post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        mvc.perform(post)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("You already have an access token")))
                .andExpect(jsonPath("$.status", equalToIgnoringCase("failed")));

        this.tokenService.revokeTokenFor("user");
    }

    @Test
    void should_NotReturnAnyToken_when_UserHasNoToken() throws Exception {

        User user = new User(1L, "user", Role.USER);
        when(userClient.findUserByUsername("user"))
                .thenReturn(Response.found(user));

        var get = get("/api/v1/auth/{username}", "user");
        mvc.perform(get)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalToIgnoringCase("Token not found")))
                .andExpect(jsonPath("$.status", equalToIgnoringCase("failed")));
    }


}