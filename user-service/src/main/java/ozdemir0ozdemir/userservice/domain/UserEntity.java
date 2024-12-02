package ozdemir0ozdemir.userservice.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
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
