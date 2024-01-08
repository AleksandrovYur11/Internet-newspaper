package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.model.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.dto.model.PictureDto;
import ru.aleksandrov.backendinternetnewspaper.dto.model.ThemeDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.NewsRepository;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final MappingUtil mappingUtil;
    private final ThemeService themeService;
    private final PictureService pictureService;

    @Autowired
    public NewsService(NewsRepository newsRepository, MappingUtil mappingUtil,
                       ThemeService themeService, PictureService pictureService) {
        this.newsRepository = newsRepository;
        this.mappingUtil = mappingUtil;
        this.themeService = themeService;
        this.pictureService = pictureService;
    }

    public News getNewsById(Integer newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> {
                    log.error("News with id = " + newsId + ": Not Found");
                    return new EntityNotFoundException("News with id = " + newsId + ": Not Found");
                });
    }

    public News saveNews(NewsDto newNewsDto) {
        News newNews = new News();
        newNews.setNewsTitle(newNewsDto.getNewsTitle());
        newNews.setNewsText(newNewsDto.getNewsText());
        newNews.setDatePublishedNews(LocalDateTime.now());
        setPictureAndThemesForNews(newNews, newNewsDto.getPicture(), newNewsDto.getThemes());
        newsRepository.save(newNews);
        log.info("Create new news by title = {}: Success", newNews.getNewsTitle());
        return newNews;
    }

    public List<News> getNewsInLastTwentyFourHours() {
        return newsRepository.findNewsInLastTwentyFourHours();
    }

    public void deleteNewsById(Integer newsId) {
        newsRepository.deleteById(newsId);
        log.info("Delete news by id = {}: Success", newsId);
    }

    public void updateNews(Integer newsId, NewsDto updatedNewsDto) {
        News news = getNewsById(newsId);
        news.setNewsTitle(updatedNewsDto.getNewsTitle());
        news.setNewsText(updatedNewsDto.getNewsText());
        setPictureAndThemesForNews(news, updatedNewsDto.getPicture(), updatedNewsDto.getThemes());
        newsRepository.save(news);
        log.info("Update news by title = {}: Success", news.getNewsTitle());
    }

    public NewsDto convertToNewsDto(News news) {
        NewsDto newsDTO = mappingUtil.convertToNewsDto(news);

        List<Like> likes = news.getLikes();
        if (!(likes == null)) {
            newsDTO.setLikes(likes.stream().map(mappingUtil::convertToLikeDto)
                    .collect(Collectors.toList()));
        }
        Picture picture = news.getPicture();
        newsDTO.setPicture(mappingUtil.convertToPictureDto(picture));
        Set<Theme> themes = news.getTheme();
        newsDTO.setThemes(themes.stream().map(mappingUtil::convertToThemeDto).collect(Collectors.toSet()));
        return newsDTO;
    }

    public List<NewsDto> getNewsByThemes(Set<Theme> favoriteThemes, Set<Theme> forbiddenThemes) {
        List<NewsDto> newsListDto = new ArrayList<>();
        Set<Theme> dbFavoriteThemes = themeService.getThemesFromDb(favoriteThemes);
        Set<Theme> dbForbiddenThemes = themeService.getThemesFromDb(forbiddenThemes);

        if (!dbFavoriteThemes.isEmpty() && !dbForbiddenThemes.isEmpty()) {
            newsListDto = newsRepository.findNewsByThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        } else if (!dbFavoriteThemes.isEmpty()) {
            newsListDto = newsRepository.findNewsByFavoriteThemes(dbFavoriteThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        } else if (!dbForbiddenThemes.isEmpty()) {
            newsListDto = newsRepository.findNewsByForbiddenThemes(dbForbiddenThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        }
        return newsListDto;
    }

    private void setPictureAndThemesForNews(News news, PictureDto pictureDto, Set<ThemeDto> themesDto) {
        Picture picture = mappingUtil.convertToPicture(pictureDto);
        Picture savedPicture = pictureService.savePicture(picture);
        news.setPicture(savedPicture);

        Set<Theme> themes = themesDto.stream()
                .map(mappingUtil::convertToTheme).collect(Collectors.toSet());
        Set<Theme> savedThemes = themeService.saveThemes(themes);
        news.setTheme(savedThemes);
    }
}