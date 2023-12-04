package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email = " + email + ": Not Found");
                    return new EntityNotFoundException("User with email = " + email + ": Not Found");
                });
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                            log.error("User with id = " + userId + ": Not Found");
                            return new EntityNotFoundException("User with id = " + userId + ": Not Found");
                        });
    }
}
