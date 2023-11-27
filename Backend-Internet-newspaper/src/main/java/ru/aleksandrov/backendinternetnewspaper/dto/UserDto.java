package ru.aleksandrov.backendinternetnewspaper.dto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ru.aleksandrov.internetnewspaper.models.Comment;
import ru.aleksandrov.internetnewspaper.models.Like;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

//    @NotBlank(message = "Name could be not empty")
    private String name;
//    @NotBlank(message = "Surname could be not empty")
    private String surname;
//    @NotBlank(message = "Email could be not empty")
//    @Email(message = "Write this line as an email")
    private String email;
//    @NotBlank(message = "Password could be not empty")
    private String password;

}
