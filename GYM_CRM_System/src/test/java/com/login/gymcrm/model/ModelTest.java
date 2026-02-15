package com.login.gymcrm.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ModelTest {

    @Test
    void traineeEqualityAndToString() {
        Trainee a = new Trainee("1", "John", "Smith", "John.Smith", "pass", true);
        Trainee b = new Trainee("1", "John", "Smith", "John.Smith", "pass", true);
        Trainee c = new Trainee("2", "Jane", "Doe", "Jane.Doe", "pass", false);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("Trainee").contains("John").contains("Smith");
    }

    @Test
    void trainerEqualityAndToString() {
        Trainer a = new Trainer("1", "John", "Smith", "John.Smith", "pass", "Yoga");
        Trainer b = new Trainer("1", "John", "Smith", "John.Smith", "pass", "Yoga");
        Trainer c = new Trainer("2", "Jane", "Doe", "Jane.Doe", "pass", "Cardio");

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("Trainer").contains("Yoga");
    }

    @Test
    void trainingEqualityAndToString() {
        LocalDate date = LocalDate.of(2025, 1, 10);
        Training a = new Training("1", "T1", "R1", "Core", date, 30);
        Training b = new Training("1", "T1", "R1", "Core", date, 30);
        Training c = new Training("2", "T2", "R2", "Cardio", date, 45);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("Training").contains("Core").contains("30");
    }
}
