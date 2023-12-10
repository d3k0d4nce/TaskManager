package ru.kishko.taskmanager.authservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleProjectNotFoundException(UserNotFoundException projectNotFoundException) {

        UserException projectException = new UserException(
                projectNotFoundException.getMessage(),
                projectNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(projectException, projectException.getHttpStatus());

    }

}
