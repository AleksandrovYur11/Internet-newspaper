package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.*;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @Size(min = 2, message = "Surname must be at least 2 characters long")
    private String surname;

    @Email(message = "Write this line as an email")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "The password must contain at least 8 characters, as well as uppercase and lowercase characters, " +
                    "as well as a number from 0 to 9")
    private String password;
}
