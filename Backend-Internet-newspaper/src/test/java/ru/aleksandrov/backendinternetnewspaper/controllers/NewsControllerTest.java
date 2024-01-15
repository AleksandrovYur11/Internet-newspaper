package ru.aleksandrov.backendinternetnewspaper.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.NewsRequestDto;
import ru.aleksandrov.backendinternetnewspaper.models.News;
import ru.aleksandrov.backendinternetnewspaper.models.Picture;
import ru.aleksandrov.backendinternetnewspaper.models.Theme;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.services.NewsService;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

@ContextConfiguration(classes = {NewsController.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class NewsControllerTest {
    @MockBean
    private CommentService commentService;

    @MockBean
    private MappingUtil mappingUtil;

    @Autowired
    private NewsController newsController;

    @MockBean
    private NewsService newsService;

    /**
     * Method under test: {@link NewsController#getNewsById(Integer)}
     */
    @Test
    public void testGetNewsById() throws Exception {
        Picture picture = Picture.builder()
                .id(1)
                .url("https://example.org/example.jpg")
                .build();

        News news = News.builder()
                .id(123)
                .newsText("News Text")
                .newsTitle("Test")
                .datePublishedNews(LocalDateTime.now())
                .picture(picture)
                .build();

        when(newsService.getNewsById((Integer) any())).thenReturn(news);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/news/{newsId}", 123);
        MockMvcBuilders.standaloneSetup(newsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link NewsController#deleteNews(Integer)}
     */
    @Test
    public void testDeleteNews() throws Exception {
        doNothing().when(newsService).deleteNewsById((Integer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/news/{newsId}", 123);
        MockMvcBuilders.standaloneSetup(newsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link NewsController#getAllNewsAtTwentyFourHours()}
     */
    @Test
    public void testGetAllNewsAtTwentyFourHours() throws Exception {
        when(newsService.getNewsInLastTwentyFourHours()).thenReturn(new ArrayList<>());
        doNothing().when(commentService).clearLoadedCommentsCountMap();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/news/fresh-news");
        MockMvcBuilders.standaloneSetup(newsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link NewsController#getNewsByUserThemes(NewsRequestDto)}
     */
    @Test
    public void testGetNewsByUserThemes() throws Exception {
        when(newsService.getNewsByThemes((Set<Theme>) any(), (Set<Theme>) any())).thenReturn(new ArrayList<>());

        NewsRequestDto newsRequestDto = new NewsRequestDto();
        newsRequestDto.setFavoritesThemes(new ArrayList<>());
        newsRequestDto.setForbiddenThemes(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(newsRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/news/user-themes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(newsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

