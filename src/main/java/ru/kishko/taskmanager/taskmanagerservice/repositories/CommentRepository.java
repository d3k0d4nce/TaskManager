package ru.kishko.taskmanager.taskmanagerservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kishko.taskmanager.taskmanagerservice.entites.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByTaskIdAndTextContaining(Long taskId, String filter, Pageable pageable);

    Page<Comment> findByTaskId(Long taskId, Pageable pageable);
}
