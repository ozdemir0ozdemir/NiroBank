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

    @Query("from UserEntity")
    Page<UserEntity> findAll(PageRequest pageRequest);

    Optional<UserEntity> findById(Long id);
    Page<UserEntity> findByUsername(String username, PageRequest pageRequest);
    Page<UserEntity> findByRole(Role role, Pageable pageable);
    Page<UserEntity> findByUsernameAndRole(String username, Role role, PageRequest pageRequest);

    @Modifying(clearAutomatically = true)
    @Query("""
            update UserEntity user set user.password = :newPassword 
            where user.username = :username
            """)
    void changePasswordByUsername(String username, String newPassword);

    @Modifying(clearAutomatically = true)
    @Query("""
            update UserEntity user set user.role = :role 
            where user.username = :username 
            """)
    void changeUserRoleByUsername(String username, Role role);

    void deleteById(Long id);
}
