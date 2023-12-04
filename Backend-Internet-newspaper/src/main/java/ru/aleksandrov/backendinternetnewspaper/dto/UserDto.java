package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @Size(min = 2, message = "Surname must be at least 2 characters")
    private String surname;

    @Email(message = "Write this line as an email")
    private String email;

}
