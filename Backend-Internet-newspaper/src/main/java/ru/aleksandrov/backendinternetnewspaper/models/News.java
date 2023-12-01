package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
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

    @NotBlank(message = "Tile news could be not empty")
    private String newsTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Text news could be not empty")
    private String newsText;

//    @Past(message = "The publication date must be before the present time")
    private LocalDateTime timePublishedNewsMsk;

    @OneToMany(mappedBy = "news")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Comment> comments;

    @OneToOne
    private Picture picture;

    @OneToMany(mappedBy = "news")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Like> likes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name =  "news_theme",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private Set<Theme> theme = new HashSet<>();

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsText='" + newsText + '\'' +
                ", timePublishedNewsMsk=" + timePublishedNewsMsk +
                ", comments=" + comments +
                ", picture=" + picture +
                ", likes=" + likes +
                ", theme=" + theme +
                '}';
    }
}


