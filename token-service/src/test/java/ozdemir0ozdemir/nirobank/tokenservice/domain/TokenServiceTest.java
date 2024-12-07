package ozdemir0ozdemir.nirobank.tokenservice.domain;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(accessTokenClaims.getExpiration()).isAfter(Instant.now());
    }

    @Test
    void should_RevokeOldToken_when_UserHasRefreshTokenAlready() {
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
}