package ru.aleksandrov.backendinternetnewspaper.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.model.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.dto.model.PictureDto;
import ru.aleksandrov.backendinternetnewspaper.dto.model.ThemeDto;
import ru.aleksandrov.backendinternetnewspaper.models.Like;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Picture;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;
import ru.aleksandrov.backendinternetnewspaper.repositories.NewsRepository;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;

import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MappingUtil mappingUtil;
    private final ThemeServiceImpl themeServiceImpl;
    private final PictureServiceImpl pictureServiceImpl;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, MappingUtil mappingUtil,
                           ThemeServiceImpl themeServiceImpl, PictureServiceImpl pictureServiceImpl) {
        this.newsRepository = newsRepository;
        this.mappingUtil = mappingUtil;
        this.themeServiceImpl = themeServiceImpl;
        this.pictureServiceImpl = pictureServiceImpl;
    }

    @Override
    public News getNewsById(Integer newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> {
                    log.error("News with id = " + newsId + ": Not Found");
                    return new EntityNotFoundException("News with id = " + newsId + ": Not Found");
                });
    }

    @Override
    public News saveNews(NewsDto newNewsDto) {
        News newNews = new News();
        newNews.setNewsTitle(newNewsDto.getNewsTitle());
        newNews.setNewsText(newNewsDto.getNewsText());
        newNews.setDatePublishedNews(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        setPictureAndThemesForNews(newNews, newNewsDto.getPicture(), newNewsDto.getThemes());
        newsRepository.save(newNews);
        log.info("Save new news by title = {}: Success", newNews.getNewsTitle());
        return newNews;
    }

    @Override
    public List<News> getNewsInLastTwentyFourHours() {
        return newsRepository.findNewsInLastTwentyFourHours();
    }

    @Override
    public void deleteNewsById(Integer newsId) {
        newsRepository.deleteById(newsId);
        log.info("Delete news by id = {}: Success", newsId);
    }

    @Override
    public void updateNews(Integer newsId, NewsDto updatedNewsDto) {
        News news = getNewsById(newsId);
        news.setNewsTitle(updatedNewsDto.getNewsTitle());
        news.setNewsText(updatedNewsDto.getNewsText());
        setPictureAndThemesForNews(news, updatedNewsDto.getPicture(), updatedNewsDto.getThemes());
        newsRepository.save(news);
        log.info("Update news by title = {}: Success", news.getNewsTitle());
    }

    @Override
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

    @Override
    public List<NewsDto> getNewsByThemes(Set<Theme> favoriteThemes, Set<Theme> forbiddenThemes) {
        List<NewsDto> newsListDto = new ArrayList<>();
        Set<Theme> dbFavoriteThemes = themeServiceImpl.getThemesFromDb(favoriteThemes);
        Set<Theme> dbForbiddenThemes = themeServiceImpl.getThemesFromDb(forbiddenThemes);

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
        Picture savedPicture = pictureServiceImpl.savePicture(picture);
        news.setPicture(savedPicture);

        Set<Theme> themes = themesDto.stream()
                .map(mappingUtil::convertToTheme).collect(Collectors.toSet());
        Set<Theme> savedThemes = themeServiceImpl.saveThemes(themes);
        news.setTheme(savedThemes);
    }
}
