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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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

        LocalDateTime twentyFourHoursAgo = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .minus(24, ChronoUnit.HOURS).toLocalDateTime();
        List<NewsDto> newsListDto = newsService.getNewsInLastTwentyFourHours(twentyFourHoursAgo).stream()
                .map(newsService::convertToNewsDto).collect(Collectors.toList());
        commentService.clearLoadedCommentsCountMap();
        log.info("Get news that is 24 hours old: Success");
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/user-themes")
    public ResponseEntity<List<NewsDto>> getNewsByUserThemes(@RequestBody NewsRequestDto newsRequestDto) {
//        List<NewsDto> newsListDto = new ArrayList<>();
//        Set<Theme> dbFavoriteThemes = new HashSet<>();
//        Set<Theme> dbForbiddenThemes = new HashSet<>();
//
//        Set<Theme> favoritesThemes = newsRequestDto.getFavoritesThemes() != null ?
//                newsRequestDto.getFavoritesThemes().stream()
//                        .map(mappingUtil::convertToTheme)
//                        .collect(Collectors.toSet()) :
//                Collections.emptySet();
//
//        Set<Theme> forbiddenThemes = newsRequestDto.getForbiddenThemes() != null ?
//                newsRequestDto.getForbiddenThemes().stream()
//                        .map(mappingUtil::convertToTheme)
//                        .collect(Collectors.toSet()) :
//                Collections.emptySet();
//
//        if (!favoritesThemes.isEmpty() && !forbiddenThemes.isEmpty()) {
//            dbFavoriteThemes = themeService.getThemesFromDb(favoritesThemes);
//            dbForbiddenThemes = themeService.getThemesFromDb(forbiddenThemes);
//            newsListDto = newsService.getNewsByThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        } else if (!favoritesThemes.isEmpty()) {
//            dbFavoriteThemes = themeService.getThemesFromDb(favoritesThemes);
//            newsListDto = newsService.getNewsByFavoriteThemes(dbFavoriteThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        } else if (!forbiddenThemes.isEmpty()) {
//            dbForbiddenThemes = themeService.getThemesFromDb(forbiddenThemes);
//            newsListDto = newsService.getNewsByForbiddenThemes(dbForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        }


//        Set<Theme> dbFavoriteThemes = new HashSet<>();
//        Set<Theme> dbForbiddenThemes = new HashSet<>();
//        if (newsRequest.getFavoritesThemes() != null) {
//            Set<Theme> favoritesThemes = newsRequest.getFavoritesThemes().
//                    stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//            dbFavoriteThemes = themeService.getDbThemes(favoritesThemes);
//        } else {
//            dbFavoriteThemes = Collections.emptySet();
//        }
//
//        if (newsRequest.getForbiddenThemes() != null) {
//            Set<Theme> forbiddenThemes = newsRequest.getForbiddenThemes().
//                    stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//            dbForbiddenThemes = themeService.getDbThemes(forbiddenThemes);
//        } else {
//            dbForbiddenThemes = Collections.emptySet();
//        }
//
//        if (!dbForbiddenThemes.isEmpty() && !dbFavoriteThemes.isEmpty()) {
//            newsListDto = newsService.getNewsByUserThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        } else if (dbForbiddenThemes.isEmpty() && !dbFavoriteThemes.isEmpty()) {
//            newsListDto = newsService.getNewsByFavoriteUserThemes(dbFavoriteThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        } else if (!dbForbiddenThemes.isEmpty()) {
//            newsListDto = newsService.getNewsByForbiddenUserThemes(dbForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
//        }
//----------------------------

//            Set<Integer> dbFavoriteThemes = new HashSet<>();
//            Set<Integer> dbForbiddenThemes = new HashSet<>();
//            if (newsRequest.getFavoritesThemes() != null) {
//                Set<Theme> favoritesThemes = newsRequest.getFavoritesThemes().
//                        stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//                dbFavoriteThemes = themeService.getIntegerDbThemes(favoritesThemes);
//            } else {
//                dbFavoriteThemes =  Collections.emptySet();
//            }
//
//            if (newsRequest.getForbiddenThemes()!= null) {
//                Set<Theme> forbiddenThemes = newsRequest.getForbiddenThemes().
//                        stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//                dbForbiddenThemes = themeService.getIntegerDbThemes(forbiddenThemes);
//            } else {
//                dbForbiddenThemes =  Collections.emptySet();
//            }
        ;
//
//            Set<Theme> forbiddenThemes = newsRequest.getForbiddenThemes().
//                    stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());

//            if (favoritesThemes == null && forbiddenThemes == null) {
//                // Оба параметра пустые - вернуть пустой список новостей или обработать исключение
//                new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK); // или выбрасывайте исключение или делайте обработку по вашему усмотрению
//            }

//            Set<Theme> dbFavoriteThemes = themeService.getDbThemes(favoritesThemes);
//            Set<Theme> dbForbiddenThemes = themeService.getDbThemes(forbiddenThemes);
//            List<News> list = newsService.geNewsByUserThemes(dbFavoriteThemes, dbForbiddenThemes);
//            List<NewsDto> newsListDto = newsService.geNewsByUserThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
//                    .map(newsService::convertToNewsDto).collect(Collectors.toList());

//           return new ResponseEntity<>(newsService.geNewsByUserThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
//                   .map(newsService::convertToNewsDto).collect(Collectors.toList()))


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

//            log.info("Get news with favorite themes: " + favoritesThemes + " and without " +
//                            "forbidden themes: " + forbiddenThemes + ": Success",
//                    favoritesThemes, forbiddenThemes);

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
//        log.info("Get news by id = {}: Success", newsId);
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
