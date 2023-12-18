package ru.aleksandrov.backendinternetnewspaper.dto.payload.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {
    private String refreshToken;
}
