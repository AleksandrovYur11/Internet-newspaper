package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.NewsRepository;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final MappingUtil mappingUtil;

    @Autowired
    public NewsService(NewsRepository newsRepository, MappingUtil mappingUtil) {
        this.newsRepository = newsRepository;
        this.mappingUtil = mappingUtil;
    }

    public News findById(Integer idNews) {
        Optional<News> optionalNews = newsRepository.findById(idNews);
        if (optionalNews.isPresent()) {
            return optionalNews.get();
        } else {
            log.error("News not found");
            throw new EntityNotFoundException("News not found");
        }
    }

    public void saveNews(NewsDto newNewsDto) {
        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        newsRepository.save(News.builder()
                .newsTitle(newNewsDto.getNewsText())
                .newsText(newNewsDto.getNewsText())
                .timePublishedNewsMsk(moscowTimeNow)
                .picture(mappingUtil.convertToPicture(newNewsDto.getPicture()))
                .theme(newNewsDto.getThemes().stream().map(mappingUtil::convertToTheme)
                        .collect(Collectors.toSet()))
                .build());
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public List<News> findAllPublishedNewsInLastTwentyFourHours(LocalDateTime twentyFourHoursAgo) {
        return newsRepository.findAllPublishedNewsInLastTwentyFourHours(twentyFourHoursAgo);
    }

    public void deleteNewsById(Integer idNews) {
        newsRepository.delete(findById(idNews));
    }

    public void updateNews(Integer idNews, NewsDto updatedNewsDto) {
        News news = findById(idNews);

        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        news.setNewsText(updatedNewsDto.getNewsText());
        news.setNewsTitle(updatedNewsDto.getNewsTitle());

        news.setComments(updatedNewsDto.getComments().stream().map(mappingUtil::convertToComment)
                .collect(Collectors.toList()));

        news.setLikes(updatedNewsDto.getLikes().stream().map(mappingUtil::convertToLike)
                .collect(Collectors.toList()));

        news.setPicture(mappingUtil.convertToPicture(updatedNewsDto.getPicture()));
        news.setTimePublishedNewsMsk(moscowTimeNow);

        news.setTheme(updatedNewsDto.getThemes().stream().map(mappingUtil::convertToTheme)
                .collect(Collectors.toSet()));
        news.setId(idNews);
        newsRepository.save(news);

    }

    public News getNewsById(int idNews) {
        return findById(idNews);
    }

    public NewsDto convertToNewsDto(News news) {
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
        return newsDTO;
    }


}
