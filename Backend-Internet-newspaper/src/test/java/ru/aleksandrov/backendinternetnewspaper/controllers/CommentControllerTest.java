package ru.aleksandrov.backendinternetnewspaper.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aleksandrov.backendinternetnewspaper.repositories.CommentRepository;
import ru.aleksandrov.backendinternetnewspaper.services.CommentService;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;

@ContextConfiguration(classes = {CommentController.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CommentControllerTest {
    @Autowired
    private CommentController commentController;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MappingUtil mappingUtil;
    /**
     * Method under test: {@link CommentController#adminDeleteComment(Integer)}
     */
    @Test
    public void testAdminDeleteComment() throws Exception {
        doNothing().when(commentService).deleteCommentById((Integer) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/comment/admin/{commentId}", 123);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(commentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CommentController#checkExistComment(Integer)}
     */
    @Test
    public void testCheckExistComment() throws Exception {
        when(commentService.getCountComments((Integer) any())).thenReturn(3);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/comment/check-db");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("newsId", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link CommentController#getCommentsForNews(Integer)}
     */
    @Test
    public void testGetCommentsForNews() throws Exception {
        when(commentService.getThreeComments((Integer) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/comment/show");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("newsId", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}

