package ozdemir0ozdemir.nirobank.tokenservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenJpaRepository repository;
    private final JwtService jwtService;
}
