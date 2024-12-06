package ozdemir0ozdemir.nirobank.tokenservice.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TokenJpaRepository extends PagingAndSortingRepository<TokenEntity, Long> {
}
