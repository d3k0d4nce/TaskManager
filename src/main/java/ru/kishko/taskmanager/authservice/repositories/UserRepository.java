package ru.kishko.taskmanager.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kishko.taskmanager.authservice.entites.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

}
