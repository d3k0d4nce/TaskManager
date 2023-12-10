package ru.kishko.taskmanager.taskmanagerservice.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kishko.taskmanager.authservice.entites.User;
import ru.kishko.taskmanager.authservice.errors.UserNotFoundException;
import ru.kishko.taskmanager.authservice.repositories.UserRepository;
import ru.kishko.taskmanager.taskmanagerservice.dtos.TaskDTO;
import ru.kishko.taskmanager.taskmanagerservice.entites.Priority;
import ru.kishko.taskmanager.taskmanagerservice.entites.Status;
import ru.kishko.taskmanager.taskmanagerservice.entites.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;

    public Task toTask(TaskDTO taskDTO) {

        User producer = null;
        User author = null;

        if (taskDTO.getProducerId() != null) {
            Long userId = taskDTO.getProducerId();
            producer = userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException("There is no user with id: " + userId)
            );
        }
        if (taskDTO.getAuthorId() != null) {
            Long userId = taskDTO.getAuthorId();
            author = userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException("There is no user with id: " + userId)
            );
        }

        return Task.builder()
                .id(taskDTO.getId())
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .status(Status.valueOf(taskDTO.getStatus()))
                .priority(Priority.valueOf(taskDTO.getPriority()))
                .producer(producer)
                .author(author)
                .users(taskDTO.getUsers())
                .comments(taskDTO.getComments())
                .build();
    }

    public TaskDTO toDTO(Task task) {

        Long producerId = null;
        Long authorId = null;

        if (task.getProducer() != null) {
            producerId = task.getProducer().getId();
        }
        if (task.getAuthor() != null) {
            authorId = task.getAuthor().getId();
        }

        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus().toString())
                .priority(task.getPriority().toString())
                .producerId(producerId)
                .authorId(authorId)
                .users(task.getUsers())
                .comments(task.getComments())
                .build();

    }

}
