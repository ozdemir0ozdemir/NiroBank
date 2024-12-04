package ozdemir0ozdemir.userservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


interface UserRepository extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findByUsername(String username);
    Page<UserEntity> findByRole(Role role, Pageable pageable);
    Page<UserEntity> findByUsernameAndRole(String username, Role role);

    @Query("""
            update UserEntity user set user.password = :newPassword 
            where user.username = :username
            """)
    void changePasswordByUsername(String username, String newPassword);

    @Query("""
            update UserEntity user set user.role = :role 
            where user.username = :username 
            and user.id = :userId
            """)
    void changeUserRoleByUsernameAndUserId(Role role, String username, Long userId);
}
