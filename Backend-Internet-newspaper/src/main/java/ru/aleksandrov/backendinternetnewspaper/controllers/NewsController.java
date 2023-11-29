package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.dto.ThemeDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.payload.request.NewsRequest;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.ThemeService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Slf4j
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;
    private final ThemeService themeService;
    private final ThemeRepository themeRepository;
    private final MappingUtil mappingUtil;

    @Autowired
    public NewsController(NewsService newsService, ThemeService themeService,
                          ThemeRepository themeRepository, MappingUtil mappingUtil) {
        this.newsService = newsService;
        this.themeService = themeService;
        this.themeRepository = themeRepository;
        this.mappingUtil = mappingUtil;
    }

//    @GetMapping("/news/{id}")
//    public ResponseEntity<News> showNews(@PathVariable Long id) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_MARKDOWN);
//        News news = newsService.findById(id);
////        return  ResponseEntity.ok(news);
//        return  new ResponseEntity<>(newsService.findById(id), headers, HttpStatus.OK);
//
//    }
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm 'MSK'");
//                String resultTimeString = news.getTimePublishedNewsMsk().format(formatter);
//                newsDTO.setTimePublishedNewsMSK(resultTimeString);

    @GetMapping("/fresh-news")
    public ResponseEntity<List<NewsDto>> getAllNewsAtTwentyFourHours() {
        List<NewsDto> newsListDto;
        try {
            LocalDateTime twentyFourHoursAgo = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                    .minus(24, ChronoUnit.HOURS).toLocalDateTime();
            newsListDto = newsService.getNewsInLastTwentyFourHours(twentyFourHoursAgo).stream()
                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
            log.info("Get news that is 24 hours old: Success");
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @GetMapping("/user-themes")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<List<NewsDto>> getNewsByUserThemes(@RequestBody NewsRequest newsRequest) {
//        List<NewsDto> newsListDto;
//        try {
//
//            Set<Theme> themes = newsRequest.getFavoritesThemes().
//                    stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//            Set<Theme> resultThemes = new HashSet<>();
//            for (Theme theme : themes) {
//                if (!themeRepository.findThemeByName(theme.getName()).isPresent()) {
//                    themeRepository.save(theme);
//                    resultThemes.add(theme);
//                } else {
//                    resultThemes.add(themeRepository.findThemeByName(theme.getName()).get());
//                }
//            }
//            news.setTheme(resultThemes);
//
//
//            Set<Theme> setFavoritesThemes = new HashSet<>();
//            Set<Theme> setForbiddenThemes = new HashSet<>();
//
//            for (ThemeDto nameTheme : newsRequest.getFavoritesThemes()) {
//                if (themeRepository.findThemeByName(nameTheme.getName()).isPresent()) {
//
//                }
////                System.out.println(nameTheme);
////                String name = themeService.findByName("Джаонозис").getName();
////                System.out.println(name);
//                setFavoritesThemes.add(themeService.findByName(nameTheme.getName()));
//            }
//
//            for (ThemeDto nameTheme : newsRequest.getForbiddenThemes()) {
//                setForbiddenThemes.add(themeService.findByName(nameTheme.getName()));
//            }
//            newsListDto = newsService.geNewsByUserThemes(setFavoritesThemes, setForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//
//            log.info("Get news with favorite themes: " + favoritesThemes + " and without " + forbiddenThemes + ": Success",
//                    favoritesThemes, forbiddenThemes);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
//    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createNews(@RequestBody NewsDto newNewsDto) {
        try {
            newsService.saveNews(newNewsDto);
            log.info("Create new news title = {}: Success", newNewsDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{idNews}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("idNews") Integer id) {

        try {
            NewsDto editNewsDto = newsService.convertToNewsDto(newsService.getNewsById(id));
            log.info("Get news by id = {}: Success", id);
            return new ResponseEntity<>(editNewsDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idNews}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateNews(@PathVariable("idNews") Integer id,
                                                 @RequestBody NewsDto updatedNewsDto) {
        try {
            newsService.updateNews(id, updatedNewsDto);
            log.info("Update news by id = {}: Success", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idNews}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable("idNews") Integer id) {
        try {
            newsService.deleteNewsById(id);
            log.info("Delete news by id = {}: Success", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
