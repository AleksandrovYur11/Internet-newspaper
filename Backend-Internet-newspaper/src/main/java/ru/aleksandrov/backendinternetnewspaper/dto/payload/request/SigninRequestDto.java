package ru.aleksandrov.backendinternetnewspaper.dto.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequestDto {
    @Email(message = "Please enter a valid email address")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "The password must contain at least 8 characters, including both uppercase and lowercase letters, " +
                    "as well as at least one numeric digit from 0 to 9")
    private String password;
}
