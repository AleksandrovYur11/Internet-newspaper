package ru.aleksandrov.backendinternetnewspaper.dto.payload.request;

import lombok.*;
import ru.aleksandrov.backendinternetnewspaper.dto.model.ThemeDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestDto {
    List<ThemeDto> favoritesThemes;
    List<ThemeDto> forbiddenThemes;
}
