package ozdemir0ozdemir.nirobank.tokenservice.dbtimetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-db.properties")
class ModelTest {

    @Autowired
    private ModelRepo modelRepo ;

    @Test
    @Commit
    void saveModels() {

        modelRepo.deleteAll();

        Instant time1 = Instant.now();
        System.out.println("time : " + time1);

        modelRepo.save(new Model().setDateTime(time1));


    }

    @Test
    void loadAllModels() {


        List<Model> models = modelRepo.findAll();
        models.forEach(model -> {
            System.out.println(model.getId() + " : \t" + model.getDateTime() + " :\t " + model.getDateTime().atZone(Clock.systemDefaultZone().getZone()));
        });


    }
}
