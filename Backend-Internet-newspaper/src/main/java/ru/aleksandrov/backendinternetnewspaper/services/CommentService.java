package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.model.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.model.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@Service
@Slf4j
public class CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NewsService newsService;
    private final MappingUtil mappingUtil;

    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository,
                          NewsService newsService, MappingUtil mappingUtil) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.newsService = newsService;
        this.mappingUtil = mappingUtil;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findByNews(News news) {
        return commentRepository.findByNews(news);
    }


    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.error("Comment with id = " + commentId + ": Not Found");
                    return new EntityNotFoundException("Comment with id = " + commentId + ": Not Found");
                });
    }

    public void deleteCommentById(Integer commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            log.info("Delete comment with id = " + commentId + ": Success");
        } else {
            log.error("Comment with id = " + commentId + ": Not Found");
            throw new EntityNotFoundException("Comment with id = " + commentId + ": Not Found");
        }
    }


    public Comment saveComment(UserDetailsImpl userDetailsImpl, Comment comment, Integer newsId) {
        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        Comment newComment = new Comment();
        newComment.setAuthorComment(userService.getUserById(userDetailsImpl.getId()));
        newComment.setNews(newsService.getNewsById(newsId));
        newComment.setTextComment(comment.getTextComment());
        newComment.setDatePublishedComment(moscowTimeNow);
        log.info("Save new comment from user (email) = " + userDetailsImpl.getEmail());
        return commentRepository.save(newComment);
    }

    public void deleteUserComment(UserDetailsImpl userDetailsImpl, Integer commentId) {
        int userId = userDetailsImpl.getId();
        if (userId == getCommentById(commentId).getAuthorComment().getId()) {
            commentRepository.deleteCommentByUserIdAndCommentId(userDetailsImpl.getId(), commentId);
            log.info("Delete comment with id = " + commentId + ": Success");
        } else {
            throw new EntityNotFoundException("Comment with id = " + commentId + " and user (id) = " +
                    userId + ": Not Found");
        }
    }

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = mappingUtil.convertToCommentDto(comment);
        commentDto.setUser(mappingUtil.convertToUserDto(comment.getAuthorComment()));
//
//        List<Like> likes = news.getLikes();
//        newsDTO.setLikes(likes.stream().map(mappingUtil::convertToLikeDto)
//                .collect(Collectors.toList()));
//
//        List<Comment> comments = news.getComments();
//        newsDTO.setComments(comments.stream().map((comment) -> {
//                    CommentDto commentDto = mappingUtil.convertToCommentDto(comment);
//                    commentDto.setUser(mappingUtil.convertToUserDto(comment.getAuthorComment()));
//                    return commentDto;
//                })
//                .collect(Collectors.toList()));
//
//        Picture picture = news.getPicture();
//        newsDTO.setPicture(mappingUtil.convertToPictureDto(picture));
//
//        Set<Theme> themes = news.getTheme();
//        newsDTO.setThemes(themes.stream().map(mappingUtil::convertToThemeDto).collect(Collectors.toSet()));
        return commentDto;
    }

    public Slice<Comment> getThreeComments(Integer newsId, Pageable pageable) {
        return commentRepository.findThreeComments(newsId, pageable);
    }

    public Integer getCountComments(Integer newsId) {
        return commentRepository.countByNewsId(newsId);
    }

}
