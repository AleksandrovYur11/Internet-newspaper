package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.Theme;
import ru.aleksandrov.backendinternetnewspaper.repositories.ThemeRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Set<Theme> getThemesFromDb(Set<Theme> themes){
        Set<Theme> dbThemes = new HashSet<>();
        for (Theme theme : themes) {
            Optional<Theme> themeOptional = themeRepository.findThemeByName(theme.getName());
            themeOptional.ifPresent(dbThemes::add);
        }
        return dbThemes;
    }

    public Set<Theme> saveThemes(Set<Theme> themes){
        Set<Theme> savedThemes = new HashSet<>();
        for (Theme theme : themes) {
            Optional<Theme> dbTheme = themeRepository.findThemeByName(theme.getName());
            if (!dbTheme.isPresent()) {
                themeRepository.save(theme);
                savedThemes.add(theme);
            } else {
                savedThemes.add(dbTheme.get());
            }
        }
        return savedThemes;
    }

//    public Theme findByName(String themeName) {
//        Optional<Theme> themeOptional = themeRepository.findThemeByName(themeName);
//        if (themeOptional.isPresent()) {
//            return themeOptional.get();
//        } else {
//            log.error("Theme with this " + themeName + " not found");
//            throw new EntityNotFoundException("Theme with this " + themeName + " not found");
//        }
//    }
//
//    public Theme findByName(Integer themeId) {
//        Optional<Theme> themeOptional = themeRepository.findById(themeId);
//        if (themeOptional.isPresent()) {
//            return themeOptional.get();
//        } else {
//            log.error("Theme with this " + themeId + " not found");
//            throw new EntityNotFoundException("Theme with this " + themeId + " not found");
//        }
//    }
}
