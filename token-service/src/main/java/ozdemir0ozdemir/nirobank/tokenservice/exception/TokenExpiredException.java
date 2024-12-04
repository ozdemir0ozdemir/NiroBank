package ozdemir0ozdemir.nirobank.tokenservice.exception;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(String message) {
        super(message);
    }
}
