package com.login.gymcrm.util;

import com.login.gymcrm.config.AppConfig;
import com.login.gymcrm.dao.TraineeDao;
import com.login.gymcrm.dao.TrainerDao;
import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class UsernameGeneratorTest {

    @Test
    void generatesUniqueUsernameAcrossTraineeAndTrainer() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UsernameGenerator generator = context.getBean(UsernameGenerator.class);
            TraineeDao traineeDao = context.getBean(TraineeDao.class);
            TrainerDao trainerDao = context.getBean(TrainerDao.class);

            Trainee trainee = new Trainee("T100", "John", "Smith", "John.Smith", "secret", true);
            traineeDao.save(trainee);
            Trainer trainer = new Trainer("R100", "John", "Smith", "John.Smith1", "secret", "Cardio");
            trainerDao.save(trainer);

            String username = generator.generate("John", "Smith");
            assertThat(username).isEqualTo("John.Smith2");
        }
    }
}
