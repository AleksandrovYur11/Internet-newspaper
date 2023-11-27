package ru.aleksandrov.backendinternetnewspaper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

    private Integer id;
    @NotBlank(message = "Tile news could be not empty")
    private String newsTitle;

    @NotBlank(message = "Text news could be not empty")
    private String newsText;

    @Past(message = "The publication date must be before the present time")
    private LocalDateTime timePublishedNewsMSK;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommentDto> comments;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LikeDto> likes;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PictureDto picture;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<ThemeDto> themes;
}
