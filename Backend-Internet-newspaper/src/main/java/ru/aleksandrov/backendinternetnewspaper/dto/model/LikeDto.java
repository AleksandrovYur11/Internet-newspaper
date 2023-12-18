package ru.aleksandrov.backendinternetnewspaper.dto.model;

import lombok.*;

import javax.validation.Valid;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private Integer id;
    @Valid
    private UserDto user;
}
