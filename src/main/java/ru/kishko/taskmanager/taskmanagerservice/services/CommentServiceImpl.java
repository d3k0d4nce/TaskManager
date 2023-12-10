package ru.kishko.taskmanager.taskmanagerservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kishko.taskmanager.taskmanagerservice.dtos.CommentDTO;
import ru.kishko.taskmanager.taskmanagerservice.entites.Comment;
import ru.kishko.taskmanager.taskmanagerservice.errors.CommentNotFoundException;
import ru.kishko.taskmanager.taskmanagerservice.mappers.CommentMapper;
import ru.kishko.taskmanager.taskmanagerservice.repositories.CommentRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        commentRepository.save(commentMapper.toComment(commentDTO));
        return commentDTO;
    }

    @Override
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDTO).toList();
    }

    @Override
    public CommentDTO getCommentById(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("There is no comment with id: " + commentId)
        );

        return commentMapper.toDTO(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO) {

        CommentDTO commentDB = getCommentById(commentId);

        String text = commentDTO.getText();
        Long taskId = commentDTO.getTaskId();

        if (Objects.nonNull(text) && !"".equalsIgnoreCase(text)) {
            commentDB.setText(text);
        }

        if (Objects.nonNull(taskId) && taskId > 0) {
            commentDB.setTaskId(taskId);
        }

        commentRepository.save(commentMapper.toComment(commentDB));

        return commentDB;
    }

    @Override
    public String deleteCommentById(Long commentId) {

        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException("There is no comment with id: " + commentId);
        }

        commentRepository.deleteById(commentId);

        return "successfully deleted";
    }

    @Override
    public Page<CommentDTO> getFilteredAndPaginatedComments(Long taskId, String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByTaskIdAndTextContaining(taskId, filter, pageable);
        return comments.map(commentMapper::toDTO);
    }


}
