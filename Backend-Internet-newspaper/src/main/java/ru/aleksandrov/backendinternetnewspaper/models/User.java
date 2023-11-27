package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name could be not empty")
    private String name;

    @NotEmpty(message = "Surname could be not empty")
    private String surname;

    @NotEmpty(message = "Email could be not empty")
    @Column(name = "email", unique = true)
    @Email(message = "Write this line as an email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name =  "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "password", unique = true)
    @NotEmpty(message = "Password could be not empty")
    private String password;

    @OneToMany(mappedBy = "authorComment")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Like> likes;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
}
