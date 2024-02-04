package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.model.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.NewsRequestDto;
import ru.aleksandrov.backendinternetnewspaper.services.impl.CommentServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.NewsServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/news")
public class NewsController {

    private final NewsServiceImpl newsServiceImpl;
    private final MappingUtil mappingUtil;
    private final CommentServiceImpl commentServiceImpl;

    @Autowired
    public NewsController(NewsServiceImpl newsServiceImpl, MappingUtil mappingUtil, CommentServiceImpl commentServiceImpl) {
        this.newsServiceImpl = newsServiceImpl;
        this.mappingUtil = mappingUtil;
        this.commentServiceImpl = commentServiceImpl;
    }

    @GetMapping("/fresh-news")
    @Cacheable(value = "freshNewsCache", key = "'freshNews'")
    public ResponseEntity<List<NewsDto>> getAllNewsAtTwentyFourHours() {
        List<NewsDto> newsListDto = newsServiceImpl.getNewsInLastTwentyFourHours().stream()
                .map(newsServiceImpl::convertToNewsDto).collect(Collectors.toList());
        commentServiceImpl.clearLoadedCommentsCountMap();
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

        List<NewsDto> newsListDto = newsServiceImpl.getNewsByThemes(favoriteThemes, forbiddenThemes);
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNews(@RequestBody @Valid NewsDto newNewsDto) {
        News savedNews = newsServiceImpl.saveNews(newNewsDto);
        NewsDto savedNewsDto = newsServiceImpl.convertToNewsDto(savedNews);
        return new ResponseEntity<>(savedNewsDto, HttpStatus.CREATED);
    }

    @GetMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("newsId") Integer newsId) {
        News news = newsServiceImpl.getNewsById(newsId);
        NewsDto editNewsDto = newsServiceImpl.convertToNewsDto(news);
        return new ResponseEntity<>(editNewsDto, HttpStatus.OK);
    }

    @PutMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNews(@PathVariable("newsId") Integer newsId,
                                        @RequestBody @Valid NewsDto updatedNewsDto) {
        newsServiceImpl.updateNews(newsId, updatedNewsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable("newsId") Integer newsId) {
        newsServiceImpl.deleteNewsById(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}