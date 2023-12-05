package ru.aleksandrov.backendinternetnewspaper.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @Size(min = 2, message = "Surname must be at least 2 characters")
    private String surname;

    @Column(name = "email", unique = true)
    @Email(message = "Write this line as an email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name =  "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "The password must contain uppercase and lowercase characters, as well as a number from 0 to 9")
    @Column(name = "password", unique = true)
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
