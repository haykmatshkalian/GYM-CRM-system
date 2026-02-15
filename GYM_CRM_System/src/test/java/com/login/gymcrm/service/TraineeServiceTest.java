package com.login.gymcrm.service;

import com.login.gymcrm.config.AppConfig;
import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TraineeServiceTest {

    @Test
    void createUpdateDeleteTrainee() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TraineeService service = context.getBean(TraineeService.class);

            Trainee created = service.createProfile("Alice", "Brown");
            assertThat(created.getId()).isNotBlank();
            assertThat(created.getUsername()).startsWith("Alice.Brown");
            assertThat(created.getPassword()).hasSize(10);

            created.setFirstName("Alicia");
            Trainee updated = service.updateProfile(created);
            assertThat(updated.getFirstName()).isEqualTo("Alicia");

            Trainee selected = service.selectProfile(created.getId());
            assertThat(selected.getId()).isEqualTo(created.getId());

            service.deleteProfile(created.getId());
        }
    }

    @Test
    void validationFailsOnMissingName() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TraineeService service = context.getBean(TraineeService.class);
            assertThatThrownBy(() -> service.createProfile("", ""))
                    .isInstanceOf(ValidationException.class);
        }
    }
}
