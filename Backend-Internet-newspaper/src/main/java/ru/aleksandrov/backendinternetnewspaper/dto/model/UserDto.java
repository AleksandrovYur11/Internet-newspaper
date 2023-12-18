package ru.aleksandrov.backendinternetnewspaper.dto.model;

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
    @Size(min = 2, message = "The name should have a minimum length of 2 characters")
    private String name;
    @Size(min = 2, message = "The surname should have a minimum length of 2 characters")
    private String surname;
    @Email(message = "Please enter a valid email address")
    private String email;
}
