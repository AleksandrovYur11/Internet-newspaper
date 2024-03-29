package ru.aleksandrov.backendinternetnewspaper.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Like;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.LikesRepositories;
import ru.aleksandrov.backendinternetnewspaper.services.LikesService;


import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class LikesServiceImpl implements LikesService {

    private final LikesRepositories likesRepositories;

    @Autowired
    public LikesServiceImpl(LikesRepositories likesRepositories) {
        this.likesRepositories = likesRepositories;
    }

    @Override
    public void saveLike(News news, User user) {
        Like like = new Like();
        like.setNews(news);
        like.setUser(user);
        likesRepositories.save(like);
        log.info("Save new like from user (email) = " + user.getEmail() + " for news (title) = "
                + news.getNewsTitle() + ": Success");
    }

    @Override
    public void deleteLike(News news, User user) {
        Like like = likesRepositories.findLikeByNewsAndUser(news, user)
                .orElseThrow(() -> {
                    log.error("Like with user (email) and news (title) = " + user.getEmail() + " "
                            + news.getNewsTitle() + ": Not Found");
                    return new EntityNotFoundException("Like with user (email) and news (title) = " + user.getEmail() + " "
                            + news.getNewsTitle() + ": Not Found");
                });
        likesRepositories.delete(like);
        log.info("Delete like from user (email) = " + user.getEmail() + " for news (title) = "
                + news.getNewsTitle() + ": Not Found");
    }
}
