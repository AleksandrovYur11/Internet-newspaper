package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {

    private Integer id;
    private UserDto user;
}
