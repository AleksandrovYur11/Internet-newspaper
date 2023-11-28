package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme findByName(String nameTheme) {
        Optional<Theme> themeOptional = themeRepository.findThemeByName(nameTheme);
        if (themeOptional.isPresent()) {
            return themeOptional.get();
        } else {
            log.error("Theme with this " + nameTheme + " not found");
            throw new EntityNotFoundException("Theme with this " + nameTheme + " not found");
        }
    }
}
