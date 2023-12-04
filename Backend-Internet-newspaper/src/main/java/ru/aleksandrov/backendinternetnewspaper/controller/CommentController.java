package ru.aleksandrov.backendinternetnewspaper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.model.Comment;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.UserService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final NewsService newsService;
    private final UserService userService;
    private final MappingUtil mappingUtil;

    private final Map<Integer, Integer> loadedCommentsCountMap = new HashMap<>();

    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentService commentService, NewsService newsService, UserService userService,
                             MappingUtil mappingUtil, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.newsService = newsService;
        this.userService = userService;
        this.mappingUtil = mappingUtil;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/show")
    public ResponseEntity<List<CommentDto>> getCommentsForNews(@RequestParam("newsId") Integer newsId) {
        Integer loadedCommentsCount = loadedCommentsCountMap.getOrDefault(newsId, 0);
        Pageable pageable = PageRequest.of(loadedCommentsCount / 3, 3);

        Slice<Comment> commentsSlice = commentService.getThreeComments(newsId, pageable);
        List<CommentDto> commentDto = commentsSlice.getContent().stream()
                .map(commentService::convertToCommentDto).collect(Collectors.toList());

        loadedCommentsCountMap.put(newsId, loadedCommentsCount + commentDto.size());
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PostMapping("/check-db")
    public ResponseEntity<Map<String, Integer>> checkExistComment(@RequestParam("newsId") Integer newsId) {
//        Integer countComment = commentService.getCountComments(newsId);
        Map<String, Integer> countComment = new HashMap<>();
        countComment.put("countComment", commentService.getCountComments(newsId));
//            Integer loadedCommentsCount = loadedCommentsCountMap.getOrDefault(newsId, 0);
//            Pageable pageable = PageRequest.of(loadedCommentsCount / 3, 3);
//
//            Slice<Comment> commentsSlice = commentService.getThreeComments(newsId, pageable);
//            List<CommentDto> commentDto = commentsSlice.getContent().stream()
//                    .map(commentService::convertToCommentDto).collect(Collectors.toList());
//
//            loadedCommentsCountMap.put(newsId, loadedCommentsCount + commentDto.size());
        return new ResponseEntity<>(countComment, HttpStatus.OK);

    }
//---------------------------------------------------------------

//    @PostMapping("/add/{idNews}")
//    public ResponseEntity<HttpStatus> addComment(@AuthenticationPrincipal UserDetails userDetails,
//                                                 @PathVariable("idNews") Long idNews,
//                                                 @RequestBody  CommentDTO commentDTO) {
//        try {
//            Comment comment = mappingService.convertToComment(commentDTO);
//            commentService.addNewComment(userDetails, comment, idNews);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                        @RequestParam("newsId") int newsId,
                                        @RequestBody @Valid CommentDto commentDTO) {
        Comment newComment = mappingUtil.convertToComment(commentDTO);
        Comment savedComment = commentService.saveComment(userDetailsImpl, newComment, newsId);
        CommentDto savedCommentDto = commentService.convertToCommentDto(savedComment);
        return new ResponseEntity<>(savedCommentDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> userDeleteComment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                        @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(userDetailsImpl, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> adminDeleteComment(@PathVariable("commentId") Integer commentId) {
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
