package com.login.gymcrm.config;

import com.login.gymcrm.facade.GymCrmFacade;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class StorageInitializerTest {

    @Test
    void loadsSeedData() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymCrmFacade facade = context.getBean(GymCrmFacade.class);

            assertThat(facade.listTrainees()).hasSize(2);
            assertThat(facade.listTrainers()).hasSize(2);
            assertThat(facade.listTrainings()).hasSize(2);
        }
    }
}
