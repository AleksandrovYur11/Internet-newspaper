package ru.aleksandrov.backendinternetnewspaper.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aleksandrov.backendinternetnewspaper.dto.ThemeDto;

import java.util.List;

@Getter
@Setter
public class NewsRequest {
    List<ThemeDto> favoritesThemes;
    List<ThemeDto> forbiddenThemes;
}
