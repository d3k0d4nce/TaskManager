package ru.kishko.taskmanager.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kishko.taskmanager.authservice.entites.Role;
import ru.kishko.taskmanager.authservice.repositories.UserRepository;
import ru.kishko.taskmanager.authservice.entites.User;
import ru.kishko.taskmanager.authservice.services.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            User admin = User.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            User user = User.builder()
                    .firstName("user")
                    .lastName("user")
                    .email("user@user.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_USER)
                    .build();

            User producer = User.builder()
                    .firstName("producer")
                    .lastName("producer")
                    .email("producer@producer.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_PRODUCER)
                    .build();

            userService.save(admin);
            userService.save(user);
            userService.save(producer);
            log.debug("created ADMIN user - {}", admin);
            log.debug("created USER user - {}", user);
            log.debug("created PRODUCER user - {}", producer);

        }

    }
}
