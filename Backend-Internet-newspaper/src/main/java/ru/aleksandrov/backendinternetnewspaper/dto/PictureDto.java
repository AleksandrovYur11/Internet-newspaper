package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureDto {

    @NotBlank(message = "URL picture could be not empty")
    private String url;
}
