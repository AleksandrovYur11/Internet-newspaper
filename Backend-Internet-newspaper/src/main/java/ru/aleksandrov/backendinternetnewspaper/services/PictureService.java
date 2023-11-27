package ru.aleksandrov.backendinternetnewspaper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Picture;
import ru.aleksandrov.backendinternetnewspaper.repositories.PictureRepository;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class PictureService {

    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public static byte[] downloadPicture(String url) throws IOException {
        URL pictureFile = new URL(url);
        try (InputStream in = pictureFile.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void savePictures(Picture picture) throws IOException {
                picture.setPicture(downloadPicture(picture.getUrl()));
        pictureRepository.save(picture);
    }

    public byte[] getPictureById(Integer idPicture){
        Optional<Picture> optionalPicture = pictureRepository.findPictureById(idPicture);
        if(optionalPicture.isPresent()){
             return optionalPicture.get().getPicture();
        } else {
            log.error("Picture not found");
            throw new EntityNotFoundException("Picture not found");
        }
    }

    public Picture findByNews(News news){
        Optional<Picture> optionalPicture = pictureRepository.findByNews(news);
        if(optionalPicture.isPresent()){
            return optionalPicture.get();
        } else {
            log.error("Picture not found");
            throw new EntityNotFoundException("Picture not found");
        }
    }

}

