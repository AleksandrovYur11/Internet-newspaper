package ru.aleksandrov.backendinternetnewspaper.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.*;
import ru.aleksandrov.backendinternetnewspaper.models.*;


@Service
public class MappingUtil {

    private final ModelMapper modelMapper;


    @Autowired
    public MappingUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NewsDto convertToNewsDTO(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    public CommentDto convertToCommentDTO(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    public Comment convertToComment(CommentDto commentDTO){
        return modelMapper.map(commentDTO, Comment.class);
    }

    public LikeDto convertToLikeDTO(Like like) {
        return modelMapper.map(like, LikeDto.class);
    }
    public Like convertToLike(LikeDto likeDto) {
        return modelMapper.map(likeDto, Like.class);
    }


    public PictureDto convertToPictureDTO(Picture picture){
        return  modelMapper.map(picture, PictureDto.class);
    }
    public Picture convertToPicture(PictureDto pictureDto){
        return  modelMapper.map(pictureDto, Picture.class);
    }

    public User convertToUser(UserDto userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    public ThemeDto convertToThemeDto(Theme theme){ return  modelMapper.map(theme, ThemeDto.class);}
    public Theme convertToTheme(ThemeDto themeDto){ return  modelMapper.map(themeDto, Theme.class);}

    public News convertToNews(NewsDto newsDto){ return  modelMapper.map(newsDto, News.class);}

}
