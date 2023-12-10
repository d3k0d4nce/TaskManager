package ru.kishko.taskmanager.taskmanagerservice.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class TaskException {

    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;

}
