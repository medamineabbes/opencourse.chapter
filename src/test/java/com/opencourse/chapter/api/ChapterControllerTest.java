package com.opencourse.chapter.api;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencourse.chapter.dtos.ChapterDto;
import com.opencourse.chapter.dtos.ElementDto;
import com.opencourse.chapter.services.*;
@WebMvcTest(ChapterController.class)
public class ChapterControllerTest {
    private String basePath="/api/v1/chapter";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ChapterService service;

    private ElementDto e1;
    private ElementDto e2;
    private ChapterDto c;
    private ChapterDto cld;
    @BeforeEach
    public void init(){
        e1=new ElementDto();
        e1.setMarkdownContent("#markdownContent");
        e1.setTitle("title e1");
        e2=new ElementDto();
        e2.setMarkdownContent("#markdownContent * e2 *");
        e2.setTitle("title e2");
        c=new ChapterDto();
        c.setDescription("description");
        c.setSectionId(1L);
        c.setTitle("title");
        c.setVideoUrl("videoUrl");
        
        
        cld=new ChapterDto();
        cld.setDescription("description");
        cld.setId(1L);
        cld.setSectionId(2L);
        cld.setTitle("title");
        cld.setVideoUrl("videoUrl");
    }
    @AfterEach
    public void reseting(){
        reset(service);
    }


    @Test
    @DisplayName("add Chapter Test 200 status code")
    public void addChapterTest()throws Exception{
        when(service.addChapter(c,15L)).thenReturn(1L);
        
        Map<String,Object> body1=new HashMap<String,Object>();
        body1.put("title","title e1");
        body1.put("markdownContent","#markdownContent");

        Map<String,Object> body2=new HashMap<String,Object>();
        body2.put("title","title e2");
        body2.put("markdownContent","#markdownContent * e2 *");

        List<Map<String,Object>> list=List.of(body1,body2);

        Map<String,Object> mainBody=new HashMap<String,Object>();
        mainBody.put("title","title");
        mainBody.put("videoUrl", "videoUrl");
        mainBody.put("sectionId",1L);
        mainBody.put("description","description");
        mainBody.put("elements", list);

        ObjectMapper mapper=new ObjectMapper();
        mvc.perform(
            post(basePath)
            .content(mapper.writeValueAsString(mainBody))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());


    }

    @Test
    @DisplayName("update chapter test 200 status code")
    public void updateChapterTest()throws Exception{
        
        doAnswer(i->{
            return null;
        }).when(service).updateChapter(cld,15L);

        Map<String,Object> mainBody=new HashMap<String,Object>();
        mainBody.put("title","title");
        mainBody.put("videoUrl", "videoUrl");
        mainBody.put("sectionId",2L);
        mainBody.put("description","description");

        ObjectMapper mapper=new ObjectMapper();
        
        mvc.perform(
            put(basePath)
            .content(mapper.writeValueAsString(mainBody))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete chapter test 200 status code")
    public void deleteChapterTest()throws Exception{
        doAnswer(i->{
            return null;
        }).when(service).deleteChapterById(1L,15L);

        mvc.perform(
            delete(basePath + "/1")
        ).andDo(print())
        .andExpect(status().isOk());

    }

    @Test
    @DisplayName("mark chapter as finished test 200")
    public void markChapterAsFinishedTest()throws Exception{
        doAnswer(i->{
            return null;
        }).when(service).markChapterAsFinished(1L, 15L);

        mvc.perform(
            get(basePath + "/finish/1")
        ).andDo(print())
        .andExpect(status().isOk());
    }
}
