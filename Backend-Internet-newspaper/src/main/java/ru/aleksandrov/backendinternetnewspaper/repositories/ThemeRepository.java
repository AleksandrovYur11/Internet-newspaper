package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    Optional<Theme> findThemeByName(String name);
}
