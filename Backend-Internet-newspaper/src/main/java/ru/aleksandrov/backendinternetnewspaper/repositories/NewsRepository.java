package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.aleksandrov.backendinternetnewspaper.dto.ThemeDto;
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

    @Transactional
    @Query("SELECT n FROM news n WHERE n.theme IN :favoritesThemes AND n NOT IN " +
            "(SELECT news FROM news news JOIN news.theme theme WHERE theme IN :forbiddenThemes)")
    List<News> findNewsByUserThemes(@Param("favoritesThemes") Set<Theme> favoritesThemes,
                                    @Param("forbiddenThemes") Set<Theme> forbiddenThemes);


}
