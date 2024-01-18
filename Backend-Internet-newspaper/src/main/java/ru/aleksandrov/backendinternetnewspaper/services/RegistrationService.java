package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.models.User;

public interface RegistrationService {
    User register(User user);
}
