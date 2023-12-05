package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer id;

    private LocalDateTime datePublishedComment;

    @NotBlank(message = "The text of the comment should not be empty")
    private String textComment;

    private UserDto user;

}

