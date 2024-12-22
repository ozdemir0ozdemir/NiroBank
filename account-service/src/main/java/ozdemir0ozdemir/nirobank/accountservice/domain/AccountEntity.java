package ozdemir0ozdemir.nirobank.accountservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
final class AccountEntity {

    @Id
    @SequenceGenerator(name = "account_id_gen", sequenceName = "account_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "account_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    static AccountEntity create(String username, Clock clock) {
        return new AccountEntity()
                .setAccountNumber(username+":"+UUID.randomUUID())
                .setAccountHolderName(username)
                .setBalance(BigDecimal.ZERO)
                .setCreatedAt(Instant.now(clock))
                .setUpdatedAt(Instant.now(clock));
    }

    static AccountEntity from(Account account) {
        return new AccountEntity()
                .setId(account.id())
                .setAccountNumber(account.accountNumber())
                .setAccountHolderName(account.accountHolderName())
                .setBalance(account.balance())
                .setCreatedAt(account.createdAt())
                .setUpdatedAt(account.updatedAt());
    }

}
