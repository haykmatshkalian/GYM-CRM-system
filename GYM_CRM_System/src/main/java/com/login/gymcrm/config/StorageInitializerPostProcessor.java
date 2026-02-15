package com.login.gymcrm.config;

import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.model.Training;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import com.login.gymcrm.util.RandomPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

@Component
public class StorageInitializerPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(StorageInitializerPostProcessor.class);

    private final Object lock = new Object();
    private boolean initialized = false;

    @Value("${storage.seed.path}")
    private String seedPath;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!initialized && (bean instanceof InMemoryStorage storage)) {
            synchronized (lock) {
                if (!initialized) {
                    loadSeedData(storage);
                    initialized = true;
                }
            }
        }
        return bean;
    }

    private void loadSeedData(InMemoryStorage storage) {
        Resource resource = resolveResource(seedPath);
        if (resource == null || !resource.exists()) {
            log.warn("Seed data resource not found at path={}", seedPath);
            return;
        }
        Map<String, Trainee> trainees = storage.getNamespace(StorageNamespaces.TRAINEE);
        Map<String, Trainer> trainers = storage.getNamespace(StorageNamespaces.TRAINER);
        Map<String, Training> trainings = storage.getNamespace(StorageNamespaces.TRAINING);
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator();

        log.info("Loading seed data from {}", seedPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] tokens = trimmed.split(",");
                String type = tokens[0].trim().toLowerCase();
                switch (type) {
                    case "trainee" -> loadTrainee(tokens, trainees, trainers, passwordGenerator);
                    case "trainer" -> loadTrainer(tokens, trainers, trainees, passwordGenerator);
                    case "training" -> loadTraining(tokens, trainings);
                    default -> log.warn("Unknown seed type: {}", type);
                }
            }
        } catch (IOException ex) {
            log.error("Failed to load seed data", ex);
        }
    }

    private void loadTrainee(String[] tokens, Map<String, Trainee> trainees,
                             Map<String, Trainer> trainers, RandomPasswordGenerator passwordGenerator) {
        if (tokens.length < 5) {
            log.warn("Invalid trainee seed row");
            return;
        }
        String id = tokens[1].trim();
        String first = tokens[2].trim();
        String last = tokens[3].trim();
        boolean active = Boolean.parseBoolean(tokens[4].trim());

        String username = generateUsername(first, last, trainees, trainers);
        String password = passwordGenerator.generate(10);

        Trainee trainee = new Trainee(id, first, last, username, password, active);
        trainees.put(id, trainee);
    }

    private void loadTrainer(String[] tokens, Map<String, Trainer> trainers,
                             Map<String, Trainee> trainees, RandomPasswordGenerator passwordGenerator) {
        if (tokens.length < 5) {
            log.warn("Invalid trainer seed row");
            return;
        }
        String id = tokens[1].trim();
        String first = tokens[2].trim();
        String last = tokens[3].trim();
        String specialization = tokens[4].trim();

        String username = generateUsername(first, last, trainees, trainers);
        String password = passwordGenerator.generate(10);

        Trainer trainer = new Trainer(id, first, last, username, password, specialization);
        trainers.put(id, trainer);
    }

    private void loadTraining(String[] tokens, Map<String, Training> trainings) {
        if (tokens.length < 7) {
            log.warn("Invalid training seed row");
            return;
        }
        String id = tokens[1].trim();
        String traineeId = tokens[2].trim();
        String trainerId = tokens[3].trim();
        String name = tokens[4].trim();
        LocalDate date = LocalDate.parse(tokens[5].trim());
        int duration = Integer.parseInt(tokens[6].trim());

        Training training = new Training(id, traineeId, trainerId, name, date, duration);
        trainings.put(id, training);
    }

    private Resource resolveResource(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("classpath:")) {
            return new ClassPathResource(path.substring("classpath:".length()));
        }
        try {
            return new UrlResource("file:" + path);
        } catch (Exception ex) {
            return null;
        }
    }

    private String generateUsername(String firstName, String lastName,
                                    Map<String, Trainee> trainees, Map<String, Trainer> trainers) {
        String normalizedFirst = normalizeName(firstName);
        String normalizedLast = normalizeName(lastName);
        String base = normalizedFirst + "." + normalizedLast;

        int maxSuffix = 0;
        boolean baseTaken = false;

        for (Trainer trainer : trainers.values()) {
            String username = safeLower(trainer.getUsername());
            if (username == null) {
                continue;
            }
            if (username.equalsIgnoreCase(base)) {
                baseTaken = true;
            }
            int suffix = parseSuffix(base, username);
            maxSuffix = Math.max(maxSuffix, suffix);
        }

        for (Trainee trainee : trainees.values()) {
            String username = safeLower(trainee.getUsername());
            if (username == null) {
                continue;
            }
            if (username.equalsIgnoreCase(base)) {
                baseTaken = true;
            }
            int suffix = parseSuffix(base, username);
            maxSuffix = Math.max(maxSuffix, suffix);
        }

        if (!baseTaken && maxSuffix == 0) {
            return base;
        }
        return base + (maxSuffix + 1);
    }

    private int parseSuffix(String base, String username) {
        String lowerBase = base.toLowerCase(Locale.US);
        String lowerUser = username.toLowerCase(Locale.US);
        if (!lowerUser.startsWith(lowerBase)) {
            return 0;
        }
        String suffix = username.substring(base.length());
        if (suffix.isEmpty() || !suffix.chars().allMatch(Character::isDigit)) {
            return 0;
        }
        try {
            return Integer.parseInt(suffix);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String normalizeName(String name) {
        String trimmed = name == null ? "" : name.trim();
        return trimmed.replaceAll("\\\\s+", "");
    }

    private String safeLower(String value) {
        return value == null ? null : value.toLowerCase(Locale.US);
    }
}
