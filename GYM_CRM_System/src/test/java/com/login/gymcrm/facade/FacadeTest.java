package com.login.gymcrm.facade;

import com.login.gymcrm.config.AppConfig;
import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.model.Training;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class FacadeTest {

    @Test
    void facadeDelegatesOperations() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymCrmFacade facade = context.getBean(GymCrmFacade.class);

            Trainee trainee = facade.createTrainee("Mia", "Lee");
            Trainer trainer = facade.createTrainer("Leo", "King", "Strength");
            Training training = facade.createTraining(trainee.getId(), trainer.getId(), "Intro", LocalDate.now(), 40);

            assertThat(facade.selectTrainee(trainee.getId())).isNotNull();
            assertThat(facade.selectTrainer(trainer.getId())).isNotNull();
            assertThat(facade.selectTraining(training.getId())).isNotNull();

            assertThat(facade.listTrainees()).isNotEmpty();
            assertThat(facade.listTrainers()).isNotEmpty();
            assertThat(facade.listTrainings()).isNotEmpty();
        }
    }
}
