package ru.aleksandrov.backendinternetnewspaper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.LikesService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/likes")
public class LikesController {
    private final UserRepository userRepository;
    private final NewsService newsService;
    private final LikesService likesService;
    private final UserService userService;

    @Autowired
    public LikesController(UserRepository userRepository, NewsService newsService,
                           LikesService likesService, UserService userService) {
        this.userRepository = userRepository;
        this.newsService = newsService;
        this.likesService = likesService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                     @RequestParam("newsId") int newsId) {
        try {
            likesService.saveLike(newsService.getNewsById(newsId),
                    userService.getUserById(userDetailsImpl.getId()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                            @RequestParam("newsId") int newsId) {
        try {
            likesService.deleteLike(newsService.getNewsById(newsId),
                    userService.getUserById(userDetailsImpl.getId()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
