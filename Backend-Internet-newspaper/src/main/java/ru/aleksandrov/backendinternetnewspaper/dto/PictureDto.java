package ru.aleksandrov.backendinternetnewspaper.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureDto {
    @NotBlank(message = "URL picture could be not empty")
    private String url;
}
