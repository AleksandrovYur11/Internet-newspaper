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
import java.time.ZoneId;
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
        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        News newNews = new News();
        newNews.setNewsTitle(newNewsDto.getNewsTitle());
        newNews.setNewsText(newNewsDto.getNewsText());
        newNews.setDatePublishedNews(moscowTimeNow);
//        News savedNews = newsRepository.save(News.builder()
//                .newsTitle(newNewsDto.getNewsText())
//                .newsText(newNewsDto.getNewsText())
//                .timePublishedNewsMsk(moscowTimeNow)
////                .picture(mappingUtil.convertToPicture(newNewsDto.getPicture()))
////                .theme(newNewsDto.getThemes().stream().map(mappingUtil::convertToTheme)
////                        .collect(Collectors.toSet()))
//                .build());

//        Picture newPicture = mappingUtil.convertToPicture(newNewsDto.getPicture());
//        if (pictureRepository.findByUrl(newPicture.getUrl()).isPresent()) {
//            savedNews.setPicture(mappingUtil.convertToPicture(newNewsDto.getPicture()));
//        } else {
//            pictureRepository.save(newPicture);
//            savedNews.setPicture(newPicture);
//        }
//
//        Set<Theme> themes = newNewsDto.getThemes().stream()
//                .map(mappingUtil::convertToTheme).collect(Collectors.toSet());
//        Set<Theme> dbThemes = new HashSet<>();
//        for (Theme theme : themes) {
//            if (!themeRepository.findThemeByName(theme.getName()).isPresent()) {
//                themeRepository.save(theme);
//                dbThemes.add(theme);
//            } else {
//                dbThemes.add(themeRepository.findThemeByName(theme.getName()).get());
//            }
//        }
//        savedNews.setTheme(dbThemes);
        setPictureAndThemesForNews(newNews, newNewsDto.getPicture(), newNewsDto.getThemes());
        newsRepository.save(newNews);
        log.info("Create new news by title = {}: Success", newNews.getNewsTitle());
        return newNews;
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public List<News> getNewsInLastTwentyFourHours(LocalDateTime twentyFourHoursAgo) {
        return newsRepository.findNewsInLastTwentyFourHours(twentyFourHoursAgo);
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
        Set<Theme> dbFavoriteThemes = new HashSet<>();
        Set<Theme> dbForbiddenThemes = new HashSet<>();

        if (!favoriteThemes.isEmpty() && !forbiddenThemes.isEmpty()) {
            dbFavoriteThemes = themeService.getThemesFromDb(favoriteThemes);
            dbForbiddenThemes = themeService.getThemesFromDb(forbiddenThemes);
            newsListDto = newsRepository.findNewsByThemes(dbFavoriteThemes, dbForbiddenThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        } else if (!favoriteThemes.isEmpty()) {
            dbFavoriteThemes = themeService.getThemesFromDb(favoriteThemes);
            newsListDto = newsRepository.findNewsByFavoriteThemes(dbFavoriteThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        } else if (!forbiddenThemes.isEmpty()) {
            dbForbiddenThemes = themeService.getThemesFromDb(forbiddenThemes);
            newsListDto = newsRepository.findNewsByForbiddenThemes(dbForbiddenThemes).stream()
                    .map(this::convertToNewsDto).collect(Collectors.toList());
        }
        return newsListDto;
    }

//    public List<News> getNewsByFavoriteThemes(Set<Theme> favoriteThemes) {
//        return newsRepository.findNewsByFavoriteThemes(favoriteThemes, favoriteThemes.size());
//    }
//
//    public List<News> getNewsByForbiddenThemes(Set<Theme> forbiddenThemes) {
//        return newsRepository.findNewsByForbiddenThemes(forbiddenThemes);
//    }


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
