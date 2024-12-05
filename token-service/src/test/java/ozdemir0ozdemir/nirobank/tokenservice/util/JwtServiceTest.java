package ozdemir0ozdemir.nirobank.tokenservice.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private static JwtService jwtService;

    @BeforeAll
    public static void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        JwtConfiguration configuration = Mockito.mock(JwtConfiguration.class);
        when(configuration.getPublicKeySpec())
                .thenReturn(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
        when(configuration.getPrivateKeySpec())
                .thenReturn(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded()));
        when(configuration.getAudience())
                .thenReturn("NiroBank-Api");
        when(configuration.getIssuer())
                .thenReturn("NiroBank-AuthService");
        when(configuration.getExpiredAtAmount())
                .thenReturn(30L);
        when(configuration.getExpiredAtUnit())
                .thenReturn(ChronoUnit.MINUTES);
        when(configuration.getRefreshExpiredAtAmount())
                .thenReturn(30L);
        when(configuration.getRefreshExpiredAtUnit())
                .thenReturn(ChronoUnit.DAYS);

        jwtService = new JwtService(configuration);
    }


    @Test
    void shouldGenerateValidBearerTokenWithCorrectClaims() throws Exception {

        String token = jwtService
                .generateJwtFor("USER", List.of("USER", "ADMIN"));

        assertThat(token).isNotNull();

        Claims claims = jwtService
                .getClaimsFrom(token);

        assertThat(claims.getAudience()).isEqualTo("NiroBank-Api");
        assertThat(claims.getIssuer()).isEqualTo("NiroBank-AuthService");

        assertThat(claims.getIssuedAt()).isBefore(Instant.now());
        assertThat(claims.getExpiration())
                .isAfter(Instant.now())
                .isAfter(claims.getIssuedAt());

        assertThat(claims.getSubject()).isEqualTo("USER");

        Object rawAuthorities = claims.get("authorities");
        assertThat(rawAuthorities).isInstanceOf(List.class);

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) rawAuthorities;

        assertThat(authorities)
                .isNotNull()
                .hasSize(2)
                .contains("USER", "ADMIN");

        assertThat(claims.getId()).isNotNull();

    }

    @Test
    void shouldThrowExpiredJwtException() throws Exception {

        String token = jwtService.generateJwt(
                "USER",
                List.of("USER", "ADMIN"),
                Instant.now().minus(35L, ChronoUnit.MINUTES),
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

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(30L, ChronoUnit.MINUTES);

        String token = Jwts
                .builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("NiroBank-AuthService")
                .setAudience("NiroBank-Api")
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setSubject("User")
                .addClaims(Map.of("authorities", List.of("USER")))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
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

        String token = jwtService.generateRefreshJwtFor("USER");
        assertThat(token).isNotNull().isNotBlank();

        Claims claims = jwtService.getClaimsFrom(token);
        assertThat(claims).isNotNull();

        Object rawAuthorities = claims.get("authorities");
        assertThat(rawAuthorities).isInstanceOf(List.class);

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) rawAuthorities;
        assertThat(authorities).contains("SCOPE_token:refresh");

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
                        Instant.now().minus(31L, ChronoUnit.MINUTES),
                        false);

        assertThat(jwtService.isJwtExpired(token))
                .isTrue();
    }
}