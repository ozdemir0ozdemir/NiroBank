package ozdemir0ozdemir.nirobank.tokenservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

public record RefreshToken(Long id,
                           String referenceId,
                           String username,
                           String refreshToken,
                           Timestamp expiresAt,
                           RefreshTokenStatus refreshTokenStatus) {
}
