package ru.aleksandrov.backendinternetnewspaper.controllers;

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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/likes")
public class LikesController {
    private final UserRepository userRepository;
    private final NewsService newsService;
    private final LikesService likesService;

    @Autowired
    public LikesController(UserRepository userRepository, NewsService newsService, LikesService likesService) {
        this.userRepository = userRepository;
        this.newsService = newsService;
        this.likesService = likesService;
    }

    @PostMapping("/add/{idNews}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                              @PathVariable("idNews") int idNews) {
        try {
            likesService.addLike(newsService.findById(idNews), userRepository.findById(userDetailsImpl.getId()).get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/remove/{idNews}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> userRemoveLike(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                     @PathVariable("idNews") int idNews) {
        try {
            likesService.removeLike(newsService.findById(idNews), userRepository.findById(userDetailsImpl.getId()).get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
