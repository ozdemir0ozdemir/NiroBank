package ozdemir0ozdemir.nirobank.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.util.Date;

public class Jwt {

    private final PublicKey publicKey;
    private final Clock clock;
    public static final String AUTHORITIES_CLAIM = "authorities";

    public Jwt(X509EncodedKeySpec publicKeySpec, Clock clock) {
        this.clock = clock;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(publicKeySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

    public Claims parse(String token) {
        return Jwts.parser()
                .clock(() -> Date.from(clock.instant()))
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parser()
                    .clock(() -> Date.from(clock.instant()))
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return false;
        }
        catch (ExpiredJwtException ex) {
            return true;
        }
    }
}
