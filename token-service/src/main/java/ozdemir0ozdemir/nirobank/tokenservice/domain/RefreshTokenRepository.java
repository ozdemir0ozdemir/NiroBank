package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.Optional;

interface RefreshTokenRepository extends PagingAndSortingRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity save(RefreshTokenEntity te);

    Optional<RefreshTokenEntity> findById(Long id);

    Optional<RefreshTokenEntity> findByReferenceId(String referenceId);

    Page<RefreshTokenEntity> findAllByUsername(String username, PageRequest of);

    Page<RefreshTokenEntity> findAllByRefreshTokenStatus(RefreshTokenStatus refreshTokenStatus, PageRequest of);

    Page<RefreshTokenEntity> findAllByUsernameAndRefreshTokenStatus(String username, RefreshTokenStatus refreshTokenStatus, PageRequest of);

    Page<RefreshTokenEntity> findAllByExpiresAtAfter(Timestamp date, PageRequest of);

    Page<RefreshTokenEntity> findAllByExpiresAtBefore(Timestamp date, PageRequest of);

    @Modifying(clearAutomatically = true)
    @Query("update RefreshTokenEntity r set r.refreshTokenStatus = :refreshTokenStatus where r.id = :id")
    int updateRefreshTokenStatusById(Long id, RefreshTokenStatus refreshTokenStatus);

    @Modifying(clearAutomatically = true)
    @Query("update RefreshTokenEntity r set r.refreshTokenStatus = :refreshTokenStatus where r.referenceId = :referenceId")
    int updateRefreshTokenStatusByReferenceId(String referenceId, RefreshTokenStatus refreshTokenStatus);

    @Modifying(clearAutomatically = true)
    @Query("update RefreshTokenEntity r set r.refreshTokenStatus = :refreshTokenStatus where r.username = :username")
    int updateRefreshTokenStatusByUsername(String username, RefreshTokenStatus refreshTokenStatus);

    @Modifying
    @Query("delete from RefreshTokenEntity r where r.id = :id")
    int deleteById(Long id);

    @Modifying
    @Query("delete from RefreshTokenEntity r where r.referenceId = :referenceId")
    int deleteByReferenceId(String referenceId);

}
