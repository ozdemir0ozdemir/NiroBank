package ozdemir0ozdemir.nirobank.tokenservice.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenNotFoundException;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest({JwtConfiguration.class, JwtService.class, TokenService.class})
class TokenServiceTest {

    @MockBean
    private TokenJpaRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;


    @Test
    void should_CreateTokens_when_UserHasNoRefreshToken() {

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());


        AccessToken accessToken = this.tokenService
                .createTokens("spring", List.of("AUTHORITY_1", "AUTHORITY_2"));

        verify(repository, times(1)).save(any());
        verify(repository, times(1)).findByUsername(anyString());

        assertThat(accessToken.accessToken()).isNotBlank();
        assertThat(accessToken.refreshTokenId()).isNotBlank();
        assertThat(accessToken.username()).isEqualTo("spring");
        assertThat(accessToken.tokenStatus()).isEqualTo(TokenStatus.ACCEPTABLE);
        assertThat(accessToken.expiresAt()).isAfter(new Date());

        Claims accessTokenClaims = this.jwtService.getClaimsFrom(accessToken.accessToken());

        assertThat(accessTokenClaims.get(JwtService.USER_AUTHORITIES, List.class))
                .contains("AUTHORITY_1", "AUTHORITY_2");
        assertThat(accessTokenClaims.getExpiration()).isAfter(new Date());
    }

    @Test
    void should_RevokeOldToken_when_CreateToken_and_UserHasRefreshTokenAlready() {
        // Given
        String token = this.jwtService.generateRefreshJwtFor("spring", List.of("USER"));
        String tokenId = this.jwtService.getClaimsFrom(token).getId();

        TokenEntity oldToken = new TokenEntity()
                .setId(1L)
                .setToken(token)
                .setTokenId(tokenId)
                .setTokenStatus(TokenStatus.ACCEPTABLE)
                .setUsername("spring")
                .setExpiresAt(new Date(System.currentTimeMillis() + (10L * 60 * 1000L)));

        when(repository.findByUsername("spring"))
                .thenReturn(Optional.of(oldToken));

        // When
        this.tokenService.createTokens("spring", List.of("USER"));

        verify(repository, times(1)).revokeTokenByTokenId(anyString());
    }

    @Test
    void should_RefreshToken_when_UserHasRefreshToken() {
        // Given
        String token = this.jwtService.generateRefreshJwtFor("spring", List.of("USER"));
        String tokenId = this.jwtService.getClaimsFrom(token).getId();

        TokenEntity oldToken = new TokenEntity()
                .setId(1L)
                .setToken(token)
                .setTokenId(tokenId)
                .setTokenStatus(TokenStatus.ACCEPTABLE)
                .setUsername("spring")
                .setExpiresAt(new Date(System.currentTimeMillis() + (10L * 60 * 1000L)));

        when(repository.findByTokenId(anyString()))
                .thenReturn(Optional.of(oldToken));

        // When
        AccessToken accessToken = this.tokenService.refreshToken(oldToken.getTokenId());

        assertThat(accessToken).isNotNull();
        assertThat(accessToken.refreshTokenId()).isEqualTo(oldToken.getTokenId());
    }

    @Test
    void shouldNot_RefreshToken_when_UserHasNoRefreshToken() {
        // Given
        String token = this.jwtService.generateRefreshJwtFor("spring", List.of("USER"));
        String tokenId = this.jwtService.getClaimsFrom(token).getId();


        when(repository.findByTokenId(anyString()))
                .thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> this.tokenService.refreshToken(tokenId))
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    void shouldThrowJwtExpired_RefreshToken_when_UserHasExpiredRefreshToken() {
        // Given
        String token = this.jwtService.generateJwt(
                "spring",
                List.of("USER"),
                new Date(System.currentTimeMillis() - 1729000000),
                true);


        TokenEntity oldToken = new TokenEntity()
                .setId(1L)
                .setToken(token)
                .setTokenId("tokenId")
                .setTokenStatus(TokenStatus.ACCEPTABLE)
                .setUsername("spring")
                .setExpiresAt(new Date(System.currentTimeMillis() - 1729000000));

        when(repository.findByTokenId(anyString()))
                .thenReturn(Optional.of(oldToken));

        // When
        assertThatThrownBy(() -> this.tokenService.refreshToken(anyString()))
                .isInstanceOf(ExpiredJwtException.class);
    }

}