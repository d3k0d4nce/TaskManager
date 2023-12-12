package ru.kishko.taskmanager.taskmanagerservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kishko.taskmanager.authservice.entites.User;
import ru.kishko.taskmanager.authservice.errors.UserNotFoundException;
import ru.kishko.taskmanager.authservice.repositories.UserRepository;
import ru.kishko.taskmanager.taskmanagerservice.dtos.CommentDTO;
import ru.kishko.taskmanager.taskmanagerservice.dtos.TaskDTO;
import ru.kishko.taskmanager.taskmanagerservice.entites.Priority;
import ru.kishko.taskmanager.taskmanagerservice.entites.Status;
import ru.kishko.taskmanager.taskmanagerservice.entites.Task;
import ru.kishko.taskmanager.taskmanagerservice.errors.InvalidUserException;
import ru.kishko.taskmanager.taskmanagerservice.errors.TaskNotFoundException;
import ru.kishko.taskmanager.taskmanagerservice.mappers.TaskMapper;
import ru.kishko.taskmanager.taskmanagerservice.repositories.TaskRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CommentService commentService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        taskDTO.setAuthorId(getCurrentUser().getId());
        Task task = taskMapper.toTask(taskDTO);
        task = taskRepository.save(task);
        return addTaskUsers(task.getId(), getCurrentUser().getId());
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO).toList();
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {

        if (userRepository.findById(userId).isPresent()) {
            return taskRepository.findTasksByUsersId(userId).stream()
                    .map(taskMapper::toDTO).toList();
        } else throw new UserNotFoundException("There is no user with id: " + userId);

    }

    @Override
    public TaskDTO getTaskById(Long taskId) throws TaskNotFoundException {

        Task project = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException("There is no project with id: " + taskId)
        );

        return taskMapper.toDTO(project);
    }

    @Override
    public TaskDTO updateTaskById(Long taskId, TaskDTO taskDTO) {

        TaskDTO taskDB = getTaskById(taskId);

        checkUserAccess(taskDB.getAuthorId());

        String name = taskDTO.getName();
        String description = taskDTO.getDescription();
        String status = taskDTO.getStatus();
        String priority = taskDTO.getPriority();

        if (Objects.nonNull(name) && !"".equalsIgnoreCase(name)) {
            taskDB.setName(name);
        }

        if (Objects.nonNull(description) && !"".equalsIgnoreCase(description)) {
            taskDB.setDescription(description);
        }

        if (Objects.nonNull(status) && !"".equalsIgnoreCase(status) && Arrays.asList(Status.values()).contains(Status.valueOf(status))) {
            taskDB.setStatus(status);
        }

        if (Objects.nonNull(priority) && !"".equalsIgnoreCase(priority) && Arrays.asList(Priority.values()).contains(Priority.valueOf(priority))) {
            taskDB.setPriority(priority);
        }

        taskRepository.save(taskMapper.toTask(taskDB));

        return taskDB;
    }

    @Override
    @Transactional
    public TaskDTO setProducerToTask(Long taskId, Long userId) {

        TaskDTO taskDB = getTaskById(taskId);

        checkUserAccess(taskDB.getAuthorId());

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("There is no user with id: " + userId);
        }

        taskDB.setProducerId(userId);

        taskRepository.save(taskMapper.toTask(taskDB));

        taskDB = addTaskUsers(taskId, userId);

        return taskDB;
    }

    @Override
    @Transactional
    public TaskDTO deleteProducerFromTask(Long taskId) {

        TaskDTO taskDB = getTaskById(taskId);

        checkUserAccess(taskDB.getAuthorId());

        if (taskDB.getProducerId() != null) {

            taskDB = deleteTasksUser(taskId, taskDB.getProducerId());
            taskDB.setProducerId(null);
            taskRepository.save(taskMapper.toTask(taskDB));

        }

        return taskDB;
    }

    @Override
    public TaskDTO changeTaskStatusById(Long taskId, String status) {

        TaskDTO taskDB = getTaskById(taskId);

        // Проверяем, принадлежит ли задача текущему пользователю

        checkUserAccess(taskDB.getProducerId());
        if (Objects.nonNull(status)
                && !"".equalsIgnoreCase(status)
                && Arrays.asList(Status.values()).contains(Status.valueOf(status))) {

            taskDB.setStatus(status);
            taskRepository.save(taskMapper.toTask(taskDB));
            return taskDB;

        } else throw new RuntimeException("Uncorrected status value");

    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @Transactional
    private TaskDTO addTaskUsers(Long taskId, Long userId) {

        List<User> users;
        TaskDTO taskDB = getTaskById(taskId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("There is no user with id: " + userId)
        );

        users = taskDB.getUsers();

        if (users.contains(user)) return taskDB;

        users.add(user);

        taskRepository.save(taskMapper.toTask(taskDB));

        return taskDB;

    }

    @Transactional
    private TaskDTO deleteTasksUser(Long taskId, Long userId) {

        List<User> users;
        TaskDTO taskDB = getTaskById(taskId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("There is no user with id: " + userId)
        );

        if (taskDB.getUsers().isEmpty()) {
            throw new RuntimeException("User with id: " + userId + " has an empty projectList.");
        }

        users = taskDB.getUsers();

        users.remove(user);

        taskRepository.save(taskMapper.toTask(taskDB));

        return taskDB;

    }

    @Override
    @Transactional
    public TaskDTO addCommentToTask(Long taskId, String commentText) {

        TaskDTO taskDB = getTaskById(taskId);

        if (taskDB.getProducerId().equals(getCurrentUser().getId()) ||
                taskDB.getAuthorId().equals(getCurrentUser().getId())) {

            CommentDTO commentDTO = CommentDTO.builder()
                    .text(commentText)
                    .taskId(taskId)
                    .build();

            commentService.createComment(commentDTO);

            taskRepository.save(taskMapper.toTask(taskDB));

        } else throw new InvalidUserException("You're don't have access to this action");

        return taskDB;
    }

    @Override
    public String deleteTaskById(Long taskId) {

        TaskDTO taskDB = getTaskById(taskId);

        checkUserAccess(taskDB.getAuthorId());

        taskRepository.deleteById(taskId);

        return "successful deleted";

    }

    private void checkUserAccess(Long userId) {
        if (!userId.equals(getCurrentUser().getId())) {
            throw new InvalidUserException("You're don't have access to this action");
        }
    }

}
