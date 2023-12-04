package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aleksandrov.backendinternetnewspaper.dto.ThemeDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {
    List<ThemeDto> favoritesThemes;
    List<ThemeDto> forbiddenThemes;
}
