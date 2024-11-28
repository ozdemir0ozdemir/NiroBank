package ozdemir0ozdemir.nirobank.authserver.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
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
public class BearerTokenService {

    private final BearerTokenConfiguration configuration;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public BearerTokenService(BearerTokenConfiguration configuration) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.configuration = configuration;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateSpec = configuration.getPrivateKeySpec();
        KeySpec publicSpec = configuration.getPublicKeySpec();
        privateKey = keyFactory.generatePrivate(privateSpec);
        publicKey = keyFactory.generatePublic(publicSpec);
    }

    public String generateToken() {

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(configuration.getExpiredAtAmount(), configuration.getExpiredAtUnit());

        String token = Jwts
                .builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("NiroBank-AuthS")
                .setAudience("NiroBank-Api")
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setSubject("USER")
                .addClaims(Map.of("authorities", List.of("SCOPE_user:read", "SCOPE_user:write")))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        String issuer = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody().toString();

        System.out.println("Verified with issuer: " + issuer);

        return token;
    }


}
