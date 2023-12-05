package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.Like;
import ru.aleksandrov.backendinternetnewspaper.model.News;
import ru.aleksandrov.backendinternetnewspaper.model.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.LikesRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LikesService {

    private final LikesRepositories likesRepositories;

    @Autowired
    public LikesService(LikesRepositories likesRepositories) {
        this.likesRepositories = likesRepositories;
    }

    public void saveLike(News news, User user) {
        Like like = new Like();
        like.setNews(news);
        like.setUser(user);
        likesRepositories.save(like);
        log.info("Save new like from user (email) = " + user.getEmail() + " for news = " + news.getNewsTitle() + ": Success");
    }

    public void deleteLike(News news, User user) {
        Like like = likesRepositories.findLikeByNewsAndUser(news, user)
                .orElseThrow(() -> {
                    log.error("Like with user (email) and news title = " + user.getEmail() + " "
                            + news.getNewsTitle() + ": Not Found");
                    return new EntityNotFoundException("Like with user (email) and news title = " + user.getEmail() + " "
                            + news.getNewsTitle() + ": Not Found");
                });
        likesRepositories.delete(like);
        log.info("Delete like with user (email) and news title = " + user.getEmail() + " "
                + news.getNewsTitle() + ": Not Found");
    }

    public List<Like> findAll() {
        return likesRepositories.findAll();
    }

    public List<Like> findLikeByNews(News news) {
        return likesRepositories.findLikeByNews(news);
    }
}
