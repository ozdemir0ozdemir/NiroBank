package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ozdemir0ozdemir.nirobank.client.userclient.Response;
import ozdemir0ozdemir.nirobank.client.userclient.User;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.tokenservice.bridge.Token;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenGenerationException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceUnitTest {

    @Mock
    JwtService jwtService;

    @Mock
    TokenRepository repository;

    @Mock
    UserClient userClient;

    @InjectMocks
    TokenService tokenService;

    @Test
    void should_GenerateNewAccessToken() {

        final String username = "ozdemir";
        User user = new User(1L, username, List.of("USER:read"));

        when(jwtService.generateBearerTokenFor(username, user.authorities()))
                .thenReturn("accessToken");

        when(jwtService.generateRefreshTokenFor(username))
                .thenReturn("refreshToken");

        when(userClient.findUserByUsername(anyString()))
                .thenReturn(Response.found(user));

        when(repository.findTokenByUsername(username))
                .thenReturn(Optional.empty());

        Token token = tokenService.generateTokenSet(username);

        assertThat(token.accessToken()).isEqualTo("accessToken");
        assertThat(token.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void shouldNot_GenerateNewAccessToken() {

        final String username = "ozdemir";
        User user = new User(1L, username, List.of("USER:read"));

        when(userClient.findUserByUsername(anyString()))
                .thenReturn(Response.found(user));

        when(repository.findTokenByUsername(username))
                .thenReturn(Optional.of(new Token("accessToken", "refreshToken")));

        assertThatThrownBy(() -> tokenService.generateTokenSet(username))
                .isInstanceOf(TokenGenerationException.class);

    }

    @Test
    void should_GetExistingToken_when_RefreshTokenRequested() {
        final String username = "ozdemir";
        User user = new User(1L, username, List.of("USER:read"));
        Token token = new Token("accessToken", "refreshToken");

        when(userClient.findUserByUsername(anyString()))
                .thenReturn(Response.found(user));

        when(repository.findTokenByUsernameAndRefreshToken(any(), any()))
                .thenReturn(Optional.of(token));

        when(jwtService.isTokenExpired(anyString()))
                .thenReturn(false);

        Token responseToken = tokenService.refreshOrGetTokenSet(username, "refreshToken");
        assertThat(token).isEqualTo(responseToken);
        verify(repository, never()).revokeToken(any(), any());
    }

    @Test
    void should_RefreshToken_when_RefreshTokenRequested_with_ExpiredAccessAndValidRefreshToken() {
        final String username = "ozdemir";
        User user = new User(1L, username, List.of("USER:read"));
        Token token = new Token("accessToken", "refreshToken");

        when(userClient.findUserByUsername(anyString()))
                .thenReturn(Response.found(user));

        when(repository.findTokenByUsernameAndRefreshToken(any(), any()))
                .thenReturn(Optional.of(token));

        when(jwtService.isTokenExpired("accessToken"))
                .thenReturn(true);

        when(jwtService.isTokenExpired("refreshToken"))
                .thenReturn(false);

        when(jwtService.generateBearerTokenFor(any(), any()))
                .thenReturn("accessToken2");

        when(jwtService.generateRefreshTokenFor(any()))
                .thenReturn("refreshToken2");

        Token responseToken = tokenService.refreshOrGetTokenSet(username, "refreshToken");

        verify(repository).revokeToken(username, token);
        verify(repository).saveToken(username, "accessToken2", "refreshToken2");

        assertThat(responseToken.accessToken()).isEqualTo("accessToken2");
        assertThat(responseToken.refreshToken()).isEqualTo("refreshToken2");

    }

    @Test
    void should_ThrowTokenNotFound_when_RefreshTokenRequested_with_ExpiredAccessAndRefreshToken() {
        final String username = "ozdemir";
        User user = new User(1L, username, List.of("USER:read"));
        Token token = new Token("accessToken", "refreshToken");

        when(userClient.findUserByUsername(anyString()))
                .thenReturn(Response.found(user));

        when(repository.findTokenByUsernameAndRefreshToken(any(), any()))
                .thenReturn(Optional.of(token));

        when(jwtService.isTokenExpired("accessToken"))
                .thenReturn(true);

        when(jwtService.isTokenExpired("refreshToken"))
                .thenReturn(true);

        assertThatThrownBy(() -> tokenService.refreshOrGetTokenSet(username, "refreshToken"))
                .isInstanceOf(TokenNotFoundException.class);

        verify(repository).revokeToken(username, token);
        verify(repository, never()).saveToken(any(), any(), any());
    }
}