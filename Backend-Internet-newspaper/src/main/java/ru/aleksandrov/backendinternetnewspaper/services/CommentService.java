package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment findById(Integer id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        } else {
            log.error("Comment with this " + id + " not found");
            throw new EntityNotFoundException("Comment with this " + id + " not found");
        }
    }


    public Comment saveComment(UserDetailsImpl userDetailsImpl, Comment comment, Integer idNews) {
        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        Comment newComment = new Comment();
        newComment.setAuthorComment(userService.findById(userDetailsImpl.getId()));
        newComment.setNews(newsService.findById(idNews));
        newComment.setTextComment(comment.getTextComment());
        newComment.setDatePublishedComment(moscowTimeNow);
        log.info("Save new comment from user: " + userDetailsImpl.getName());
        return commentRepository.save(newComment);
    }

    public void deleteComment(UserDetailsImpl userDetailsImpl, Integer idComment) {
        if (userDetailsImpl.getId() == findById(idComment).getAuthorComment().getId()) {
            commentRepository.deleteCommentByUserIdAndCommentId(userDetailsImpl.getId(), idComment);
        } else {
            log.error("Comment with this " + idComment + " not found");
            throw new EntityNotFoundException("Comment with this " + idComment + " not found");
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

}
