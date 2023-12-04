package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PictureDto {
    @URL(message = "URL picture must be format URL" )
    private String url;
}
