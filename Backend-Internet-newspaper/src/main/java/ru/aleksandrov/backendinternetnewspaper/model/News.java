package ru.aleksandrov.backendinternetnewspaper.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tile news must be not empty")
    private String newsTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Text news must be not empty")
    private String newsText;

//    @Past(message = "The publication date must be before the present time")
    @PastOrPresent(message = "The publication date must be before or in now present time")
    private LocalDateTime timePublishedNewsMsk;

    @OneToMany(mappedBy = "news")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;

    @OneToMany(mappedBy = "news")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Like> likes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name =  "news_theme",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    @NotNull(message = "News must have at least one theme")
    private Set<Theme> theme = new HashSet<>();

}


