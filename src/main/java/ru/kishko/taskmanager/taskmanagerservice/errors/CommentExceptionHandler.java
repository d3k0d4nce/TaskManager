package ru.kishko.taskmanager.taskmanagerservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<Object> handleProjectNotFoundException(CommentNotFoundException commentNotFoundException) {

        CommentException commentException = new CommentException(
                commentNotFoundException.getMessage(),
                commentNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(commentException, commentException.getHttpStatus());

    }

}
