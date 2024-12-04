package ozdemir0ozdemir.userservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


interface UserRepository extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findByUsername(String username);
    Page<UserEntity> findByRole(Role role, Pageable pageable);
    Page<UserEntity> findByUsernameAndRole(String username, Role role);
}
