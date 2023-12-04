package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.dto.PictureDto;
import ru.aleksandrov.backendinternetnewspaper.dto.ThemeDto;
import ru.aleksandrov.backendinternetnewspaper.model.*;
import ru.aleksandrov.backendinternetnewspaper.repositories.NewsRepository;
import ru.aleksandrov.backendinternetnewspaper.repositories.PictureRepository;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final MappingUtil mappingUtil;
    private final PictureRepository pictureRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository, MappingUtil mappingUtil,
                       PictureRepository pictureRepository, ThemeRepository themeRepository) {
        this.newsRepository = newsRepository;
        this.mappingUtil = mappingUtil;
        this.pictureRepository = pictureRepository;
        this.themeRepository = themeRepository;
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
        newNews.setTimePublishedNewsMsk(moscowTimeNow);
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
    }

    public void updateNews(Integer newsId, NewsDto updatedNewsDto) {
        News news = getNewsById(newsId);
        news.setNewsTitle(updatedNewsDto.getNewsTitle());
        news.setNewsText(updatedNewsDto.getNewsText());
//        news.setTimePublishedNewsMsk(moscowTimeNow);
//        news.setComments(updatedNewsDto.getComments());
//        news.setLikes(updatedNewsDto.getLikes());

        //---------------------------------
//        Picture newPicture = mappingUtil.convertToPicture(updatedNewsDto.getPicture());
//        if (pictureRepository.findByUrl(newPicture.getUrl()).isPresent()) {
//            news.setPicture(mappingUtil.convertToPicture(updatedNewsDto.getPicture()));
//        } else {
//            pictureRepository.save(newPicture);
//            news.setPicture(newPicture);
//        }
//
//        Set<Theme> themes = updatedNewsDto.getThemes().stream()
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
//        news.setTheme(dbThemes);

        setPictureAndThemesForNews(news, updatedNewsDto.getPicture(), updatedNewsDto.getThemes());
        newsRepository.save(news);
    }


    public NewsDto convertToNewsDto(News news) {
        NewsDto newsDTO = mappingUtil.convertToNewsDto(news);

        List<Like> likes = news.getLikes();
        if (!(likes == null)) {
            newsDTO.setLikes(likes.stream().map(mappingUtil::convertToLikeDto)
                    .collect(Collectors.toList()));
        }

//        List<Comment> comments = news.getComments();
//        if (!(comments == null)){
//            newsDTO.setComments(comments.stream().map((comment) -> {
//                        CommentDto commentDto = mappingUtil.convertToCommentDto(comment);
//                        commentDto.setUser(mappingUtil.convertToUserDto(comment.getAuthorComment()));
//                        return commentDto;
//                    })
//                    .collect(Collectors.toList()));
//        }

        Picture picture = news.getPicture();
        newsDTO.setPicture(mappingUtil.convertToPictureDto(picture));

        Set<Theme> themes = news.getTheme();
        newsDTO.setThemes(themes.stream().map(mappingUtil::convertToThemeDto).collect(Collectors.toSet()));
        return newsDTO;
    }

//    public void updateNews(Integer idNews, NewsDto updatedNewsDto) {
//        LocalDateTime moscowTimeNow = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
//        newsRepository.findById(idNews).map(
//                news -> {
//                    news.setNewsTitle(updatedNewsDto.getNewsText());
//                    news.setNewsText(updatedNewsDto.getNewsTitle());
//                    news.setTimePublishedNewsMsk(moscowTimeNow);
//                    news.setPicture(mappingUtil.convertToPicture(updatedNewsDto.getPicture()));
//                    news.setTheme(updatedNewsDto.getThemes().stream().map(mappingUtil::convertToTheme)
//                            .collect(Collectors.toSet()));
//                    News news1 = newsRepository.save(news);
//                    return news;
//                });
//    }

    public List<News> getNewsByUserThemes(Set<Theme> favoriteThemes, Set<Theme> forbiddenThemes) {
        return newsRepository.findNewsByUserThemes(favoriteThemes, forbiddenThemes);
    }

    public List<News> getNewsByFavoriteUserThemes(Set<Theme> favoriteThemes) {
        return newsRepository.findNewsByFavoriteUserThemes(favoriteThemes, favoriteThemes.size());
    }

    public List<News> getNewsByForbiddenUserThemes(Set<Theme> forbiddenThemes) {
        return newsRepository.findNewsByForbiddenUserThemes(forbiddenThemes);
    }


    private void setPictureAndThemesForNews(News news, PictureDto pictureDto, Set<ThemeDto> themesDto) {
        Picture newPicture = mappingUtil.convertToPicture(pictureDto);
        if (pictureRepository.findByUrl(pictureDto.getUrl()).isPresent()) {
            news.setPicture(mappingUtil.convertToPicture(pictureDto));
        } else {
            pictureRepository.save(newPicture);
            news.setPicture(newPicture);
        }

        Set<Theme> themes = themesDto.stream()
                .map(mappingUtil::convertToTheme).collect(Collectors.toSet());
        Set<Theme> dbThemes = new HashSet<>();
        for (Theme theme : themes) {
            if (!themeRepository.findThemeByName(theme.getName()).isPresent()) {
                themeRepository.save(theme);
                dbThemes.add(theme);
            } else {
                dbThemes.add(themeRepository.findThemeByName(theme.getName()).get());
            }
        }
        news.setTheme(dbThemes);
    }

}
