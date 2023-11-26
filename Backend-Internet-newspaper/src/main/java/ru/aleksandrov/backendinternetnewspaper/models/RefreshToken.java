package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity(name = "RefreshToken")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @Column(unique = true)
    @NotBlank(message = "Refresh token could be not empty")
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
