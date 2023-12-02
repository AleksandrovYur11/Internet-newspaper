package ru.aleksandrov.backendinternetnewspaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.UserService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final NewsService newsService;
    private final UserService userService;
    private final MappingUtil mappingUtil;

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


//---------------------------------------------------------------
    //    @GetMapping("/all")
//    public ResponseEntity<List<CommentDTO>> getAllCommentByNews() {
//        try {
//            List<Comment> commentList = commentService.findAll();
//            List<CommentDTO> commentDTOS = commentList.stream().map(this::convertToCommentDTO).toList();
//            return new ResponseEntity<>(commentDTOS, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
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

    @PostMapping("/{idNews}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                        @PathVariable("idNews") int idNews,
                                        @RequestBody @Valid CommentDto commentDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.getFieldError() != null) {
                return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Incorrectly written comment", HttpStatus.BAD_REQUEST);
            }
        }
        try {
            Comment comment = mappingUtil.convertToComment(commentDTO);

            Comment savedComment = commentService.saveComment(userDetailsImpl, comment, idNews);
            CommentDto savedCommentDto = commentService.convertToCommentDto(savedComment);
            return new ResponseEntity<>(savedCommentDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{idComment}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> userDeleteComment(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                        @PathVariable("idComment") Integer idComment) {
        try {
            commentService.deleteComment(userDetailsImpl, idComment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/{idComment}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> adminDeleteComment(@PathVariable("idComment") Integer idComment) {
        try {
            commentRepository.deleteById(idComment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
