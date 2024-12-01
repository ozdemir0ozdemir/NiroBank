package ozdemir0ozdemir.nirobank.authserver.exception;

public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(String message) {
        super(message);
    }
}
