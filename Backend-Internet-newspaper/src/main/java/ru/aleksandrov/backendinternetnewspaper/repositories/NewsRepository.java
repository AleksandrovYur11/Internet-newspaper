package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.News;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Transactional
    Optional<News> findByNewsTitle(String title);

    @Transactional
    @Query("SELECT n FROM news n WHERE n.datePublishedNews >= :twentyFourHoursAgo")
    List<News> findAllPublishedInLast24Hours(Date twentyFourHoursAgo);
}
