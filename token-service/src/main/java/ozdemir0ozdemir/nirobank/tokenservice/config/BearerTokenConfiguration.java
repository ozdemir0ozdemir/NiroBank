package ozdemir0ozdemir.nirobank.tokenservice.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@ConfigurationProperties(prefix = "bearer.token")
@Component
@NoArgsConstructor
@Getter
@Setter
public final class BearerTokenConfiguration {

    private PKCS8EncodedKeySpec privateKeySpec;
    private X509EncodedKeySpec publicKeySpec;
    private ChronoUnit expiredAtUnit;
    private Long expiredAtAmount;
    private ChronoUnit refreshExpiredAtUnit;
    private Long refreshExpiredAtAmount;
    private String issuer;
    private String audience;

    void setPublicKey(@NonNull Resource resource) {
        try {
            String publicKeyString = resource.getContentAsString(Charset.defaultCharset())
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");

            this.publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        }
        catch (IOException ex) {
            throw new RuntimeException("Public Key cannot be read");
        }
    }

    void setPrivateKey(@NonNull Resource resource) {
        try {
            String privateKeyString = resource.getContentAsString(Charset.defaultCharset())
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");

            this.privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
