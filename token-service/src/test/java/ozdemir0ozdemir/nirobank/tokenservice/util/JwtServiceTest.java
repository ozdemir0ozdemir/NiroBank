package ozdemir0ozdemir.nirobank.tokenservice.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


class JwtServiceTest {

    private static JwtService jwtService;
    private static JwtConfiguration jwtConfiguration;

    private static final ZonedDateTime now = ZonedDateTime.of(
            2024,1, 1,
            10, 0, 0, 0,
            ZoneId.of("UTC")
    );
    private static final Clock clock = Clock.fixed(now.toInstant(), now.getZone());

    @BeforeAll
    public static void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        jwtConfiguration = Mockito.mock(JwtConfiguration.class);
        when(jwtConfiguration.getPublicKeySpec())
                .thenReturn(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
        when(jwtConfiguration.getPrivateKeySpec())
                .thenReturn(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded()));
        when(jwtConfiguration.getAudience())
                .thenReturn("NiroBank-Api");
        when(jwtConfiguration.getIssuer())
                .thenReturn("NiroBank-AuthService");
        when(jwtConfiguration.getExpiresAtMillis())
                .thenReturn(30 * 60 * 1000L);

        when(jwtConfiguration.getRefreshExpiresAtMillis())
                .thenReturn(30 * 24 * 60 * 60 * 1000L);

        jwtService = new JwtService(jwtConfiguration, clock);
    }


    @Test
    void shouldGenerateValidBearerTokenWithCorrectClaims() throws Exception {

        String token = jwtService
                .generateJwt("USER", List.of("USER", "ADMIN"), Instant.now(clock), false);

        assertThat(token).isNotNull();

        Claims claims = jwtService
                .getClaimsFrom(token);

        assertThat(claims.getAudience()).containsExactly("NiroBank-Api");
        assertThat(claims.getIssuer()).isEqualTo("NiroBank-AuthService");

        assertThat(claims.getIssuedAt()).isEqualTo(Instant.now(clock));
        assertThat(claims.getExpiration()).isEqualTo(Instant.now(clock).plusMillis(jwtConfiguration.getExpiresAtMillis()));
        assertThat(claims.getSubject()).isEqualTo("USER");

        Object rawAuthorities = claims.get("authorities");
        assertThat(rawAuthorities).isInstanceOf(List.class);

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) rawAuthorities;

        assertThat(authorities)
                .isNotNull()
                .hasSize(2)
                .contains("USER", "ADMIN");

        assertThat(claims.getId()).isNotNull().startsWith("USER:");
    }

    @Test
    void shouldThrowExpiredJwtException() throws Exception {

        String token = jwtService.generateJwt(
                "USER",
                List.of("USER", "ADMIN"),
                Instant.now(clock).minusMillis(jwtConfiguration.getExpiresAtMillis() + 1),
                false);

        assertThatThrownBy(() -> jwtService.getClaimsFrom(token))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void shouldThrowMalformedJwtException() throws Exception {
        assertThatThrownBy(() -> jwtService.getClaimsFrom("malformed.jwt.token"))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    void shouldThrowSignatureException() throws Exception {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        String token = Jwts
                .builder()
                .id(UUID.randomUUID().toString())
                .issuer("NiroBank-AuthService")
                .audience().add("NiroBank-Api").and()
                .issuedAt(Date.from(Instant.now(clock)))
                .expiration(Date.from(Instant.now(clock).plusMillis(jwtConfiguration.getExpiresAtMillis())))
                .subject("User")
                .claim("authorities", List.of("USER"))
                .signWith(keyPair.getPrivate())
                .compact();

        assertThatThrownBy(() -> jwtService.getClaimsFrom(token))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void shouldThrowIllegalArgumentException() throws Exception {
        assertThatThrownBy(() -> jwtService.getClaimsFrom(""))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> jwtService.getClaimsFrom("         "))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> jwtService.getClaimsFrom(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReturnRefreshScopeAuthorityClaim() throws Exception {

        String token = jwtService.generateRefreshJwtFor("USER", List.of("USER"));
        assertThat(token).isNotNull().isNotBlank();

        Claims claims = jwtService.getClaimsFrom(token);
        assertThat(claims).isNotNull();

        Object rawAuthorities = claims.get("authorities");
        assertThat(rawAuthorities).isInstanceOf(List.class);

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) rawAuthorities;
        assertThat(authorities).contains("USER");

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCheckingTokensExpireStatus() throws Exception {
        assertThatThrownBy(() -> jwtService.isJwtExpired(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReturnTrueWhenCheckingTokensExpireStatus() throws Exception {
        String token = jwtService
                .generateJwt(
                        "USER",
                        List.of("USER", "ADMIN"),
                        Instant.now(clock).minusMillis(jwtConfiguration.getExpiresAtMillis() + 1),
                        false);

        assertThat(jwtService.isJwtExpired(token))
                .isTrue();
    }
}