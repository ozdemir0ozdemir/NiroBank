package ozdemir0ozdemir.nirobank.authserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.authserver.configuration.BearerTokenConfiguration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public final class BearerTokenService {

    private final BearerTokenConfiguration configuration;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public BearerTokenService(@NonNull BearerTokenConfiguration configuration) throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {

        this.configuration = configuration;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateSpec = configuration.getPrivateKeySpec();
        KeySpec publicSpec = configuration.getPublicKeySpec();
        privateKey = keyFactory.generatePrivate(privateSpec);
        publicKey = keyFactory.generatePublic(publicSpec);
    }

    public Claims getClaimsFrom(@NonNull final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateBearerTokenFor(@NonNull final String username,
                                         @NonNull final List<String> authorities) {

        return generateBearerToken(username, authorities, Instant.now(), false);
    }

    public String generateRefreshTokenFor(@NonNull final String username) {
        return generateBearerToken(username, List.of("SCOPE_token:refresh"), Instant.now(), true);
    }


    String generateBearerToken(@NonNull final String username,
                               @NonNull final List<String> authorities,
                               @NonNull final Instant issuedAt,
                               boolean isRefreshToken) {

        Instant expiredAt = issuedAt.plus(
                isRefreshToken ? configuration.getRefreshExpiredAtAmount() : configuration.getExpiredAtAmount(),
                isRefreshToken ? configuration.getRefreshExpiredAtUnit() : configuration.getExpiredAtUnit());

        return Jwts
                .builder()
                .setId(username + ":" + UUID.randomUUID())
                .setIssuer(this.configuration.getIssuer())
                .setAudience(this.configuration.getAudience())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setSubject(username)
                .addClaims(Map.of("authorities", authorities))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
