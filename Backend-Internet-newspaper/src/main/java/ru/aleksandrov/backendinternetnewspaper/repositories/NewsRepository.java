package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Transactional
    Optional<News> findByNewsTitle(String title);

    @Transactional
    Optional<News> findById(Integer newsId);

    @Transactional
    @Query(value = "SELECT n FROM news n WHERE n.datePublishedNews >= :twentyFourHoursAgo ORDER BY n.datePublishedNews DESC")
    List<News> findNewsInLastTwentyFourHours(@Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);
    //------------------------------
//    @Transactional
//    @Query("SELECT DISTINCT n FROM news n JOIN n.theme t " +
//            "WHERE t IN :favoritesThemes AND t NOT IN :forbiddenThemes")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

    @Transactional
    @Query("SELECT n FROM news n " +
            "WHERE (EXISTS (SELECT t1 FROM n.theme t1 WHERE t1 IN :favoritesThemes)) AND " +
            "NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes)")
    List<News> findNewsByThemes(
            @Param("favoritesThemes") Set<Theme> favoritesThemes,
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);
//-------------------------------------------

//    @Transactional
//    @Query("SELECT n FROM news n JOIN n.theme t " +
//            "WHERE t IN :favoritesThemes ")
//    List<News> findNewsByFavoriteThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes);
    @Transactional
    @Query("SELECT n FROM news n WHERE n.theme IN :favoritesThemes")
    List<News> findNewsByFavoriteThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes);



    //-----------------------------

    //    @Transactional
//    @Query("SELECT n FROM news n " +
//            "WHERE (EXISTS (SELECT t1 FROM n.theme t1)) AND " +
//            "NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes)")
//    List<News> findNewsByForbiddenUserThemes(
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);
    @Transactional
    @Query("SELECT n FROM news n WHERE n.theme NOT IN :forbiddenThemes")
    List<News> findNewsByForbiddenThemes(
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);
    //---------------------------------
}
