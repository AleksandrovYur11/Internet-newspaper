package ru.aleksandrov.backendinternetnewspaper.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import ru.aleksandrov.backendinternetnewspaper.model.Comment;
import ru.aleksandrov.backendinternetnewspaper.model.Like;
import ru.aleksandrov.backendinternetnewspaper.model.Picture;
import ru.aleksandrov.backendinternetnewspaper.model.Theme;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

    private Integer id;
    @NotBlank(message = "Tile news must be not empty")
    private String newsTitle;

    @NotBlank(message = "Text news must be not empty")
    private String newsText;

    @PastOrPresent(message = "The publication date must be before or in now present time")
    private LocalDateTime timePublishedNewsMSK;

    private List<LikeDto> likes;

    @Valid
    private PictureDto picture;
    @Valid
    private Set<ThemeDto> themes;
}

