package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    @NotBlank(message = "Name could be not empty")
    private String name;

    @NotBlank(message = "Surname could be not empty")
    private String surname;

    @NotBlank(message = "Email could be not empty")
    @Email(message = "Write this line as an email")
    private String email;

    @NotBlank(message = "Password could be not empty")
    private String password;

}
