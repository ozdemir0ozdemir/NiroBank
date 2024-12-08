package ozdemir0zdemir.authservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.client.tokenclient.TokenClient;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenClient tokenClient;

    public void register(String username, String password) {
        userClient.registerUser(new RegisterUser(username, password));
    }
}
