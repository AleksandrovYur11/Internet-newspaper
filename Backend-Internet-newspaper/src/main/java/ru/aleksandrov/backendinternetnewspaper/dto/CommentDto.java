package ru.aleksandrov.backendinternetnewspaper.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @Column(length = 100)
    @NotBlank(message = "Text news could be not empty")
    private String textComment;

}

