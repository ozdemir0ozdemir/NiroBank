package ozdemir0ozdemir.nirobank.tokenservice.domain;

import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ozdemir0ozdemir.common.user.Role;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService sut;

    private final PageRequest mediumSizeFirstPage = PageRequest.of(0, 15);

    @Test
    void should_generateToken() throws Exception {
        //given
       String username = "test-username";
        when(repository
                .findAllByUsernameAndRefreshTokenStatus( username, RefreshTokenStatus.ACCEPTABLE, mediumSizeFirstPage))
                .thenReturn(Page.empty());

        when(jwtService.generateJwt(anyString(), anyList(), any(), anyBoolean()))
                .thenReturn("example-token");

        Date expiresAt = new Date(System.currentTimeMillis());
        Timestamp timestamp = Timestamp.from(expiresAt.toInstant());

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("exp", expiresAt);
        claims.put("jti", "tokenId");

        when(jwtService.getClaimsFrom(anyString()))
                .thenReturn(new DefaultClaims(claims));

        RefreshTokenEntity entity = new RefreshTokenEntity()
                .setId(1L)
                .setReferenceId("refId")
                .setRefreshToken("token")
                .setRefreshTokenStatus(RefreshTokenStatus.ACCEPTABLE)
                .setUsername("test-username")
                .setExpiresAt(timestamp);

        when(repository.save(any()))
                .thenReturn(entity);

        //when
        AccessToken accessToken = sut.generateTokenFor(username, Role.USER);

        //then
        assertThat(accessToken.refreshTokenReferenceId()).isEqualTo(entity.getReferenceId());
        assertThat(accessToken.username()).isEqualTo(username);
        assertThat(accessToken.accessToken()).isEqualTo("example-token");
//        assertThat(accessToken.expiresAt()).isEqualTo(timestamp);
    }
}