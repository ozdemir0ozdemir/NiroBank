package ozdemir0ozdemir.nirobank.accountservice.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
interface AccountRepository extends PagingAndSortingRepository<AccountEntity, Long>,
        CrudRepository<AccountEntity, Long> {

    @Transactional(readOnly = true)
    @Query("from AccountEntity ae where ae.accountHolderName = :accountHolderName")
    Optional<AccountEntity> findByAccountHolderName(String accountHolderName);

    @Transactional(readOnly = true)
    @Query("from AccountEntity ae where ae.accountNumber = :accountNumber")
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    @Transactional(readOnly = true)
    @Query("from AccountEntity")
    List<AccountEntity> findAllAccounts();


    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = :newBalance where ae.id = :accountId")
    int setAccountBalanceByAccountId(Long accountId, BigDecimal newBalance);

    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = :newBalance where ae.accountHolderName = :accountHolderName")
    int setAccountBalanceByAccountHolderName(String accountHolderName, BigDecimal newBalance);

    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = :newBalance where ae.accountNumber = :accountNumber")
    int setAccountBalanceByAccountNumber(String accountNumber, BigDecimal newBalance);


    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = ae.balance + :newBalance where ae.id = :accountId")
    int addBalanceToAccountByAccountId(Long accountId, BigDecimal newBalance);

    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = ae.balance + :newBalance where ae.accountHolderName = :accountHolderName")
    int addBalanceToAccountByAccountHolderName(String accountHolderName, BigDecimal newBalance);

    @Modifying(clearAutomatically = true)
    @Query("update AccountEntity ae set ae.balance = ae.balance + :newBalance where ae.accountNumber = :accountNumber")
    int addBalanceToAccountByAccountNumber(String accountNumber, BigDecimal newBalance);


    @Modifying
    @Query("delete from AccountEntity ae where ae.id = :accountId")
    int deleteAccountByAccountId(Long accountId);

    @Modifying
    @Query("delete from AccountEntity ae where ae.accountHolderName = :accountHolderName")
    int deleteAccountByAccountHolderName(String accountHolderName);

    @Modifying
    @Query("delete from AccountEntity ae where ae.accountNumber = :accountNumber")
    int deleteAccountByAccountNumber(String accountNumber);

}
