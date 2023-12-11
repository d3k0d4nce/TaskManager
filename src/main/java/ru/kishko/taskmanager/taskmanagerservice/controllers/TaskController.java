package ru.kishko.taskmanager.taskmanagerservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kishko.taskmanager.taskmanagerservice.dtos.TaskDTO;
import ru.kishko.taskmanager.taskmanagerservice.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("taskId") Long taskId) {
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskDTO> updateTaskById(@PathVariable("taskId") Long taskId, @RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.updateTaskById(taskId, taskDTO), HttpStatus.OK);
    }

    @PostMapping("/add-comment/{taskId}")
    public ResponseEntity<TaskDTO> addCommentToTask(@PathVariable("taskId") Long taskId, @RequestParam("commentText") String commentText) {
        return new ResponseEntity<>(taskService.addCommentToTask(taskId, commentText), HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteTaskById(@PathVariable("taskId") Long taskId) {
        return new ResponseEntity<>(taskService.deleteTaskById(taskId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(taskService.getTasksByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/{taskId}/producer/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskDTO> setProducerToTask(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(taskService.setProducerToTask(taskId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}/delete-producer")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskDTO> deleteProducerFromTask(@PathVariable("taskId") Long taskId) {
        return new ResponseEntity<>(taskService.deleteProducerFromTask(taskId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PRODUCER')")
    @PutMapping("/change-status/{taskId}")
    public ResponseEntity<TaskDTO> changeStatusByTaskId(@PathVariable("taskId") Long taskId, @RequestParam("status") String status) {
        return new ResponseEntity<>(taskService.changeTaskStatusById(taskId, status), HttpStatus.OK);
    }

}
