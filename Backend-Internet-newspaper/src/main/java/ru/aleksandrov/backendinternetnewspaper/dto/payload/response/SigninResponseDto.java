package ru.aleksandrov.backendinternetnewspaper.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SigninResponseDto {
    private String accessToken;
    private String refreshToken;
    private Integer id;
    private String name;
    private List<String> roles;
}
