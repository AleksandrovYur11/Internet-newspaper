package ru.aleksandrov.backendinternetnewspaper.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne
    @JoinTable(name =  "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Role role;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "The password must contain uppercase and lowercase characters, as well as a number from 0 to 9")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "authorComment")
    @OnDelete(action = OnDeleteAction.CASCADE)

    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)

    private List<Like> likes;

    @OneToOne(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RefreshToken refreshToken;
}
