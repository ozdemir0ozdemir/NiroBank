package ozdemir0ozdemir.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserEntity {

    private Long id;
    private String username;
    private List<String> authorities;

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        else if (this == other) {
            return true;
        }

        if(other instanceof UserEntity otherUser){
            return this.id.equals(otherUser.id) &&
                    this.username.equals(otherUser.username) &&
                    this.authorities.containsAll(otherUser.authorities);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("user: { id: %d, username: %s, authorities: %s}",
                this.id, this.username, this.authorities.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, authorities);
    }
}
