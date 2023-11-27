package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class SignupRequest {

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
