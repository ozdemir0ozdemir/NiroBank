package ozdemir0ozdemir.nirobank.tokenservice.domain;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ozdemir0ozdemir.common.user.Role;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenExpiredException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenNotFoundException;
import ozdemir0ozdemir.nirobank.tokenservice.util.JwtService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final JwtService jwtService;
    private final PageRequest mediumSizeFirstPage = PageRequest.of(0, 15);

    public AccessToken generateTokenFor(String username, Role role) {

        // Revoke old acceptable refresh tokens
        Page<RefreshTokenEntity> page = repository
                .findAllByUsernameAndRefreshTokenStatus(username, RefreshTokenStatus.ACCEPTABLE, mediumSizeFirstPage);

        page.forEach(entity -> this.revokeRefreshTokenByReferenceId(entity.getReferenceId()));

        // Generate new jwt token pair
        Date now = new Date();

        String accessTokenString = jwtService.generateJwt(username, role.getPermissions(), now, false);
        String refreshTokenString = jwtService.generateJwt(username, role.getPermissions(), now, true);

        Claims accessTokenClaims = jwtService.getClaimsFrom(accessTokenString);
        Claims refreshTokenClaims = jwtService.getClaimsFrom(refreshTokenString);

        Timestamp expiresAt = Timestamp.from(refreshTokenClaims.getExpiration().toInstant());

        // Save refresh token and return access token
        RefreshTokenEntity entity = repository.save(new RefreshTokenEntity()
                .setReferenceId(refreshTokenClaims.getId())
                .setUsername(accessTokenClaims.getSubject())
                .setRefreshToken(refreshTokenString)
                .setExpiresAt(expiresAt));

        AccessToken result = new AccessToken(
                entity.getReferenceId(),
                accessTokenClaims.getSubject(),
                accessTokenString,
                expiresAt);

        assert entity.getId() != null;
        return result;
    }

    public AccessToken refreshTokenFor(String refreshTokenReferenceId) {

        Optional<RefreshTokenEntity> optionalEntity =
                repository.findByReferenceId(refreshTokenReferenceId);

        if (optionalEntity.isEmpty() || optionalEntity.get().getRefreshTokenStatus().equals(RefreshTokenStatus.REVOKED)) {
            throw new RefreshTokenNotFoundException("Refresh token not found. Ref ID: " + refreshTokenReferenceId);
        }

        Claims refreshTokenClaims = jwtService
                .getClaimsFrom(optionalEntity.get().getRefreshToken());

        Date now = new Date();

        if (refreshTokenClaims.getExpiration().before(now)) {
            throw new RefreshTokenExpiredException("Refresh token expired. Ref ID: " + refreshTokenReferenceId);
        }

        @SuppressWarnings("unchecked")
        String accessTokenString = jwtService.generateJwt(
                optionalEntity.get().getUsername(),
                (List<String>) refreshTokenClaims.get(JwtService.USER_AUTHORITIES),
                now,
                false);

        Claims accessTokenClaims = jwtService.getClaimsFrom(accessTokenString);


        // FIXME: BUG
        return new AccessToken(
                refreshTokenReferenceId,
                accessTokenClaims.getSubject(),
                accessTokenString,
                Timestamp.from(accessTokenClaims.getExpiration().toInstant()));
    }

    public RefreshToken findRefreshTokenByReferenceId(String refreshTokenReferenceId) {
        return repository.findByReferenceId(refreshTokenReferenceId)
                .map(this::from)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token cannot be revoked. Ref ID: " + refreshTokenReferenceId));
    }

    public Page<RefreshToken> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(this::from);
    }

    public Page<RefreshToken> findAllByTokenStatus(RefreshTokenStatus refreshTokenStatus, int page, int size) {
        return repository.findAllByRefreshTokenStatus(refreshTokenStatus, PageRequest.of(page, size))
                .map(this::from);
    }

    public Page<RefreshToken> findAllByUsername(String username, int page, int size) {
        return repository.findAllByUsername(username, PageRequest.of(page, size))
                .map(this::from);
    }

    public Page<RefreshToken> findAllByUsernameAndRefreshTokenStatus(String username,
                                                                     RefreshTokenStatus refreshTokenStatus,
                                                                     int page, int size) {
        return repository.findAllByUsernameAndRefreshTokenStatus(username, refreshTokenStatus, PageRequest.of(page, size))
                .map(this::from);
    }

    void revokeRefreshTokenByReferenceId(String refreshTokenReferenceId) {
        int affectedRow = repository
                .updateRefreshTokenStatusByReferenceId(refreshTokenReferenceId, RefreshTokenStatus.REVOKED);

        if (affectedRow == 0) {
            throw new RefreshTokenNotFoundException("Refresh token cannot be revoked. Ref ID: " + refreshTokenReferenceId);
        }
    }

    RefreshToken from(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getReferenceId(),
                entity.getUsername(),
                entity.getRefreshToken(),
                entity.getExpiresAt(),
                entity.getRefreshTokenStatus()
        );
    }
}
