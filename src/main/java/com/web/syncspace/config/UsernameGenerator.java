package com.web.syncspace.config;

import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.UUID;

@Configuration
public class UsernameGenerator {

    public String generateUsername(String fullName) {
        String firstName = fullName.split(" ")[0].toLowerCase(Locale.ROOT);
        String randomPostfix = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        return firstName + randomPostfix;
    }

}
