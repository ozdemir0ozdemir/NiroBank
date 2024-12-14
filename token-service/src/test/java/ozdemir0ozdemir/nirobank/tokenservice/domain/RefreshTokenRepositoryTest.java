package ozdemir0ozdemir.nirobank.tokenservice.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ozdemir0ozdemir.nirobank.tokenservice.WithPostgresContainer;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:test-token-data.sql")
class RefreshTokenRepositoryTest implements WithPostgresContainer {

    @Autowired
    private RefreshTokenRepository repository;

    private RefreshTokenEntity VALID_TOKEN;
    private final PageRequest smallSizedFirstPage = PageRequest.of(0, 5);
    private final PageRequest largeSizedFirstPage = PageRequest.of(0, 50);
    private final int TOTAL_ACCEPTABLE_REFRESH_TOKEN = 15;
    private final int TOTAL_REVOKED_REFRESH_TOKEN = 15;
    private final int TOTAL_REFRESH_TOKEN = 30;

    @BeforeEach
    void beforeEach() throws Exception {
           VALID_TOKEN = new RefreshTokenEntity()
                .setReferenceId("ref_001")
                .setUsername("Starlight123")
                .setRefreshToken("token12345")
                .setExpiresAt(Timestamp.valueOf("2025-12-31 00:30:00"))
                .setRefreshTokenStatus(RefreshTokenStatus.ACCEPTABLE);
    }


    @Test
    void should_saveToken() throws Exception {
        //given
        RefreshTokenEntity entity = RefreshTokenEntity.of("test-token", "refresh-token123", Timestamp.valueOf("2025-12-31 00:30:00"));

        //when
        RefreshTokenEntity saved = repository.save(entity);

        entity.setId(saved.getId());

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved).isEqualTo(entity);
    }

    @Test
    void should_findRefreshTokenById() throws Exception {
        //when
        RefreshTokenEntity ent = repository.findById(1L).get();

        //then
        assertThat(ent.getId()).isNotNull();
        assertThat(ent.getExpiresAt()).isNotNull();
        assertThat(ent.getReferenceId()).isNotNull();
        assertThat(ent.getRefreshToken()).isNotNull();
        assertThat(ent.getUsername()).isNotNull();
        assertThat(ent.getRefreshTokenStatus()).isNotNull();

    }

    @Test
    void should_findRefreshTokenByReferenceId() throws Exception {
        //when
        RefreshTokenEntity ent = repository.findByReferenceId(VALID_TOKEN.getReferenceId()).get();

        //then
        assertThat(ent.getId()).isNotNull();
        assertThat(ent.getExpiresAt()).isEqualTo(VALID_TOKEN.getExpiresAt());
        assertThat(ent.getReferenceId()).isEqualTo(VALID_TOKEN.getReferenceId());
        assertThat(ent.getRefreshToken()).isEqualTo(VALID_TOKEN.getRefreshToken());
        assertThat(ent.getUsername()).isEqualTo(VALID_TOKEN.getUsername());
        assertThat(ent.getRefreshTokenStatus()).isEqualTo(VALID_TOKEN.getRefreshTokenStatus());
    }

    @Test
    void should_findAllRefreshTokensByUsername() throws Exception {
        //when
        Page<RefreshTokenEntity> page =
                repository.findAllByUsername(VALID_TOKEN.getUsername(), largeSizedFirstPage);

        //then
        assertThat(page.getTotalElements()).isEqualTo(3);
        page.forEach(ent -> assertThat(ent.getUsername()).isEqualTo(VALID_TOKEN.getUsername()));
    }

    @Test
    void should_not_findAnyRefreshTokensByUsername_when_userNotExist() throws Exception {
        //when
        Page<RefreshTokenEntity> page =
                repository.findAllByUsername("non-existent-user", largeSizedFirstPage);
        //then
        assertThat(page.getTotalElements()).isEqualTo(0);
    }

    @Test
    void should_findAllRefreshTokensByRefreshTokenStatus_ACCEPTABLE() throws Exception {
        //when
        Page<RefreshTokenEntity> page =
                repository.findAllByRefreshTokenStatus(RefreshTokenStatus.ACCEPTABLE, largeSizedFirstPage);

        //then
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ACCEPTABLE_REFRESH_TOKEN);
        page.forEach(ent -> assertThat(ent.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.ACCEPTABLE));
    }

    @Test
    void should_findAllRefreshTokensByRefreshTokenStatus_REVOKED() throws Exception {
        //when
        Page<RefreshTokenEntity> page =
                repository.findAllByRefreshTokenStatus(RefreshTokenStatus.REVOKED, largeSizedFirstPage);

        //then
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_REVOKED_REFRESH_TOKEN);
        page.forEach(ent -> assertThat(ent.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.REVOKED));
    }

    @Test
    void should_findAllByUsernameAndRefreshTokenStatus() throws Exception {
        //when
        Page<RefreshTokenEntity> page = repository.findAllByUsernameAndRefreshTokenStatus(
                VALID_TOKEN.getUsername(),
                RefreshTokenStatus.ACCEPTABLE,
                largeSizedFirstPage
        );

        //then
        assertThat(page.getTotalElements()).isEqualTo(2);
        page.forEach(ent -> {
            assertThat(ent.getUsername()).isEqualTo(VALID_TOKEN.getUsername());
            assertThat(ent.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.ACCEPTABLE);
        });
    }

    @Test
    void should_findAllByExpiresAtAfter() throws Exception {
        //given
        Timestamp date = Timestamp.valueOf("2025-12-31 00:00:00");

        //when
        Page<RefreshTokenEntity> page = repository.findAllByExpiresAtAfter(date, largeSizedFirstPage);

        //then
        assertThat(page.getTotalElements()).isEqualTo(20);
        page.forEach(ent -> {
            assertThat(ent.getExpiresAt()).isAfter(date);
        });
    }

    @Test
    void should_findAllByExpiresAtBefore() throws Exception {
        //given
        Timestamp date = Timestamp.valueOf("2025-12-31 00:00:00");

        //when
        Page<RefreshTokenEntity> page = repository.findAllByExpiresAtBefore(date, largeSizedFirstPage);

        //then
        assertThat(page.getTotalElements()).isEqualTo(10);
        page.forEach(ent -> {
            assertThat(ent.getExpiresAt()).isBefore(date);
        });
    }

    @Test
    void should_updateRefreshTokenStatusById() throws Exception {
        //given
        RefreshTokenEntity entity = repository.findById(1L).get();

        //when
        int affectedRow = repository.updateRefreshTokenStatusById(entity.getId(), RefreshTokenStatus.REVOKED);
        entity = repository.findById(1L).get();

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.REVOKED);

        //when
        affectedRow = repository.updateRefreshTokenStatusById(entity.getId(), RefreshTokenStatus.ACCEPTABLE);
        entity = repository.findById(1L).get();

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.ACCEPTABLE);
    }

    @Test
    void should_not_updateRefreshTokenStatusByReferenceId_when_idNotFound() throws Exception {
        //when
        int affectedRow = repository.updateRefreshTokenStatusById(999L, RefreshTokenStatus.REVOKED);

        //then
        assertThat(affectedRow).isEqualTo(0);
    }

    @Test
    void should_updateRefreshTokenStatusByReferenceId() throws Exception {
        //given
        RefreshTokenEntity entity = repository.findByReferenceId(VALID_TOKEN.getReferenceId()).get();

        //when
        int affectedRow = repository.updateRefreshTokenStatusByReferenceId(entity.getReferenceId(), RefreshTokenStatus.REVOKED);
        entity = repository.findByReferenceId(VALID_TOKEN.getReferenceId()).get();

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.REVOKED);

        //when
        affectedRow = repository.updateRefreshTokenStatusByReferenceId(entity.getReferenceId(), RefreshTokenStatus.ACCEPTABLE);
        entity = repository.findByReferenceId(VALID_TOKEN.getReferenceId()).get();

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.ACCEPTABLE);
    }

    @Test
    void should_not_updateRefreshTokenStatusByReferenceId_when_refIdNotFound() throws Exception {
        //when
        int affectedRow = repository.updateRefreshTokenStatusByReferenceId("non-existent-ref-id", RefreshTokenStatus.REVOKED);

        //then
        assertThat(affectedRow).isEqualTo(0);
    }

    @Test
    void should_updateRefreshTokenStatusByUsername() throws Exception {

        Page<RefreshTokenEntity> page;
        //when
        int affectedRow = repository.updateRefreshTokenStatusByUsername(VALID_TOKEN.getUsername(), RefreshTokenStatus.REVOKED);
        page = repository.findAllByUsername(VALID_TOKEN.getUsername(), largeSizedFirstPage);

        //then
        page.forEach(entity ->  assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.REVOKED));
        assertThat(affectedRow).isEqualTo(3);

        //when
        affectedRow = repository.updateRefreshTokenStatusByUsername(VALID_TOKEN.getUsername(), RefreshTokenStatus.ACCEPTABLE);
        page = repository.findAllByUsername(VALID_TOKEN.getUsername(), largeSizedFirstPage);

        //then
        page.forEach(entity ->  assertThat(entity.getRefreshTokenStatus()).isEqualTo(RefreshTokenStatus.ACCEPTABLE));
        assertThat(affectedRow).isEqualTo(3);

    }

    @Test
    void should_not_updateRefreshTokenStatusByUsername_when_usernameNotFound() throws Exception {
        //when
        int affectedRow = repository.updateRefreshTokenStatusByUsername("non-existent-username", RefreshTokenStatus.REVOKED);

        //then
        assertThat(affectedRow).isEqualTo(0);
    }

    @Test
    void should_deleteById() throws Exception {
        //when
        int affectedRow = repository.deleteById(1L);
        Page<RefreshTokenEntity> page = repository.findAll(largeSizedFirstPage);

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_REFRESH_TOKEN - 1);
    }

    @Test
    void should_deleteById_when_idNotFound() throws Exception {
        //when
        int affectedRow = repository.deleteById(999L);
        Page<RefreshTokenEntity> page = repository.findAll(largeSizedFirstPage);

        //then
        assertThat(affectedRow).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_REFRESH_TOKEN);
    }

    @Test
    void should_deleteByReferenceId() throws Exception {
        //when
        int affectedRow = repository.deleteByReferenceId(VALID_TOKEN.getReferenceId());
        Page<RefreshTokenEntity> page = repository.findAll(largeSizedFirstPage);

        //then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_REFRESH_TOKEN - 1);
    }

    @Test
    void should_not_deleteByReferenceId_when_refIdNotFound() throws Exception {
        //when
        int affectedRow = repository.deleteByReferenceId("9999999999999999999");
        Page<RefreshTokenEntity> page = repository.findAll(largeSizedFirstPage);

        //then
        assertThat(affectedRow).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_REFRESH_TOKEN);
    }

}