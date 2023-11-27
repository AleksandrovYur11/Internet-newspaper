package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NewsService newsService;

    @Autowired
    public CommentService(UserRepository userRepository, CommentRepository commentRepository, NewsService newsService) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.newsService = newsService;
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


    public void addNewComment(UserDetailsImpl userDetailsImpl, Comment comment, Integer idNews) {
        Comment newComment = new Comment();
        newComment.setAuthorComment(userRepository.findById(userDetailsImpl.getId()).get());
        newComment.setNews(newsService.findById(idNews));
        newComment.setTextComment(comment.getTextComment());
        newComment.setDatePublishedComment(new Date());
        commentRepository.save(newComment);
        log.info("Save new comment from user: " + userDetailsImpl.getName());
    }

    public void removeComment(UserDetailsImpl userDetailsImpl, Integer idComment) {
        if (userDetailsImpl.getId() == findById(idComment).getId()) {
            commentRepository.deleteById(idComment);
        } else {
            log.error("Comment with this " + idComment + " not found");
            throw new EntityNotFoundException("Comment with this " + idComment + " not found");
        }
    }

}
