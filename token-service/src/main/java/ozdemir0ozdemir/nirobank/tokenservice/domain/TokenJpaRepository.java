package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

interface TokenJpaRepository extends PagingAndSortingRepository<TokenEntity, Long> {

    void save(TokenEntity te);

    Optional<TokenEntity> findByTokenId(String tokenId);

    Optional<TokenEntity> findByUsername(String username);

    Page<TokenEntity> findAllByUsername(String username, PageRequest of);

    Page<TokenEntity> findAllByTokenStatus(TokenStatus status, PageRequest of);

    Page<TokenEntity> findAllByUsernameAndTokenStatus(String username, TokenStatus status, PageRequest of);

    @Query("select TokenEntity te from TokenEntity where te.expiresAt < current timestamp and te.tokenStatus = :tokenStatus ")
    Page<TokenEntity> findAllExpiredTokensByTokenStatus(TokenStatus tokenStatus, PageRequest of);

    @Query("update TokenEntity t set t.tokenStatus = 'REVOKED' where t.tokenId = :tokenId")
    void revokeTokenByTokenId(String tokenId);

    @Query("delete from TokenEntity t where t.tokenId = :tokenId")
    void deleteTokenByTokenId(String tokenId);
}
