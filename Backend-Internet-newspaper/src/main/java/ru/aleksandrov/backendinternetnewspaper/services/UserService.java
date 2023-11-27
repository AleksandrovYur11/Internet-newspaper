package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.User;
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

    public User findByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public User findById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
}
