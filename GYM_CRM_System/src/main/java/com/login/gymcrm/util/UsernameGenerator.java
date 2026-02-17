package com.login.gymcrm.util;

import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class UsernameGenerator {
    private InMemoryStorage storage;

    public String generate(String firstName, String lastName) {
        String base = buildBaseUsername(firstName, lastName);
        List<String> usernames = collectUsernames();
        return nextUsername(base, usernames);
    }

    private String buildBaseUsername(String firstName, String lastName) {
        return normalizeName(firstName) + "." + normalizeName(lastName);
    }

    private List<String> collectUsernames() {
        Map<String, Trainer> trainers = storage.getNamespace(StorageNamespaces.TRAINER);
        Map<String, Trainee> trainees = storage.getNamespace(StorageNamespaces.TRAINEE);

        return Stream.concat(
                        trainers.values().stream().map(Trainer::getUsername),
                        trainees.values().stream().map(Trainee::getUsername))
                .filter(Objects::nonNull)
                .toList();
    }

    private String nextUsername(String base, List<String> usernames) {
        boolean baseTaken = usernames.stream().anyMatch(u -> u.equalsIgnoreCase(base));
        int maxSuffix = usernames.stream().mapToInt(u -> parseSuffix(base, u)).max().orElse(0);

        if (!baseTaken && maxSuffix == 0) {
            return base;
        }
        return base + (maxSuffix + 1);
    }

    private int parseSuffix(String base, String username) {
        String lowerBase = base.toLowerCase(Locale.US);
        String lowerUser = username.toLowerCase(Locale.US);
        if (!lowerUser.startsWith(lowerBase) || lowerUser.length() == lowerBase.length()) {
            return 0;
        }
        String suffix = username.substring(base.length());
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

    @Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }
}
