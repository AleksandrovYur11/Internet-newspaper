package ru.aleksandrov.backendinternetnewspaper.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private Integer id;
    @NotBlank(message = "The title of the news should not be empty")
    private String newsTitle;
    @NotBlank(message = "The text of the news should not be empty")
    private String newsText;
    private LocalDateTime datePublishedNews;
    @Valid
    private List<LikeDto> likes;
    @Valid
    private PictureDto picture;
    @Valid
    private Set<ThemeDto> themes;
}

