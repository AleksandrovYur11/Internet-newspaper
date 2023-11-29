package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.CommentDto;
import ru.aleksandrov.backendinternetnewspaper.dto.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.*;
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

    public List<News> getNewsInLastTwentyFourHours(LocalDateTime twentyFourHoursAgo) {
        return newsRepository.findNewsInLastTwentyFourHours(twentyFourHoursAgo);
    }

    public void deleteNewsById(Integer idNews) {
        newsRepository.delete(findById(idNews));
    }

    public void updateNews(Integer idNews, NewsDto updatedNewsDto) {
        News news = findById(idNews);
        news.setNewsTitle(updatedNewsDto.getNewsTitle());
        news.setNewsText(updatedNewsDto.getNewsText());
//        news.setTimePublishedNewsMsk(moscowTimeNow);
//        news.setComments(updatedNewsDto.getComments());
//        news.setLikes(updatedNewsDto.getLikes());
        Picture newPicture = mappingUtil.convertToPicture(updatedNewsDto.getPicture());
        if (pictureRepository.findByUrl(newPicture.getUrl()).isPresent()) {
            news.setPicture(mappingUtil.convertToPicture(updatedNewsDto.getPicture()));
        } else {
            pictureRepository.save(newPicture);
            news.setPicture(newPicture);
        }

        Set<Theme> themes = updatedNewsDto.getThemes().stream().map(mappingUtil::convertToTheme).collect(Collectors.toSet());
        Set<Theme> resultThemes = new HashSet<>();
        for (Theme theme : themes) {
            if (!themeRepository.findThemeByName(theme.getName()).isPresent()) {
                themeRepository.save(theme);
                resultThemes.add(theme);
            } else {
                resultThemes.add(themeRepository.findThemeByName(theme.getName()).get());
            }
        }
        news.setTheme(resultThemes);
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
        newsDTO.setComments(comments.stream().map((comment) -> {
                    CommentDto commentDto = mappingUtil.convertToCommentDto(comment);
                    commentDto.setUser(mappingUtil.convertToUserDto(comment.getAuthorComment()));
                    return commentDto;
                })
                .collect(Collectors.toList()));

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

    public List<News> geNewsByUserThemes(Set<Theme> favoriteThemes, Set<Theme> forbiddenThemes) {
        return newsRepository.findNewsByUserThemes(favoriteThemes, forbiddenThemes);
    }


}
