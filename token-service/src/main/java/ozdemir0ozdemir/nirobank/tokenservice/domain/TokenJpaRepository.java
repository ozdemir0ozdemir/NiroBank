package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

interface TokenJpaRepository extends PagingAndSortingRepository<TokenEntity, Long> {

    TokenEntity save(TokenEntity te);

    Optional<TokenEntity> findByTokenId(String tokenId);

    @Query("from TokenEntity t where t.tokenStatus = 'ACCEPTABLE' ")
    Optional<TokenEntity> findByUsername(String username);

    Page<TokenEntity> findAllByUsername(String username, PageRequest of);

    Page<TokenEntity> findAllByTokenStatus(TokenStatus status, PageRequest of);

    Page<TokenEntity> findAllByUsernameAndTokenStatus(String username, TokenStatus status, PageRequest of);

    @Query("from TokenEntity t where t.tokenStatus = :status and t.expiresAt < current_timestamp ")
    Page<TokenEntity> findAllExpiredTokensByTokenStatus(TokenStatus status, PageRequest of);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TokenEntity t set t.tokenStatus = 'REVOKED' where t.tokenId = :tokenId")
    void revokeTokenByTokenId(String tokenId);

    @Modifying
    @Query("delete from TokenEntity t where t.tokenId = :tokenId")
    void deleteTokenByTokenId(String tokenId);
}
