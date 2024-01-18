package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.User;

public interface LikesService {
    void saveLike(News news, User user);
    void deleteLike(News news, User user);
}
