package ozdemir0zdemir.authservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.client.tokenclient.TokenClient;
import ozdemir0ozdemir.nirobank.client.userclient.UserClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenClient tokenClient;
}
