package ru.aleksandrov.backendinternetnewspaper.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Integer id;

    private LocalDateTime datePublishedComment;
    
    @Column(length = 100)
    @NotBlank(message = "Text news could be not empty")
    private String textComment;

    private UserDto user;

}

