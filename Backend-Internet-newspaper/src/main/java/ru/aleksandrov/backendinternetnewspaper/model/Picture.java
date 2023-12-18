package ru.aleksandrov.backendinternetnewspaper.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "picture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class  Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @URL(message = "URL picture must be format URL" )
    @NotBlank(message = "The URL should not be empty")
    private String url;

    @OneToMany(mappedBy = "picture")
    private List<News> news;
}
