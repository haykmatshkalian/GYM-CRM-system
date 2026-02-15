package com.login.gymcrm.util;

import com.login.gymcrm.storage.InMemoryStorage;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UtilityTest {

    @Test
    void passwordGeneratorRejectsInvalidLength() {
        RandomPasswordGenerator generator = new RandomPasswordGenerator();
        assertThatThrownBy(() -> generator.generate(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void inMemoryStorageRegistersNamespaces() {
        InMemoryStorage storage = new InMemoryStorage();
        Map<String, String> map = new ConcurrentHashMap<>();
        storage.registerNamespace("demo", map);
        assertThat(storage.getNamespace("demo")).isSameAs(map);
        assertThat(storage.getAllNamespaces()).containsKey("demo");
    }
}
