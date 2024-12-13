package ozdemir0ozdemir.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ozdemir0ozdemir.common.util.JwtConfig;
import ozdemir0ozdemir.common.util.JwtUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@SpringBootTest
class JwtParserTest {

    private JwtConfig config;
    private JwtUtil jwt;

    JwtParserTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        config = new JwtConfig();
        jwt = new JwtUtil(config);
    }

    @Test
    void should_loadPublicPem() {
        X509EncodedKeySpec key = this.config.getPublicKeySpec();
        System.out.println(key);
    }



}
