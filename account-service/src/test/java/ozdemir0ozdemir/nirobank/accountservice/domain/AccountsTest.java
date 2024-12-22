package ozdemir0ozdemir.nirobank.accountservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ozdemir0ozdemir.nirobank.accountservice.PostgresContainer;
import ozdemir0ozdemir.nirobank.accountservice.exception.AccountException;
import ozdemir0ozdemir.nirobank.accountservice.exception.AccountNotFoundException;
import ozdemir0ozdemir.nirobank.accountservice.exception.UserAlreadyHasAnAccountException;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Sql("/test-data/account-test-data.sql")
@Transactional
class AccountsTest implements PostgresContainer {

    @Autowired
    private Accounts accounts;

    @Autowired
    private AccountRepository repository;

    private ZonedDateTime now = ZonedDateTime.of(
            2024, 1, 1,
            12, 0, 0, 0,
            ZoneId.of("UTC"));

    @MockBean
    private Clock clock;

    @BeforeEach
    void beforeEach() {
        when(clock.instant())
                .thenReturn(now.toInstant());

        when(clock.getZone())
                .thenReturn(now.getZone());
    }

    @Test
    void should_createAnAccount_and_findSavedAccount() {
        String username = "newUsername";

        Long accountId = accounts.createAccount(username);
        assertThat(accountId).isNotNegative().isNotNegative();

        Account account = accounts.findAccountByAccountId(accountId);
        assertThat(account).isNotNull();
        assertThat(account.accountHolderName()).isEqualTo(username);
        assertThat(account.id()).isEqualTo(accountId);
        assertThat(account.createdAt()).isEqualTo(now.toInstant());
        assertThat(account.updatedAt()).isEqualTo(now.toInstant());
        assertThat(account.balance().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    void should_not_createAnAccount() {
        assertThatThrownBy(() -> accounts.createAccount("JohnDoe"))
                .isInstanceOf(UserAlreadyHasAnAccountException.class);
    }

    @Test
    void should_findAnAccountByAccountHolderName() {
        Account account = accounts
                .findAccountByAccountHolderName("JohnDoe");

        assertThat(account).isNotNull();
        assertThat(account.accountNumber()).isEqualTo("JohnDoe:0b74d8f9-b67a-4e36-b5ad-58ab58c5cf54");
        assertThat(account.accountHolderName()).isEqualTo("JohnDoe");
        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50))).isEqualTo(0);
        assertThat(account.createdAt()).isEqualTo(Instant.parse("2024-01-01T10:00:00Z"));
    }

    @Test
    void should_not_findAnAccountByAccountHolderName_throwing_AccountNotFoundException() {
        assertThatThrownBy(() -> accounts.findAccountByAccountNumber("not-registered-account"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_findFiveAccount_inDb() throws Exception {
        List<Account> accountList = accounts.findAllAccounts();

        assertThat(accountList.size()).isEqualTo(5);
    }

    @Test
    void should_setAccountBalanceByAccountHolderName_to_921() throws Exception {

        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.setAccountBalanceByAccountHolderName("JohnDoe", BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(921)))
                .isEqualTo(0);
    }

    @Test
    void should_setAccountBalanceByAccountId_to_921() throws Exception {

        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.setAccountBalanceByAccountId(account.id(), BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(921)))
                .isEqualTo(0);

    }

    @Test
    void should_setAccountBalanceByAccountNumber_to_921() throws Exception {

        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.setAccountBalanceByAccountNumber(account.accountNumber(), BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(921)))
                .isEqualTo(0);

    }

    @Test
    void should_addBalanceToAccountByAccountId_adding_921_total_1921_50() throws Exception {
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.addBalanceToAccountByAccountId(account.id(), BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(1921.50)))
                .isEqualTo(0);
    }

    @Test
    void should_addBalanceToAccountByAccountHolderName_adding_921_total_1921_50() throws Exception {
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.addBalanceToAccountByAccountHolderName(account.accountHolderName(), BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(1921.50)))
                .isEqualTo(0);
    }

    @Test
    void should_addBalanceToAccountByAccountNumber_adding_921_total_1921_50() throws Exception {
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(account.balance().compareTo(BigDecimal.valueOf(1000.50)))
                .isEqualTo(0);

        accounts.addBalanceToAccountByAccountNumber(account.accountNumber(), BigDecimal.valueOf(921));

        Account updatedAccount = accounts.findAccountByAccountHolderName("JohnDoe");

        assertThat(updatedAccount.balance().compareTo(BigDecimal.valueOf(1921.50)))
                .isEqualTo(0);
    }

    @Test
    void should_deleteAccountByAccountId() throws Exception {
        // given
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");
        assertThat(account).isNotNull();

        // when
        accounts.deleteAccountByAccountId(account.id());

        // then
        assertThatThrownBy(() -> accounts.findAccountByAccountHolderName("JohnDoe"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_deleteAccountByAccountHolderName() throws Exception {
        // given
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");
        assertThat(account).isNotNull();

        // when
        accounts.deleteAccountByAccountHolderName(account.accountHolderName());

        // then
        assertThatThrownBy(() -> accounts.findAccountByAccountHolderName("JohnDoe"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_deleteAccountByAccountNumber() throws Exception {
        // given
        Account account = accounts.findAccountByAccountHolderName("JohnDoe");
        assertThat(account).isNotNull();

        // when
        accounts.deleteAccountByAccountNumber(account.accountNumber());

        // then
        assertThatThrownBy(() -> accounts.findAccountByAccountHolderName("JohnDoe"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_not_deleteAccountByAccountNumber() throws Exception {
        assertThatThrownBy(() -> accounts.deleteAccountByAccountHolderName("non-existent-account"))
                .isInstanceOf(AccountException.class);
    }


}