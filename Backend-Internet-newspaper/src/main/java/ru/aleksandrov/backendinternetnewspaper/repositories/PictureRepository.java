package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.models.Picture;


import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    @Transactional
    Optional<Picture> findById(Integer id);
    @Transactional
    Optional<Picture> findByUrl(String url);
}
