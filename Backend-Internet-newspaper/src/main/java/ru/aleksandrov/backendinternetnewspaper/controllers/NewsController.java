package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.model.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.NewsRequestDto;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;
    private final MappingUtil mappingUtil;
    private final CommentService commentService;

    @Autowired
    public NewsController(NewsService newsService, MappingUtil mappingUtil, CommentService commentService) {
        this.newsService = newsService;
        this.mappingUtil = mappingUtil;
        this.commentService = commentService;
    }

    @GetMapping("/fresh-news")
    public ResponseEntity<List<NewsDto>> getAllNewsAtTwentyFourHours() {
        List<NewsDto> newsListDto = newsService.getNewsInLastTwentyFourHours().stream()
                .map(newsService::convertToNewsDto).collect(Collectors.toList());
        commentService.clearLoadedCommentsCountMap();
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/user-themes")
    public ResponseEntity<List<NewsDto>> getNewsByUserThemes(@RequestBody NewsRequestDto newsRequestDto) {
        Set<Theme> favoriteThemes = newsRequestDto.getFavoritesThemes() != null ?
                newsRequestDto.getFavoritesThemes().stream()
                        .map(mappingUtil::convertToTheme)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        Set<Theme> forbiddenThemes = newsRequestDto.getForbiddenThemes() != null ?
                newsRequestDto.getForbiddenThemes().stream()
                        .map(mappingUtil::convertToTheme)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        List<NewsDto> newsListDto = newsService.getNewsByThemes(favoriteThemes, forbiddenThemes);
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNews(@RequestBody @Valid NewsDto newNewsDto) {
        News savedNews = newsService.saveNews(newNewsDto);
        NewsDto savedNewsDto = newsService.convertToNewsDto(savedNews);
        return new ResponseEntity<>(savedNewsDto, HttpStatus.CREATED);
    }

    @GetMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("newsId") Integer newsId) {
        News news = newsService.getNewsById(newsId);
        NewsDto editNewsDto = newsService.convertToNewsDto(news);
        return new ResponseEntity<>(editNewsDto, HttpStatus.OK);
    }

    @PutMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNews(@PathVariable("newsId") Integer newsId,
                                        @RequestBody @Valid NewsDto updatedNewsDto) {
        newsService.updateNews(newsId, updatedNewsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable("newsId") Integer newsId) {
        newsService.deleteNewsById(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}