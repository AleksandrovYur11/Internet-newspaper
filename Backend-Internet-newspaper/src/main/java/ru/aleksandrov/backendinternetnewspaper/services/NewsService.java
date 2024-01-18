package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.dto.model.NewsDto;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.util.List;
import java.util.Set;

public interface NewsService {
    News getNewsById(Integer newsId);
    News saveNews(NewsDto newNewsDto);
    List<News> getNewsInLastTwentyFourHours();
    void deleteNewsById(Integer newsId);
    void updateNews(Integer newsId, NewsDto updatedNewsDto);
    NewsDto convertToNewsDto(News news);
    List<NewsDto> getNewsByThemes(Set<Theme> favoriteThemes, Set<Theme> forbiddenThemes);
}
