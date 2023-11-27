package ru.aleksandrov.backendinternetnewspaper.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksandrov.internetnewspaper.models.Comment;
import ru.aleksandrov.internetnewspaper.models.Like;
import ru.aleksandrov.internetnewspaper.models.Picture;


import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    @NotBlank(message = "Tile news could be not empty")
    private String newsTitle;
    @NotBlank(message = "Text news could be not empty")
    private String newsText;
    @Past(message = "The publication date must be before the present time")
    private Date datePublishedNews;
    private List<CommentDto> comments;
    private List<LikeDto> likes;
    private PictureDto picture;
    private Set<ThemeDto> themes;

}
