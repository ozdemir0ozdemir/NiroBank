package ozdemir0zdemir.authservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.user.Role;
import ozdemir0ozdemir.nirobank.client.tokenclient.AccessToken;
import ozdemir0ozdemir.nirobank.client.tokenclient.TokenClient;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.GenerateToken;
import ozdemir0ozdemir.nirobank.client.userclient.User;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;
import ozdemir0zdemir.authservice.exception.TokenClientException;
import ozdemir0zdemir.authservice.exception.UserClientException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenClient tokenClient;

    public Response<Void> register(String username, String password) {
        // TODO: Send request to the message broker instead of sending directly
        // TODO: Send activation code to users email (User model has no email field yet)

        RegisterUser request = new RegisterUser(username, password);
        ResponseEntity<Response<Void>> userClientResponse = userClient.saveNewUser(request);

        if (userClientResponse.getStatusCode().is2xxSuccessful()) {
            return userClientResponse.getBody();
        }
        throw new UserClientException("User registration failed");
    }


    public Response<AccessToken> login(String username, String password) {

        ResponseEntity<Response<User>> loginResponse = userClient.login(new Login(username, password));
        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            User user = loginResponse.getBody().getPayload();

            ResponseEntity<Response<AccessToken>> tokenClientResponse = tokenClient
                    .generateToken(new GenerateToken(user.username(), user.role()));

            if (tokenClientResponse.getStatusCode().is2xxSuccessful()) {
                return tokenClientResponse.getBody();
            }
            else {
                throw new TokenClientException("Token cannot be generated");
            }

        }
        throw new UserClientException("Login failed");
    }


    public Response<AccessToken> refresh(String refreshTokenId) {
        ResponseEntity<Response<AccessToken>> tokenClientResponse = tokenClient
                .refreshAccessToken(refreshTokenId);

        if (tokenClientResponse.getStatusCode().is2xxSuccessful()) {
            return tokenClientResponse.getBody();
        }
        throw new TokenClientException("Access token cannot be refreshed");
    }
}
