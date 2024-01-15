package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.model.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NewsService newsService;
    private final MappingUtil mappingUtil;

    private final Map<Integer, Integer> loadedCommentsCountMap = new HashMap<>();

    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository, NewsService newsService, MappingUtil mappingUtil) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.newsService = newsService;
        this.mappingUtil = mappingUtil;
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
            Comment comment = getCommentById(commentId);
            commentRepository.deleteById(commentId);
            loadedCommentsCountMap.put(comment.getNews().getId(), loadedCommentsCountMap
                    .getOrDefault(comment.getNews().getId(), 0) - 1);
            log.info("Delete comment with id = " + commentId + ": Success");
        } else {
            log.error("Comment with id = " + commentId + ": Not Found");
            throw new EntityNotFoundException("Comment with id = " + commentId + ": Not Found");
        }
    }

    public Comment saveComment(UserDetailsImpl userDetailsImpl, Comment comment, Integer newsId) {
        Comment newComment = new Comment();
        newComment.setAuthorComment(userService.getUserById(userDetailsImpl.getId()));
        newComment.setNews(newsService.getNewsById(newsId));
        newComment.setTextComment(comment.getTextComment());
        newComment.setDatePublishedComment(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        log.info("Save new comment from user (email) = " + userDetailsImpl.getEmail() + ": Success");
        Comment savedComment = commentRepository.save(newComment);
        loadedCommentsCountMap.put(newsId, loadedCommentsCountMap.getOrDefault(newsId, 0) + 1);
        return savedComment;
    }

    public void deleteUserComment(UserDetailsImpl userDetailsImpl, Integer commentId) {
        int userId = userDetailsImpl.getId();
        Comment comment = getCommentById(commentId);
        if (userId == comment.getAuthorComment().getId()) {
            commentRepository.deleteCommentByUserIdAndCommentId(userDetailsImpl.getId(), commentId);
            loadedCommentsCountMap.put(comment.getNews().getId(), loadedCommentsCountMap
                    .getOrDefault(comment.getNews().getId(), 0) - 1);
            log.info("Delete comment from user (email) = " + userDetailsImpl.getEmail() + ": Success");
        } else {
            throw new EntityNotFoundException("Comment with id = " + commentId + " and user (id) = " +
                    userId + ": Not Found");
        }
    }

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = mappingUtil.convertToCommentDto(comment);
        commentDto.setUser(mappingUtil.convertToUserDto(comment.getAuthorComment()));
        return commentDto;
    }

    public List<CommentDto> getThreeComments(Integer newsId) {
        Integer loadedCommentsCount = loadedCommentsCountMap.getOrDefault(newsId, 0);
        Pageable pageable = PageRequest.of(loadedCommentsCount, 1,
                Sort.by(Sort.Direction.DESC, "datePublishedComment"));
        Slice<Comment> commentsSlice = commentRepository.findThreeComments(newsId, pageable);
        List<CommentDto> commentDto = commentsSlice.getContent().stream()
                .map(this::convertToCommentDto).collect(Collectors.toList());
        loadedCommentsCountMap.put(newsId, loadedCommentsCount + commentDto.size());
        return commentDto;
    }

    public void clearLoadedCommentsCountMap() {
        loadedCommentsCountMap.clear();
    }

    public Integer getCountComments(Integer newsId) {
        return commentRepository.countByNewsId(newsId);
    }
}
