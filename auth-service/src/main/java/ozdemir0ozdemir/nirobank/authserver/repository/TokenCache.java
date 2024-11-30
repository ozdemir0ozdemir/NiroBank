package ozdemir0ozdemir.nirobank.authserver.repository;

public record TokenCache(String username, String accessToken, String refreshToken) {
}
