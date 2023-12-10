package ru.kishko.taskmanager.taskmanagerservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.kishko.taskmanager.authservice.entites.User;
import ru.kishko.taskmanager.taskmanagerservice.entites.Comment;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {

    private Long id;

    private String name;

    private String description;

    private String status;

    private String priority;

    private Long authorId;

    private Long producerId;

    @JsonIgnore
    private List<User> users = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

}
