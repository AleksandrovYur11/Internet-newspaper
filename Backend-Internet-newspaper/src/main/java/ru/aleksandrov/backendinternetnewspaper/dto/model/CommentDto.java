package ru.aleksandrov.backendinternetnewspaper.dto.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;
    private LocalDateTime datePublishedComment;
    @NotBlank(message = "The text of the comment should not be empty")
    private String textComment;
    @Valid
    private UserDto user;
}

