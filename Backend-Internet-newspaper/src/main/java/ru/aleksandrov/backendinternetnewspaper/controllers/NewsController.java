package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Slf4j
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
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

    @GetMapping( "/fresh-news")
    public ResponseEntity<List<NewsDto>> getAllNewsAtTwentyFourHours() {

        LocalDateTime twentyFourHoursAgo = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .minus(24, ChronoUnit.HOURS).toLocalDateTime();
        List<News> newsList = newsService.findAllPublishedNewsInLastTwentyFourHours(twentyFourHoursAgo);
        List<NewsDto> newsDTOList = new ArrayList<>(newsList.size());
        try {
            for (News news : newsList) {
                newsDTOList.add(newsService.convertToNewsDto(news));
            }
            log.info("Get news that is 24 hours old: Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }

    @PostMapping( "/create")
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


    @GetMapping( "/{idNews}")
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

    @PutMapping( "/{idNews}")
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
