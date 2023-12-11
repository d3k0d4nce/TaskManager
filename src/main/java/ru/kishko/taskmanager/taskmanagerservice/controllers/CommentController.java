package ru.kishko.taskmanager.taskmanagerservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kishko.taskmanager.taskmanagerservice.dtos.CommentDTO;
import ru.kishko.taskmanager.taskmanagerservice.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(commentDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentById(@PathVariable("commentId") Long commentId, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.updateCommentById(commentId, commentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(commentService.deleteCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentDTO>> getFilteredAndPaginatedComments(
            @PathVariable("taskId") Long taskId,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(commentService.getFilteredAndPaginatedComments(taskId, filter, page, size), HttpStatus.OK);
    }

}
