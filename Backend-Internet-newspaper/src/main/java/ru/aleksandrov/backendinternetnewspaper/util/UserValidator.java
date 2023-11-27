package ru.aleksandrov.backendinternetnewspaper.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.aleksandrov.backendinternetnewspaper.dto.UserDto;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.payload.request.SignupRequest;
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
        return SignupRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupRequest signupRequest = (SignupRequest) target;
        User user = mappingUtil.convertToUser(signupRequest);


        if (userRepository.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "409", "User with email " + user.getEmail() +" already exists");

        }

//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            Map<String, String> errorMap = new HashMap<>();
//            errorMap.put("email", "This email is already taken");
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                String errorMessage = objectMapper.writeValueAsString(errorMap);
////                objectMapper.writeValue("email", errorMessage)
//                errors.rejectValue("email", "", errorMessage);
//            } catch (JsonProcessingException e) {
//                // Обработка ошибки при преобразовании в JSON
//                e.printStackTrace();
//                // Можно также добавить стандартное сообщение об ошибке, если не удается преобразовать в JSON
//                errors.rejectValue("email", "", "This email is already taken");
//            }
//        }
    }
}
