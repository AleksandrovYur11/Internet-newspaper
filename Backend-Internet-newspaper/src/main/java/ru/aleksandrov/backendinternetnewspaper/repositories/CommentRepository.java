package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.models.News;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Transactional
    Optional<Comment> findById(Long id);

    @Transactional
    List<Comment> findAll();

    @Transactional
    List<Comment> findByNews(News news);

    @Transactional
    @Query("DELETE FROM comments c WHERE c.id = :commentId AND c.authorComment = :userId")
    void deleteCommentByUserIdAndCommentId(@Param("userId") Integer userId, @Param("commentId") Integer commentId);
}
