package ru.aleksandrov.backendinternetnewspaper.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SignupRequestDto;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;

@Component
public class UserValidator implements Validator {

    private final MappingUtil mappingUtil;
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(MappingUtil mappingUtil, UserRepository userRepository) {
        this.mappingUtil = mappingUtil;
        this.userRepository = userRepository;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return SignupRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupRequestDto signupRequestDto = (SignupRequestDto) target;
        User user = mappingUtil.convertToUser(signupRequestDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "","User with email " + user.getEmail() +" already exists");
        }
    }
}
