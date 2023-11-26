package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "URL picture could be not empty")
    private String url;

    @Lob
    private byte[] picture;

    @OneToOne(mappedBy = "picture")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private News news;
}
