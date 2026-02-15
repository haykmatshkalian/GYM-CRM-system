package com.login.gymcrm.service;

import com.login.gymcrm.config.AppConfig;
import com.login.gymcrm.model.Training;
import com.login.gymcrm.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrainingServiceTest {

    @Test
    void createAndSelectTraining() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TrainingService service = context.getBean(TrainingService.class);

            Training training = service.createTraining("T001", "R001", "Core", LocalDate.now(), 30);
            assertThat(training.getId()).isNotBlank();

            Training selected = service.selectTraining(training.getId());
            assertThat(selected.getName()).isEqualTo("Core");
        }
    }

    @Test
    void rejectsInvalidTrainingInput() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TrainingService service = context.getBean(TrainingService.class);

            assertThatThrownBy(() -> service.createTraining("", "R001", "", LocalDate.now(), 0))
                    .isInstanceOf(ValidationException.class);
        }
    }
}
