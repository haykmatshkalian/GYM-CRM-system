package com.login.gymcrm.service;

import com.login.gymcrm.config.AppConfig;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrainerServiceTest {

    @Test
    void createAndSelectTrainer() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TrainerService service = context.getBean(TrainerService.class);

            Trainer created = service.createProfile("Mark", "Lewis", "Yoga");
            assertThat(created.getId()).isNotBlank();
            assertThat(created.getUsername()).startsWith("Mark.Lewis");
            assertThat(created.getPassword()).hasSize(10);

            Trainer selected = service.selectProfile(created.getId());
            assertThat(selected.getId()).isEqualTo(created.getId());
        }
    }

    @Test
    void rejectsMissingName() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            TrainerService service = context.getBean(TrainerService.class);

            assertThatThrownBy(() -> service.createProfile("", "", ""))
                    .isInstanceOf(ValidationException.class);
        }
    }
}
