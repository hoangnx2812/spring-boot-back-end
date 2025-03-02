package com.devteria.identityservice.configuration;

import com.devteria.identityservice.entity.Role;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.repository.InvalidatedTokenRepository;
import com.devteria.identityservice.repository.RoleRepository;
import com.devteria.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${admin.password}")
    private String password;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<Role> role = new HashSet<>();
                role.add(roleRepository.findById("ADMIN").orElse(null));
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode(password))
                        .firstName("Admin")
                        .lastName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .roles(role)
                        .build();
                userRepository.save(admin);
                log.warn("Admin user has been created with default username and password: admin, {}", password);
            }
            invalidatedTokenRepository.deleteByExpiryTimeAfterNow();
        };
    }
}
