package ozdemir0ozdemir.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public final class JwtUtil {

    public static final String USER_AUTHORITIES = "authorities";
    private final PublicKey publicKey;


    public JwtUtil(@NonNull JwtConfig configuration) throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec publicSpec = configuration.getPublicKeySpec();
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



}
