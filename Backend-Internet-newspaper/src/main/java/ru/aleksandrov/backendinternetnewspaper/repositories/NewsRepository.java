package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Transactional
    Optional<News> findById(Integer newsId);

    @Transactional
    @Query(value = "SELECT * FROM news WHERE date_published_news >= NOW() - INTERVAL '1 day' " +
            "ORDER BY date_published_news DESC",
            nativeQuery = true)
    List<News> findNewsInLastTwentyFourHours();

    @Transactional
    @Query("SELECT DISTINCT n FROM news n JOIN n.theme t " +
            "WHERE t IN :favoritesThemes AND n NOT IN " +
            "(SELECT x FROM news x JOIN x.theme t2 WHERE t2 IN :forbiddenThemes)")
    List<News> findNewsByThemes(
            @Param("favoritesThemes") Set<Theme> favoritesThemes,
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

    @Transactional
    @Query("SELECT DISTINCT n FROM news n JOIN n.theme t WHERE t IN :favoritesThemes ")
    List<News> findNewsByFavoriteThemes(
            @Param("favoritesThemes") Set<Theme> favoritesThemes);

    @Transactional
    @Query("SELECT DISTINCT n FROM news n " +
            "WHERE n NOT IN (SELECT DISTINCT x FROM news x JOIN x.theme t WHERE t IN :forbiddenThemes)")
    List<News> findNewsByForbiddenThemes(
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);
}
