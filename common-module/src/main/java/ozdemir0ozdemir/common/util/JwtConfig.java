package ozdemir0ozdemir.common.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ConfigurationProperties(prefix = "common.jwt")
@Component
public final class JwtConfig {

    private X509EncodedKeySpec publicKeySpec;

    public JwtConfig() {
    }

    public X509EncodedKeySpec getPublicKeySpec() {
        return publicKeySpec;
    }

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

}
