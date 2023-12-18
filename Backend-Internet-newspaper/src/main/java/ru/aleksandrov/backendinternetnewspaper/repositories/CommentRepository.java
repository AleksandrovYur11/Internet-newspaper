package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.model.Comment;
import ru.aleksandrov.backendinternetnewspaper.model.News;


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
    @Modifying
    @Query("DELETE FROM comments c WHERE c.id = :commentId AND c.authorComment.id = :userId")
    void deleteCommentByUserIdAndCommentId(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    @Transactional
    @Query("SELECT c FROM comments c WHERE c.news.id = :newsId")
    Slice<Comment> findThreeComments(@Param("newsId") Integer newsId, Pageable pageable);
    Integer countByNewsId(Integer newsId);
//    @Transactional
//    @Query("SELECT c FROM comments c WHERE  EXISTS c.news.id = :newsId ")
//    Boolean existsComments(@Param("newsId") Integer newsId, Pageable pageable);


}
