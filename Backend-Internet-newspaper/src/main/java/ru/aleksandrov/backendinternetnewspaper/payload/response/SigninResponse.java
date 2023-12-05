package ru.aleksandrov.backendinternetnewspaper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SigninResponse {
    private String accessToken;
    private String refreshToken;
    private Integer id;
    private String name;
    private List<String> roles;
}
