package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "like", schema = "public")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;
}
