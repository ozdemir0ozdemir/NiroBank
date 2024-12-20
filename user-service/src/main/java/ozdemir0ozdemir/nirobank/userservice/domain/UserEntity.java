package ozdemir0ozdemir.nirobank.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users")
final class UserEntity {

    @Id
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true, updatable = false, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        else if (this == other) {
            return true;
        }

        if (other instanceof UserEntity otherUser) {
            return this.id.equals(otherUser.id) &&
                    this.username.equals(otherUser.username) &&
                    this.role.equals(otherUser.role);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("user: { id: %d, username: %s, role: %s}",
                this.id, this.username, this.role.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role);
    }
}
