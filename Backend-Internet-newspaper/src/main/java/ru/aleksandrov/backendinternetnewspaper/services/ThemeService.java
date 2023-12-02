package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Comment;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    public Set<Theme> getDbThemes(Set<Theme> setThemesWithoutId){
        Set<Theme> dbThemes = new HashSet<>();
        for (Theme theme : setThemesWithoutId) {
            Optional<Theme> themeOptional = themeRepository.findThemeByName(theme.getName());
            themeOptional.ifPresent(dbThemes::add);
        }
        return dbThemes;
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

    public Theme findByName(Integer idTheme) {
        Optional<Theme> themeOptional = themeRepository.findById(idTheme);
        if (themeOptional.isPresent()) {
            return themeOptional.get();
        } else {
            log.error("Theme with this " + idTheme + " not found");
            throw new EntityNotFoundException("Theme with this " + idTheme + " not found");
        }
    }
}
