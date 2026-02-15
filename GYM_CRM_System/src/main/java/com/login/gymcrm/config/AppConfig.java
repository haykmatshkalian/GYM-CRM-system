package com.login.gymcrm.config;

import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.model.Training;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan(basePackages = "com.login.gymcrm")
@PropertySource("file:./config/app.properties")
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Map<String, Trainee> traineeStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Trainer> trainerStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Training> trainingStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public InMemoryStorage inMemoryStorage(Map<String, Trainee> traineeStore,
                                           Map<String, Trainer> trainerStore,
                                           Map<String, Training> trainingStore) {
        InMemoryStorage storage = new InMemoryStorage();
        storage.registerNamespace(StorageNamespaces.TRAINEE, traineeStore);
        storage.registerNamespace(StorageNamespaces.TRAINER, trainerStore);
        storage.registerNamespace(StorageNamespaces.TRAINING, trainingStore);
        return storage;
    }
}
