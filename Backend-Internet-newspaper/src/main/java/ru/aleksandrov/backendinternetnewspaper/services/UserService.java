package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.models.User;

public interface UserService {
    User getUserById(Integer userId);
}

