package ozdemir0ozdemir.nirobank.accountservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ozdemir0ozdemir.nirobank.accountservice.exception.AccountException;
import ozdemir0ozdemir.nirobank.accountservice.exception.AccountNotFoundException;
import ozdemir0ozdemir.nirobank.accountservice.exception.UserAlreadyHasAnAccountException;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class Accounts {

    private final AccountRepository repository;
    private final Clock clock;

    /**
     * @param accountHolderName is the users name own the account
     * @return the account id
     */
    public Long createAccount(String accountHolderName) {

        // if the user has an account don't create new one
        boolean isPresent = repository.findByAccountHolderName(accountHolderName)
                .isPresent();

        if (isPresent) {
            throw new UserAlreadyHasAnAccountException();
        }

        AccountEntity accountEntity = AccountEntity.create(accountHolderName, clock);
        return repository.save(accountEntity).getId();
    }

    public Account findAccountByAccountId(Long accountId) {
        return repository
                .findById(accountId)
                .map(Account::from)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Account findAccountByAccountHolderName(String accountHolderName) {
        return repository
                .findByAccountHolderName(accountHolderName)
                .map(Account::from)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Account findAccountByAccountNumber(String accountNumber) {
        return repository
                .findByAccountNumber(accountNumber)
                .map(Account::from)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Page<Account> findAllAccounts(int page, int size) {
        return repository
                .findAllAccounts(PageRequest.of(page, size))
                .map(Account::from);
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void setAccountBalanceByAccountId(Long accountId, BigDecimal newBalance) {
        int result = repository
                .setAccountBalanceByAccountId(accountId, newBalance);

        checkModifyingProcessResult(result, "Couldn't set the accounts balance");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void setAccountBalanceByAccountHolderName(String accountHolderName, BigDecimal newBalance) {
        int result = repository
                .setAccountBalanceByAccountHolderName(accountHolderName, newBalance);

        checkModifyingProcessResult(result, "Couldn't set the accounts balance");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void setAccountBalanceByAccountNumber(String accountNumber, BigDecimal newBalance) {
        int result = repository
                .setAccountBalanceByAccountNumber(accountNumber, newBalance);

        checkModifyingProcessResult(result, "Couldn't set the accounts balance");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void addBalanceToAccountByAccountId(Long accountId, BigDecimal newBalance) {
        int result = repository
                .addBalanceToAccountByAccountId(accountId, newBalance);

        checkModifyingProcessResult(result, "Couldn't add balance to the account");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void addBalanceToAccountByAccountHolderName(String accountHolderName, BigDecimal newBalance) {
        int result = repository
                .addBalanceToAccountByAccountHolderName(accountHolderName, newBalance);

        checkModifyingProcessResult(result, "Couldn't add balance to the account");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void addBalanceToAccountByAccountNumber(String accountNumber, BigDecimal newBalance) {
        int result = repository
                .addBalanceToAccountByAccountNumber(accountNumber, newBalance);

        checkModifyingProcessResult(result, "Couldn't add balance to the account");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void deleteAccountByAccountId(Long accountId) {
        int result = repository
                .deleteAccountByAccountId(accountId);

        checkModifyingProcessResult(result, "Couldn't delete the account");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void deleteAccountByAccountHolderName(String accountHolderName) {
        int result = repository
                .deleteAccountByAccountHolderName(accountHolderName);

        checkModifyingProcessResult(result, "Couldn't delete the account");
    }

    /**
     * @throws AccountException when nothing is changed
     */
    public void deleteAccountByAccountNumber(String accountNumber) {
        int result = repository
                .deleteAccountByAccountNumber(accountNumber);

        checkModifyingProcessResult(result, "Couldn't delete the account");
    }

    private void checkModifyingProcessResult(int result, String exceptionMessage) {
        if (result == 0) {
            throw new AccountException(exceptionMessage);
        }
    }

}
