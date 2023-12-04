package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.validation.Valid;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    private Integer id;
    private UserDto user;
}
