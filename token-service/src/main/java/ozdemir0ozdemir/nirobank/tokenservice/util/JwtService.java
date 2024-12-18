package ozdemir0ozdemir.nirobank.tokenservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.tokenservice.config.JwtConfiguration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

@Service
public final class JwtService {

    public static final String USER_AUTHORITIES = "authorities";
    private final JwtConfiguration configuration;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;


    JwtService(@NonNull JwtConfiguration configuration) throws
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

    public boolean isJwtExpired(@NonNull final String token) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return false;
        }
        catch (ExpiredJwtException _) {
            return true;
        }
    }

    public String generateJwtFor(@NonNull final String username,
                                 @NonNull final List<String> authorities) {

        return generateJwt(username, authorities, new Date(), false);
    }

    public String generateRefreshJwtFor(@NonNull final String username,
                                        @NonNull final List<String> authorities) {
        return generateJwt(username, authorities, new Date(), true);
    }

    public String generateJwt(@NonNull final String username,
                       @NonNull final List<String> authorities,
                       @NonNull final Date issuedAt,
                       boolean isRefreshToken) {

        long issuedAtTime  = issuedAt.getTime();
        long expiresAmount = isRefreshToken ? configuration.getRefreshExpiresAtMillis() : configuration.getExpiresAtMillis();


        return Jwts
                .builder()
                .setId(username + ":" + UUID.randomUUID())
                .setIssuer(this.configuration.getIssuer())
                .setAudience(this.configuration.getAudience())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAtTime + expiresAmount))
                .setSubject(username)
                .addClaims(Map.of(USER_AUTHORITIES, authorities))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
