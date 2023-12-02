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
    @Query("SELECT n FROM news n WHERE n.timePublishedNewsMsk >= :twentyFourHoursAgo ORDER BY n.timePublishedNewsMsk DESC")
    List<News> findNewsInLastTwentyFourHours(@Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);

//    @Query("update news c set c.newsTitle = :name WHERE c.id = :customerId")
//    void updateNews(@Param("updatedNews") News id, @Param("name") String name);

//    @Transactional
//    @Query("SELECT n FROM news n WHERE n.theme = :favoritesThemes AND n.theme != :forrbiddenThemes")
//    List<News> findNewsByUserThemes(@Param("favoritesThemes") Theme favoritesThemes,
//                                    @Param("forbiddenThemes") Theme forbiddenThemes);

    //    @Transactional
//    @Query("SELECT n FROM news n WHERE n.theme IN :favoritesThemes AND n NOT IN " +
//            "(SELECT news FROM news n JOIN news.theme theme WHERE theme IN :forbiddenThemes)")
//    List<News> findNewsByUserThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes,
//                                    @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


//    @Transactional
//    @Query("SELECT DISTINCT n FROM news n " +
//            "WHERE n.theme IN :favoritesThemes AND " +
//            "n NOT IN (SELECT n FROM news n JOIN n.theme t WHERE t IN :forbiddenThemes)")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


//    @Transactional
//    @Query("SELECT n FROM news n WHERE (:favoritesThemes IS NULL OR n.theme IN :favoritesThemes) AND " +
//            "(:forbiddenThemes IS NULL OR n.theme NOT IN :forbiddenThemes)")
//    List<News> findNewsByUserThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes,
//                                    @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

//    @Query("SELECT DISTINCT n FROM news n " +
//            "LEFT JOIN n.theme t1 " +
//            "LEFT JOIN n.theme t2 " +
//            "WHERE ( t1 IN :favoritesThemes  OR n.theme IS EMPTY)  AND " +
//            "( t2 NOT IN :forbiddenThemes  OR n.theme IS EMPTY)")
//    List<News> findNewsByUserThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes,
//                                    @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

//    @Query("SELECT DISTINCT n FROM news n " +
//            "LEFT JOIN n.theme t1 " +
//            "LEFT JOIN n.theme t2 " +
//            "WHERE (t1 IN :favoritesThemes) AND " +
//            "(NOT (t2 IN :forbiddenThemes))")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

    //Ищет так, чтобы не выводил новость, если она содержит запретную тему и она вместе с желаемой
//    @Query("SELECT DISTINCT n FROM news n " +
//            "WHERE (EXISTS (SELECT t1 FROM n.theme t1 WHERE t1 IN :favoritesThemes) " +
//            "AND NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes))")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


    //    @Query("SELECT DISTINCT n FROM news n " +
//            "WHERE (EXISTS (SELECT t1 FROM n.theme t1 WHERE CASE WHEN :favoritesThemes != NULL Then t1 IN :favoritesThemes ELSE (SELECT t1 FROM n.theme t1)) " +
//            "AND NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes))")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);
    @Transactional
    @Query("SELECT DISTINCT n FROM news n " +
            "WHERE (EXISTS (SELECT t1 FROM n.theme t1 WHERE t1 IN :favoritesThemes)) AND " +
            "NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes)")
    List<News> findNewsByUserThemes(
            @Param("favoritesThemes") Set<Theme> favoritesThemes,
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);

    @Transactional
    @Query("SELECT DISTINCT n FROM news n " +
            "WHERE (EXISTS (SELECT t1 FROM n.theme t1 WHERE t1 IN :favoritesThemes))")
    List<News> findNewsByFavoriteUserThemes(
            @Param("favoritesThemes") Set<Theme> favoritesThemes);
    @Transactional
    @Query("SELECT DISTINCT n FROM news n " +
            "WHERE (EXISTS (SELECT t1 FROM n.theme t1)) AND " +
            "NOT EXISTS (SELECT t2 FROM n.theme t2 WHERE t2 IN :forbiddenThemes)")
    List<News> findNewsByForbiddenUserThemes(
            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);



//    @Query("SELECT DISTINCT n FROM news n " +
//            "WHERE (:favoritesThemes IS NULL OR n IN (SELECT nt FROM news nt JOIN nt.theme t1 WHERE t1 IN :favoritesThemes)) " +
//            "AND (NOT EXISTS (SELECT nft FROM news nft JOIN nft.theme t2 WHERE t2 IN :forbiddenThemes) OR :forbiddenThemes IS NULL)")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemes") Set<Theme> favoritesThemes,
//            @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


//    @Transactional
//    @Query("SELECT DISTINCT n FROM news n " +
//            "LEFT JOIN n.themes t1 " +
//            "LEFT JOIN n.themes t2 " +
//            "WHERE (t1 IN :favoritesThemes OR n.themes IS EMPTY) AND " +
//            "(t2 NOT IN :forbiddenThemes OR n.themes IS EMPTY)")
//    List<News> findNewsByUserThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes,
//                                    @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


    //    @Transactional
//    @Query("SELECT DISTINCT n FROM news n " +
//            "LEFT JOIN n.theme t1 " +
//            "LEFT JOIN n.theme t2 " +
//            "WHERE (t1.id IN :favoritesThemesIds OR n.theme IS EMPTY) AND " +
//            "(t2.id NOT IN :forbiddenThemesIds OR n.theme IS EMPTY)")
//    List<News> findNewsByUserThemes(@Param("favoritesThemesIds") Set<Integer> favoritesThemesIds,
//                                    @Param("forbiddenThemesIds") Set<Integer> forbiddenThemesIds);
//
//    @Transactional
////    @Query("SELECT DISTINCT n FROM news n " +
////            "JOIN n.theme t " +
////            "WHERE (t.id IN :favoritesThemesIds ) AND " +
////            "n.id NOT IN (SELECT n.id FROM news n JOIN n.theme ft WHERE ft.id IN :forbiddenThemesIds)")
//
//    @Query("SELECT DISTINCT n FROM news n " +
//            "JOIN n.theme t " +
//            "WHERE (t.id IN :favoritesThemesIds) AND " +
//            "(t.id NOT IN :forbiddenThemesIds)")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemesIds") Set<Integer> favoritesThemesIds,
//            @Param("forbiddenThemesIds") Set<Integer> forbiddenThemesIds
//    );
//
//    @Transactional
//    @Query("SELECT DISTINCT n FROM news n " +
//            "JOIN n.theme t " +
//            "WHERE t.id IN :favoritesThemesIds " +
//            "AND n NOT IN (SELECT n FROM news n JOIN n.theme ft WHERE ft.id IN :forbiddenThemesIds)")
//    List<News> findNewsByUserThemes(
//            @Param("favoritesThemesIds") Set<Integer> favoritesThemesIds,
//            @Param("forbiddenThemesIds") Set<Integer> forbiddenThemesIds);

}
