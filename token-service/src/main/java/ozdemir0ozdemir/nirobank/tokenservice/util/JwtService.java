package ozdemir0ozdemir.nirobank.tokenservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public final class JwtService {

    public static final String USER_AUTHORITIES = "authorities";
    private final JwtConfiguration configuration;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Clock clock;

    public JwtService(@NonNull JwtConfiguration configuration, Clock clock) throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {

        this.configuration = configuration;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateSpec = configuration.getPrivateKeySpec();
        KeySpec publicSpec = configuration.getPublicKeySpec();
        privateKey = keyFactory.generatePrivate(privateSpec);
        publicKey = keyFactory.generatePublic(publicSpec);
        this.clock = clock;
    }

    public Claims getClaimsFrom(@NonNull final String token) {

        return Jwts
                .parser()
                .clock(() -> Date.from(clock.instant()))
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isJwtExpired(@NonNull final String token) {
        try {
            Jwts
                    .parser()
                    .clock(() -> Date.from(clock.instant()))
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return false;
        }
        catch (ExpiredJwtException _) {
            return true;
        }
    }

    public String generateJwtFor(@NonNull final String username,
                                 @NonNull final List<String> authorities) {

        return generateJwt(username, authorities, Instant.now(clock), false);
    }

    public String generateRefreshJwtFor(@NonNull final String username,
                                        @NonNull final List<String> authorities) {
        return generateJwt(username, authorities, Instant.now(clock), true);
    }

    public String generateJwt(@NonNull final String username,
                       @NonNull final List<String> authorities,
                       @NonNull final Instant issuedAt,
                       boolean isRefreshToken) {


        long expiresAmount = isRefreshToken ? configuration.getRefreshExpiresAtMillis() : configuration.getExpiresAtMillis();


        return Jwts
                .builder()
                .id(username + ":" + UUID.randomUUID())
                .issuer(this.configuration.getIssuer())
                .audience().add(this.configuration.getAudience()).and()
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plusMillis(expiresAmount)))
                .subject(username)
                .claim(USER_AUTHORITIES, authorities)
                .signWith(privateKey)
                .compact();
    }
}
