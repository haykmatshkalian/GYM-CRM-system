package com.login.gymcrm.util;

import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class UsernameGenerator {
    private InMemoryStorage storage;

    public String generate(String firstName, String lastName) {
        String normalizedFirst = normalizeName(firstName);
        String normalizedLast = normalizeName(lastName);
        String base = normalizedFirst + "." + normalizedLast;

        Map<String, Trainer> trainers = storage.getNamespace(StorageNamespaces.TRAINER);
        Map<String, Trainee> trainees = storage.getNamespace(StorageNamespaces.TRAINEE);

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
        if (username == null) {
            return 0;
        }
        String lowerBase = base.toLowerCase(Locale.US);
        String lowerUser = username.toLowerCase(Locale.US);
        if (!lowerUser.startsWith(lowerBase)) {
            return 0;
        }
        String suffix = username.substring(base.length());
        if (suffix.isEmpty()) {
            return 0;
        }
        if (!suffix.chars().allMatch(Character::isDigit)) {
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
        return trimmed.replaceAll("\\s+", "");
    }

    private String safeLower(String value) {
        return value == null ? null : value.toLowerCase(Locale.US);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }
}
