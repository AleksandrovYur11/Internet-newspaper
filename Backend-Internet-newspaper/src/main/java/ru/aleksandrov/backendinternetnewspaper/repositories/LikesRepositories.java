package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.model.Like;
import ru.aleksandrov.backendinternetnewspaper.model.News;
import ru.aleksandrov.backendinternetnewspaper.model.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepositories extends JpaRepository<Like, Integer> {
    @Transactional
    Optional<Like> findLikeByNewsAndUser(News news, User user);
}
