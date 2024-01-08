package ru.aleksandrov.backendinternetnewspaper.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeDto {
    @NotBlank(message = "The theme should not be empty")
    private String name;
}
