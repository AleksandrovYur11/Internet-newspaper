package ru.aleksandrov.backendinternetnewspaper.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.dto.model.*;
import ru.aleksandrov.backendinternetnewspaper.models.*;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SignupRequestDto;


@Service
public class MappingUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public MappingUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NewsDto convertToNewsDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    public CommentDto convertToCommentDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    public Comment convertToComment(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }

    public LikeDto convertToLikeDto(Like like) {
        return modelMapper.map(like, LikeDto.class);
    }

    public Like convertToLike(LikeDto likeDto) {
        return modelMapper.map(likeDto, Like.class);
    }

    public PictureDto convertToPictureDto(Picture picture) {
        return modelMapper.map(picture, PictureDto.class);
    }

    public Picture convertToPicture(PictureDto pictureDto) {
        return modelMapper.map(pictureDto, Picture.class);
    }

    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User convertToUser(SignupRequestDto signupRequestDto) {
        return modelMapper.map(signupRequestDto, User.class);
    }

    public ThemeDto convertToThemeDto(Theme theme) {
        return modelMapper.map(theme, ThemeDto.class);
    }

    public Theme convertToTheme(ThemeDto themeDto) {
        return modelMapper.map(themeDto, Theme.class);
    }

    public News convertToNews(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }
}
