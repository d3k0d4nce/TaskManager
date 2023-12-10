package ru.kishko.taskmanager.taskmanagerservice.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kishko.taskmanager.taskmanagerservice.dtos.CommentDTO;
import ru.kishko.taskmanager.taskmanagerservice.entites.Comment;
import ru.kishko.taskmanager.taskmanagerservice.entites.Task;
import ru.kishko.taskmanager.taskmanagerservice.errors.TaskNotFoundException;
import ru.kishko.taskmanager.taskmanagerservice.repositories.TaskRepository;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final TaskRepository taskRepository;

    public CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .taskId(comment.getTask().getId())
                .build();
    }

    public Comment toComment(CommentDTO commentDTO) {

        Task task = taskRepository.findById(commentDTO.getTaskId()).orElseThrow(
                () -> new TaskNotFoundException("There is no task with id: " + commentDTO.getTaskId())
        );

        return Comment.builder()
                .id(commentDTO.getId())
                .text(commentDTO.getText())
                .task(task)
                .build();
    }

}
