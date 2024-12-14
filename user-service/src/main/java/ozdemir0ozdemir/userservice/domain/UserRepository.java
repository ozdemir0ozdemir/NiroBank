package ozdemir0ozdemir.userservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity save(UserEntity userEntity);

    Page<UserEntity> findAllByRole(Role role, Pageable pageable);
    Optional<UserEntity> findByUsername(String username);

    @Query("from UserEntity u where u.id = :id")
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsernameAndRole(String username, Role role);

    @Modifying
    @Query("update UserEntity user set user.password = :newPassword where user.username = :username")
    int changePasswordByUsername(String username, String newPassword);

    @Modifying
    @Query("update UserEntity user set user.role = :role where user.username = :username")
    int changeUserRoleByUsername(String username, Role role);

    @Modifying
    @Query("delete from UserEntity user where user.id = :id")
    int deleteById(Long id);
}
