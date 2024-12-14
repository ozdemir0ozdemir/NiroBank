package ozdemir0ozdemir.nirobank.tokenservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface WithPostgresContainer {

    @Container
    @ServiceConnection
    PostgreSQLContainer<?> postgres
            = new PostgreSQLContainer<>("postgres:16-alpine");
}
