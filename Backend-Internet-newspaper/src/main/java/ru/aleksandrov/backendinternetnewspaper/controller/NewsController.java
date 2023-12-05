package ru.aleksandrov.backendinternetnewspaper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.model.*;
import ru.aleksandrov.backendinternetnewspaper.payload.request.NewsRequest;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.ThemeService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.validation.Valid;
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

        LocalDateTime twentyFourHoursAgo = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .minus(24, ChronoUnit.HOURS).toLocalDateTime();
        List<NewsDto> newsListDto = newsService.getNewsInLastTwentyFourHours(twentyFourHoursAgo).stream()
                .map(newsService::convertToNewsDto).collect(Collectors.toList());
        log.info("Get news that is 24 hours old: Success");
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/user-themes")
    public ResponseEntity<List<NewsDto>> getNewsByUserThemes(@RequestBody NewsRequest newsRequest) {
        List<NewsDto> newsListDto = new ArrayList<>();
        Set<Theme> dbFavoriteThemes = new HashSet<>();
        Set<Theme> dbForbiddenThemes = new HashSet<>();

        Set<Theme> favoritesThemes = newsRequest.getFavoritesThemes() != null ?
                newsRequest.getFavoritesThemes().stream()
                        .map(mappingUtil::convertToTheme)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        Set<Theme> forbiddenThemes = newsRequest.getForbiddenThemes() != null ?
                newsRequest.getForbiddenThemes().stream()
                        .map(mappingUtil::convertToTheme)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        if (!favoritesThemes.isEmpty() && !forbiddenThemes.isEmpty()) {
            dbFavoriteThemes = themeService.getDbThemes(favoritesThemes);
            dbForbiddenThemes = themeService.getDbThemes(forbiddenThemes);
            newsListDto = newsService.getNewsByUserThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
        } else if (!favoritesThemes.isEmpty()) {
            dbFavoriteThemes = themeService.getDbThemes(favoritesThemes);
            newsListDto = newsService.getNewsByFavoriteUserThemes(dbFavoriteThemes).stream()
                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
        } else if (!forbiddenThemes.isEmpty()) {
            dbForbiddenThemes = themeService.getDbThemes(forbiddenThemes);
            newsListDto = newsService.getNewsByForbiddenUserThemes(dbForbiddenThemes).stream()
                    .map(newsService::convertToNewsDto).collect(Collectors.toList());
        }


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
        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
//        return new ResponseEntity<>(newsListDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDto> createNews(@RequestBody @Valid NewsDto newNewsDto) throws IllegalAccessException {
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
                                        @RequestBody @Valid NewsDto updatedNewsDto) throws IllegalAccessException {
        newsService.updateNews(newsId, updatedNewsDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable("newsId") Integer newsId) {
        newsService.deleteNewsById(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
