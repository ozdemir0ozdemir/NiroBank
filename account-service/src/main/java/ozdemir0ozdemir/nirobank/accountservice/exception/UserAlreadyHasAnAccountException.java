package ozdemir0ozdemir.nirobank.accountservice.exception;

public class UserAlreadyHasAnAccountException extends RuntimeException {

    public UserAlreadyHasAnAccountException() {
        this("User already has an Account");
    }

    public UserAlreadyHasAnAccountException(String message) {
        super(message);
    }
}
