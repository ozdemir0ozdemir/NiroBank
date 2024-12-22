package ozdemir0ozdemir.nirobank.accountservice.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        this("Account not found");
    }
    public AccountNotFoundException(String message) {
        super(message);
    }
}
