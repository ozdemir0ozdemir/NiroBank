package ozdemir0ozdemir.nirobank.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApplication {

    /* TODO:
    *   1 - Store refresh token id in redis cache (or in-memory storage username -> tokenid map)
    *   2 - Access token generation using basic authentication (return access and refresh token)
    *   3 - Refresh token revoke endpoint
    *   4 - Refresh token auto-revoke scheduler
    *   5 - Access token refresher endpoint (expired access token + refresh token needed)
    *       5a - Return same access token while access token is valid
    *       5b - Return new access and refresh token, revoke old refresh token
    *   6 - Verify access token endpoint (May not be needed since i use asymmetric key)
    *   7 - Create the user-service
    *
    * */

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
