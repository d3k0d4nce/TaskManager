package ru.kishko.taskmanager.authservice.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UserException {

    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;

}
