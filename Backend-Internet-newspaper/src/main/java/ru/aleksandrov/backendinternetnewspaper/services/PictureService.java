package ru.aleksandrov.backendinternetnewspaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.Picture;
import ru.aleksandrov.backendinternetnewspaper.repositories.PictureRepository;

import java.util.Optional;

@Service
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
            return picture;
        }
    }
}
