package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.stereotype.Repository;
import ozdemir0ozdemir.nirobank.tokenservice.bridge.Token;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
final class TokenRepository {

    private final Set<TokenCache> tokenCacheSet = new HashSet<>();

    Optional<Token> findTokenByUsername(String username) {

        Optional<TokenCache> optionalTokenCache = tokenCacheSet.stream()
                .filter(cache -> cache.username().equals(username))
                .findFirst();

        if(optionalTokenCache.isPresent()){
            TokenCache cache = optionalTokenCache.get();
            return Optional.of(new Token(cache.accessToken(), cache.refreshToken()));
        }

        return Optional.empty();
    }

    Optional<Token> findTokenByUsernameAndRefreshToken(String username, String refreshToken) {

        Optional<TokenCache> optionalTokenCache = tokenCacheSet.stream()
                .filter(cache -> cache.username().equals(username))
                .filter(cache -> cache.refreshToken().equals(refreshToken))
                .findFirst();

        if(optionalTokenCache.isPresent()){
            TokenCache cache = optionalTokenCache.get();
            return Optional.of(new Token(cache.accessToken(), cache.refreshToken()));
        }

        return Optional.empty();
    }


    void revokeToken(String username, Token token) {
        this.tokenCacheSet.remove(new TokenCache(username, token.accessToken(), token.refreshToken()));
    }

    void saveToken(String username, String accessToken, String refreshToken) {
        this.tokenCacheSet.add(new TokenCache(username, accessToken, refreshToken));
    }

    Set<TokenCache> getTokenCacheSet() {
        return Set.copyOf(this.tokenCacheSet);
    }
}
