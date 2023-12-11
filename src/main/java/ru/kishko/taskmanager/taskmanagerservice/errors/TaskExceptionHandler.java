package ru.kishko.taskmanager.taskmanagerservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<Object> handleProjectNotFoundException(TaskNotFoundException taskNotFoundException) {

        TaskException taskException = new TaskException(
                taskNotFoundException.getMessage(),
                taskNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(taskException, taskException.getHttpStatus());

    }

    @ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException invalidUserException) {

        TaskException taskException = new TaskException(
                invalidUserException.getMessage(),
                invalidUserException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(taskException, taskException.getHttpStatus());
    }

}
