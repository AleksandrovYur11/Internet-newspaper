package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;

import java.util.Optional;
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
    Optional<Theme> findThemeByNameIgnoreCase(String name);
}
