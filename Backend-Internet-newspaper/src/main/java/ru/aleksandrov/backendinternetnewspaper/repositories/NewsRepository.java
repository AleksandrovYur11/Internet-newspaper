package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.News;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Transactional
    Optional<News> findByNewsTitle(String title);

    @Transactional
    @Query("SELECT n FROM news n WHERE n.timePublishedNewsMsk >= :twentyFourHoursAgo ORDER BY n.timePublishedNewsMsk DESC")
    List<News> findAllPublishedNewsInLastTwentyFourHours(@Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);

//    @Query("update news c set c.newsTitle = :name WHERE c.id = :customerId")
//    void updateNews(@Param("updatedNews") News id, @Param("name") String name);

}
