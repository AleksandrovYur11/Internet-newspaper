package ru.aleksandrov.backendinternetnewspaper.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = "RefreshToken")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @Column(unique = true)
    @NotBlank(message = "Refresh token must be not empty")
    private String token;

    @Column
    private Instant expiryDate;
}
