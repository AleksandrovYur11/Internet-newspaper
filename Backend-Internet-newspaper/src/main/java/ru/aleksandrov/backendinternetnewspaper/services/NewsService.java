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

    public News findById(Integer id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isPresent()) {
            return optionalNews.get();
        } else {
            log.error("News not found");
            throw new EntityNotFoundException("News not found");
        }
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public List<News> findAllPublishedNewsInLastTwentyFourHours(LocalDateTime twentyFourHoursAgo) {
        return newsRepository.findAllPublishedNewsInLastTwentyFourHours(twentyFourHoursAgo);
    }

    public NewsDto getNewsById(int id) {
        News news = findById(id);
        return convertToNewsDto(news);
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
