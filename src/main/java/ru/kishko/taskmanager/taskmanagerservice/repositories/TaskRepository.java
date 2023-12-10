package ru.kishko.taskmanager.taskmanagerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kishko.taskmanager.taskmanagerservice.entites.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTasksByUsersId(Long users_id);

}
