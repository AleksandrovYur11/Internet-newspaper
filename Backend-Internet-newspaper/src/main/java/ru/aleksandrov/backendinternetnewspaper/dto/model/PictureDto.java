package ru.aleksandrov.backendinternetnewspaper.dto.model;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PictureDto {
    @URL(message = "The picture URL should be in a valid URL format" )
    @NotBlank(message = "The URL should not be empty")
    private String url;
}
