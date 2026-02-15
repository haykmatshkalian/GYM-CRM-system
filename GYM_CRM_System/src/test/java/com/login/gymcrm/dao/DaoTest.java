package com.login.gymcrm.dao;

import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.model.Training;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class DaoTest {

    @Test
    void traineeDaoCrud() {
        InMemoryStorage storage = new InMemoryStorage();
        Map<String, Trainee> trainees = new ConcurrentHashMap<>();
        storage.registerNamespace(StorageNamespaces.TRAINEE, trainees);

        InMemoryTraineeDao dao = new InMemoryTraineeDao();
        dao.setStorage(storage);

        Trainee trainee = new Trainee("1", "A", "B", "A.B", "pass", true);
        dao.save(trainee);
        assertThat(dao.findById("1")).contains(trainee);

        trainee.setFirstName("AA");
        dao.update(trainee);
        assertThat(dao.findById("1").orElseThrow().getFirstName()).isEqualTo("AA");

        dao.deleteById("1");
        assertThat(dao.findById("1")).isEmpty();
    }

    @Test
    void trainerDaoCrud() {
        InMemoryStorage storage = new InMemoryStorage();
        Map<String, Trainer> trainers = new ConcurrentHashMap<>();
        storage.registerNamespace(StorageNamespaces.TRAINER, trainers);

        InMemoryTrainerDao dao = new InMemoryTrainerDao();
        dao.setStorage(storage);

        Trainer trainer = new Trainer("1", "A", "B", "A.B", "pass", "Yoga");
        dao.save(trainer);
        assertThat(dao.findById("1")).contains(trainer);

        trainer.setSpecialization("Cardio");
        dao.update(trainer);
        assertThat(dao.findById("1").orElseThrow().getSpecialization()).isEqualTo("Cardio");

        dao.deleteById("1");
        assertThat(dao.findById("1")).isEmpty();
    }

    @Test
    void trainingDaoCrud() {
        InMemoryStorage storage = new InMemoryStorage();
        Map<String, Training> trainings = new ConcurrentHashMap<>();
        storage.registerNamespace(StorageNamespaces.TRAINING, trainings);

        InMemoryTrainingDao dao = new InMemoryTrainingDao();
        dao.setStorage(storage);

        Training training = new Training("1", "T1", "R1", "Core", LocalDate.now(), 30);
        dao.save(training);
        assertThat(dao.findById("1")).contains(training);

        training.setDurationMinutes(45);
        dao.update(training);
        assertThat(dao.findById("1").orElseThrow().getDurationMinutes()).isEqualTo(45);

        dao.deleteById("1");
        assertThat(dao.findById("1")).isEmpty();
    }
}
