package ru.kishko.taskmanager.taskmanagerservice.services;

import ru.kishko.taskmanager.taskmanagerservice.dtos.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long taskId);

    TaskDTO updateTaskById(Long taskId, TaskDTO taskDTO);

    String deleteTaskById(Long taskId);

    TaskDTO addCommentToTask(Long taskId, String commentText);

    List<TaskDTO> getTasksByUserId(Long userId);

    TaskDTO setProducerToTask(Long taskId, Long userId);

    TaskDTO changeTaskStatusById(Long taskId, String status);
}
