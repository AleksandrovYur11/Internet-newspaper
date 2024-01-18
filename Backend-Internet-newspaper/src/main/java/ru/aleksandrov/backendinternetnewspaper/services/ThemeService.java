package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.util.Set;

public interface ThemeService {
    Set<Theme> getThemesFromDb(Set<Theme> themes);
    Set<Theme> saveThemes(Set<Theme> themes);
}
