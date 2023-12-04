package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}
