package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.Picture;
import ru.aleksandrov.backendinternetnewspaper.repositories.PictureRepository;

import java.util.Optional;

@Service
@Slf4j
public class PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public Picture savePicture(Picture picture){
        Optional<Picture> optionalPicture = pictureRepository.findByUrl(picture.getUrl());
        if (optionalPicture.isPresent()) {
            return optionalPicture.get();
        } else {
            pictureRepository.save(picture);
            log.info("Save new picture by url = {}: Success", picture.getUrl());
            return picture;
        }
    }
}
