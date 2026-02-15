package com.login.gymcrm.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
