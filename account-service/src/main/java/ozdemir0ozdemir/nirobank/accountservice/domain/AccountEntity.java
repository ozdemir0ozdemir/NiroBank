package ozdemir0ozdemir.nirobank.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class AccountEntity {

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


}
