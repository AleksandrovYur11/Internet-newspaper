package ru.aleksandrov.backendinternetnewspaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.security.services.impl.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.LikesServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.NewsServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.UserServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/likes")
public class LikesController {

    private final NewsServiceImpl newsServiceImpl;
    private final LikesServiceImpl likesServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public LikesController(NewsServiceImpl newsServiceImpl, LikesServiceImpl likesServiceImpl,
                           UserServiceImpl userServiceImpl) {
        this.newsServiceImpl = newsServiceImpl;
        this.likesServiceImpl = likesServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> saveLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                     @RequestParam("newsId") int newsId) {
        User user = userServiceImpl.getUserById(userDetailsImpl.getId());
        News news = newsServiceImpl.getNewsById(newsId);
        likesServiceImpl.saveLike(news, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                        @RequestParam("newsId") int newsId) {
        News news = newsServiceImpl.getNewsById(newsId);
        User user = userServiceImpl.getUserById(userDetailsImpl.getId());
        likesServiceImpl.deleteLike(news, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
