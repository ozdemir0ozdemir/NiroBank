package ozdemir0zdemir.authservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.common.response.ResponseStatus;
import ozdemir0ozdemir.nirobank.client.tokenclient.AccessToken;
import ozdemir0ozdemir.nirobank.client.tokenclient.TokenClient;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.CreateToken;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.RefreshToken;
import ozdemir0ozdemir.nirobank.client.userclient.User;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;
import ozdemir0zdemir.authservice.exception.TokenException;
import ozdemir0zdemir.authservice.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenClient tokenClient;

    public AccessToken login(Login request) {
        Response<User> userResponse =
                this.userClient.login(request);

        if (userResponse.getStatus().equals(ResponseStatus.FAILED)) {
            throw new UserNotFoundException(userResponse.getMessage());
        }

        User user = userResponse.getPayload();
        Response<AccessToken> accessTokenResponse =
                this.tokenClient.createToken(new CreateToken(user.username(), user.role()));

        if (accessTokenResponse.getStatus().equals(ResponseStatus.FAILED)) {
            throw new TokenException(accessTokenResponse.getMessage());
        }

        return accessTokenResponse.getPayload();
    }

    public Response<Void> register(String username, String password) {
        // TODO: Send request to the message broker instead of sending directly
        // TODO: Send activation code to users email (User model has no email field yet)
        return this.userClient.registerUser(new RegisterUser(username, password));
    }

    public AccessToken refresh(String refreshTokenId) {

        // TODO: parse token, get username and roles to validate user
        // TODO: send validation request to the user-service

        Response<AccessToken> accessTokenResponse =
                this.tokenClient.refreshAccessToken(new RefreshToken(refreshTokenId));

        if (accessTokenResponse.getStatus().equals(ResponseStatus.FAILED)) {
            throw new TokenException(accessTokenResponse.getMessage());
        }

        return accessTokenResponse.getPayload();
    }
}
