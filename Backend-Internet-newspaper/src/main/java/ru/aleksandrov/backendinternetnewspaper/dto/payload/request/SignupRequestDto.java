package ru.aleksandrov.backendinternetnewspaper.dto.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @Size(min = 2, message = "The name should have a minimum length of 2 characters")
    private String name;
    @Size(min = 2, message = "The surname should have a minimum length of 2 characters")
    private String surname;
    @Email(message = "Please enter a valid email address")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "The password must contain at least 8 characters, including both uppercase and lowercase letters, " +
                    "as well as at least one numeric digit from 0 to 9")
    private String password;

    @Override
    public String toString() {
        return "SignupRequestDto{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
