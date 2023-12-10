package ru.kishko.taskmanager.taskmanagerservice.services;

import org.springframework.data.domain.Page;
import ru.kishko.taskmanager.taskmanagerservice.dtos.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);

    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(Long commentId);

    CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO);

    String deleteCommentById(Long commentId);

    Page<CommentDTO> getFilteredAndPaginatedComments(Long taskId, String filter, int page, int size);

//    List<CommentDTO> getCommentsByTaskId(Long taskId);
}
