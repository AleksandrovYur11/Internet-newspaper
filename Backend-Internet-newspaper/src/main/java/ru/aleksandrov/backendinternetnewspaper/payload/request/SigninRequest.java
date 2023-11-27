package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    private String email;
    private String password;
}
