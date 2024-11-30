package ozdemir0ozdemir.nirobank.authserver.repository;

import org.springframework.stereotype.Repository;
import ozdemir0ozdemir.nirobank.authserver.model.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public final class TokenRepository {

    private final List<TokenCache> tokenList = new ArrayList<>();

    public Optional<Token> findTokenByUsername(String username) {

        Optional<TokenCache> optionalTokenCache = tokenList.stream()
                .filter(cache -> cache.username().equals(username))
                .findFirst();

        if(optionalTokenCache.isPresent()){
            TokenCache cache = optionalTokenCache.get();
            return Optional.of(new Token(cache.accessToken(), cache.refreshToken()));
        }

        return Optional.empty();
    }

    public Optional<Token> findTokenByUsernameAndRefreshToken(String username, String refreshToken) {

        Optional<TokenCache> optionalTokenCache = tokenList.stream()
                .filter(cache -> cache.username().equals(username))
                .filter(cache -> cache.refreshToken().equals(refreshToken))
                .findFirst();

        if(optionalTokenCache.isPresent()){
            TokenCache cache = optionalTokenCache.get();
            return Optional.of(new Token(cache.accessToken(), cache.refreshToken()));
        }

        return Optional.empty();
    }


    public void revokeToken(String username, Token token) {
        this.tokenList.remove(new TokenCache(username, token.accessToken(), token.refreshToken()));
    }

    public void saveToken(String username, String accessToken, String refreshToken) {
        this.tokenList.add(new TokenCache(username, accessToken, refreshToken));
    }
}
