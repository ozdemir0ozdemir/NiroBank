package ozdemir0ozdemir.nirobank.tokenservice.dbtimetest;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class DateTest {

    private static JwtService jwtService;
    private static JwtConfiguration configuration;
    private static PrivateKey privateKey;


    private static final Clock clock = Clock.fixed(Instant.now(), Clock.systemUTC().getZone());

    @BeforeAll
    public static void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();
        privateKey = keyPair.getPrivate();

        configuration = Mockito.mock(JwtConfiguration.class);
        when(configuration.getPublicKeySpec())
                .thenReturn(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
        when(configuration.getPrivateKeySpec())
                .thenReturn(new PKCS8EncodedKeySpec(privateKey.getEncoded()));
        when(configuration.getAudience())
                .thenReturn("NiroBank-Api");
        when(configuration.getIssuer())
                .thenReturn("NiroBank-AuthService");
        when(configuration.getExpiresAtMillis())
                .thenReturn(30 * 60 * 1000L);

        when(configuration.getRefreshExpiresAtMillis())
                .thenReturn(30 * 24 * 60 * 60 * 1000L);


        jwtService = new JwtService(configuration, Clock.systemUTC());
    }

    @Test
    void test() {


        String token = Jwts
                .builder()
                .id("token_id")
                .issuer(configuration.getIssuer())
                .audience().add(configuration.getAudience()).and()
                .signWith(privateKey)
                .claim("iat", Date.from(clock.instant()))
                .claim("exp", Date.from(clock.instant().plusSeconds(120)))
                .compact();

        System.out.println(token);

        Claims claims = jwtService.getClaimsFrom(token);
        System.out.println(claims.getIssuedAt().toInstant());
        System.out.println(claims.getExpiration().toInstant());
    }

    @Test
    void clockTest() {


    }
}
