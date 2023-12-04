package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequest {
    @Email(message = "Write this line as an email")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "The password must contain at least 8 characters, as well as uppercase and lowercase characters, " +
                    "as well as a number from 0 to 9")
    private String password;
}
