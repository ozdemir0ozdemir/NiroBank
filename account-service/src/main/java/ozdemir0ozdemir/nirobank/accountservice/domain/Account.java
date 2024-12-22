package ozdemir0ozdemir.nirobank.accountservice.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record Account(Long id,
               String accountNumber,
               String accountHolderName,
               BigDecimal balance,
               Instant createdAt,
               Instant updatedAt) {


    static Account from(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getAccountHolderName(),
                entity.getBalance(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

}
