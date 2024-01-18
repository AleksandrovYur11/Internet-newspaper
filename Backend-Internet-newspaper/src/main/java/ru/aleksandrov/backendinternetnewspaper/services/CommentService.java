package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.dto.model.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.security.services.impl.UserDetailsImpl;

import java.util.List;

public interface CommentService {
    Comment getCommentById(Integer commentId);
    void deleteCommentById(Integer commentId);
    Comment saveComment(UserDetailsImpl userDetailsImpl, Comment comment, Integer newsId);
    void deleteUserComment(UserDetailsImpl userDetailsImpl, Integer commentId);
    CommentDto convertToCommentDto(Comment comment);
    List<CommentDto> getThreeComments(Integer newsId);
    void clearLoadedCommentsCountMap();
    Integer getCountComments(Integer newsId);
}
