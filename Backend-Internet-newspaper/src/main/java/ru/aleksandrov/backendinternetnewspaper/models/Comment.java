package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 1000)
    @NotBlank(message = "Text comment news must be not empty")
    private String textComment;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User authorComment;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;
    private LocalDateTime datePublishedComment;
}
