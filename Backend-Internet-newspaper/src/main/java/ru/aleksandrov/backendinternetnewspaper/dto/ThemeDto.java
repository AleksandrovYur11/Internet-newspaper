package ru.aleksandrov.backendinternetnewspaper.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeDto {
    @Column(unique = true)
    @NotBlank
    private String name;
}
