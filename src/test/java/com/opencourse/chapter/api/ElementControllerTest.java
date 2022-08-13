package com.opencourse.chapter.api;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.opencourse.chapter.services.*;
import com.opencourse.chapter.entities.*;
import com.opencourse.chapter.handlers.CustomResponseEntityExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencourse.chapter.dtos.ElementDto;


@WebMvcTest(ElementController.class)
public class ElementControllerTest {
    private String basePath="/api/v1/element";
    private ElementDto ed;
    private ObjectMapper mapper;
    @InjectMocks
    private CustomResponseEntityExceptionHandler uderTestHandler;
    @MockBean
    ChapterService service;
    @Autowired
    MockMvc mvc;
    Element e;
    Chapter c;
    @BeforeEach
    public void init(){
        mapper=new ObjectMapper();
        ed=new ElementDto();
        ed.setChapterId(1L);
        ed.setMarkdownContent("markdownContent");
        ed.setTitle("title");
    }
    @Test
    @DisplayName("update element error (not valid) test")
    public void updateElementNotValidTest() throws Exception{
        Map<String,Object> body=new HashMap<String,Object>();
        body.put("title", "");
        body.put("markdwnContent","content");

        mvc.perform(
            put(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("adding element error (not valid) test")
    public void addElementNotValidTest() throws Exception{
        Map<String,Object> body=new HashMap<String,Object>();
        body.put("title", "");
        body.put("markdwnContent","content");

        mvc.perform(
            post(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("adding element test")
    public void addingElementTest()throws Exception{
        when(service.addElement(ed)).thenReturn(1L);
        Map<String,Object> body=new HashMap<>();
        body.put("title", "title");
        body.put("markdownContent","markdownContent");
        body.put("chapterId","1");
        body.put("id", null);
        
        mvc.perform(
            post(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("deleting element test")
    public void deletingElementTest()throws Exception{
        doAnswer(i->{
            return null;
        }).when(service).deleteElementById(1L);
        mvc.perform(
            delete(basePath + "/1")
            ).andDo(print())
            .andExpect(status().isOk());
        Mockito.verify(service).deleteElementById(1L);
    }

    @Test
    @DisplayName("updating Element Test")
    public void updatingElementTest()throws Exception{
        doAnswer(i->{
            return null;
        }).when(service).updateElement(ed);
        Map<String,Object> body=new HashMap<>();
        body.put("title", "title");
        body.put("markdownContent","markdownContent");
        body.put("chapterId","1");
        body.put("id", 1L);
        
        mvc.perform(
            put(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }
}
