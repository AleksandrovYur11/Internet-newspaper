package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Like;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.User;
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
    }

    public void deleteLike(News news, User user) {
        Optional<Like> optionalLike= likesRepositories.findLikeByNewsAndUser(news, user);
        if (optionalLike.isPresent()){
            Like like = optionalLike.get();
            likesRepositories.delete(like);
        } else {
            log.error("Like not found");
            throw new EntityNotFoundException("Like not found");
        }
    }

    public List<Like> findAll(){
        return likesRepositories.findAll();
    }

    public List<Like> findLikeByNews(News news){
        return likesRepositories.findLikeByNews(news);
    }
}
