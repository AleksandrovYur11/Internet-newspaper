package ru.aleksandrov.backendinternetnewspaper.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.services.LikesService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.services.PictureService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;


import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Slf4j
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;
    private final MappingUtil mappingUtil;
    private final CommentService commentService;
    private final LikesService likesService;
    private final PictureService pictureService;

    @Autowired
    public NewsController(NewsService newsService, MappingUtil mappingUtil, CommentService commentService,
                          LikesService likesService, PictureService pictureService) {
        this.newsService = newsService;
        this.mappingUtil = mappingUtil;
        this.commentService = commentService;
        this.likesService = likesService;
        this.pictureService = pictureService;
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

    @GetMapping("/fresh")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NewsDto>> getAllNewsAtTwentyFourHours() {

        LocalDateTime twentyFourHoursAgo = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .minus(24, ChronoUnit.HOURS).toLocalDateTime();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm 'MSK'");
//                String resultTimeString = news.getTimePublishedNewsMsk().format(formatter);
//                newsDTO.setTimePublishedNewsMSK(resultTimeString);

        List<News> newsList = newsService.findAllPublishedNewsInLastTwentyFourHours(twentyFourHoursAgo);
        List<NewsDto> newsDTOList = new ArrayList<>(newsList.size());
        try {
            for (News news : newsList) {
                NewsDto newsDTO = mappingUtil.convertToNewsDto(news);
                List<Like> likes = news.getLikes();

                newsDTO.setLikes(likes.stream().map(mappingUtil::convertToLikeDto)
                        .collect(Collectors.toList()));

                List<Comment> comments = news.getComments();
                newsDTO.setComments(comments.stream().map(mappingUtil::convertToCommentDto)
                        .collect(Collectors.toList()));

                Picture picture = news.getPicture();
                newsDTO.setPicture(mappingUtil.convertToPictureDto(picture));

                Set<Theme> themes = news.getTheme();
                newsDTO.setThemes(themes.stream().map(mappingUtil::convertToThemeDto).collect(Collectors.toSet()));
                newsDTOList.add(newsDTO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }


    @GetMapping("edit/{idNews}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("idNews") Integer id) {

        try {
            NewsDto newsDto = newsService.getNewsById(id);
            log.info("get 1 news: success");
            return new ResponseEntity<>(newsDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @PutMapping("edit/{idNews}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateNews(@PathVariable("idNews") Integer id, @RequestBody NewsDto updatedNews) {
        try {
            News news = newsService.findById(id);

            ZoneId moscowZone = ZoneId.of("Europe/Moscow");

            // Получение текущей даты и времени для Москвы
            LocalDateTime moscowTime = LocalDateTime.now(moscowZone);

            // Форматирование даты и времени
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm MSK");
            String formattedMoscowTime = moscowTime.format(formatter);

//            News convertNews = mappingUtil.convertToNews(updatedNews);
            news.setNewsText(updatedNews.getNewsText());
            news.setNewsTitle(updatedNews.getNewsTitle());
            news.setComments(updatedNews.getComments().stream().map(mappingUtil::convertToComment)
                    .collect(Collectors.toList()));
            news.setLikes(updatedNews.getLikes().stream().map(mappingUtil::convertToLike)
                    .collect(Collectors.toList()));
            news.setPicture(mappingUtil.convertToPicture(updatedNews.getPicture()));
            news.setTimePublishedNewsMsk(moscowTime);
            news.setTheme(updatedNews.getThemes().stream().map(mappingUtil::convertToTheme)
                    .collect(Collectors.toSet()));

//            List<Like> likes = news.getLikes();
//            newsDTO.setLikes(likes.stream().map(mappingUtil::convertToLikeDTO).toList());
//
//            List<Comment> comments = news.getComments();
//            newsDTO.setComments(comments.stream().map(mappingUtil::convertToCommentDTO).toList());
//
//            Picture picture = news.getPicture();
//            newsDTO.setPicture(mappingUtil.convertToPictureDTO(picture));
//            log.info("get 1 news: success");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
