package com.opencourse.chapter.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.opencourse.chapter.dtos.ChapterDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.opencourse.chapter.repos.*;
import com.opencourse.chapter.entities.*;
import com.opencourse.chapter.dtos.*;
import com.opencourse.chapter.exceptions.ChapterNotFoundException;
import com.opencourse.chapter.exceptions.ElementNotFoundException;
import com.opencourse.chapter.exceptions.ChapterAlreadyFinishedException;
import com.opencourse.chapter.exceptions.ChapterAlreadyUnFinishedException;

public class ChapterServiceTest {
    ChapterRepo chRepo=mock(ChapterRepo.class);
    ElementRepo elRepo=mock(ElementRepo.class);
    FinishedChapterRepo fcRepo=mock(FinishedChapterRepo.class);

    ChapterService service=new ChapterService(chRepo,elRepo,fcRepo);
    
    Element e;
    Chapter c;

    
    @BeforeEach
    public void init(){
        e=new Element();
        c=new Chapter();
        c.setDescription("description");
        c.setElements(List.of(e));
        c.setFinishedChapters(new ArrayList<>());
        c.setId(1L);
        c.setSectionId(2L);
        c.setTitle("title");
        c.setVideoUrl("videoUrl");
        e.setChapter(c);
        e.setId(1L);
        e.setMarkdownContent("markdownContent");
        e.setTitle("title");
    }
    
    
    @Test
    @DisplayName("get chapter by id test")
    public void getChapterByIdTest(){

        //given 
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));

        //whene
        ChapterDto cd=service.getChapterById(1L);

        //thene
        assertEquals(c.getDescription(),cd.getDescription());

    }


    @Test
    @DisplayName("get chapter by id error test")
    public void getChapterBySectionError()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ChapterNotFoundException.class,()->{
            service.getChapterById(1L);
        });
    }


    @Test
    @DisplayName("update chapter test")
    public void updateChapterErrorTest()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ChapterNotFoundException.class,()->{
            service.updateChapter(ChapterDto.fromChapter(c));
        });
    }


    @Test
    @DisplayName("update chapter test")
    public void updateChapterTest()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        service.updateChapter(ChapterDto.fromChapter(c));
        Mockito.verify(chRepo).flush();
    }

    @Test
    @DisplayName("update element error test")
    public void updateElementError(){
        when(elRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ElementNotFoundException.class,()->{
            service.updateElement(ElementDto.fromElement(e));
        });
    }
    @Test
    @DisplayName("update element test")
    public void updateElementTest()throws Exception{
        when(elRepo.findById(1L)).thenReturn(Optional.of(e));

        service.updateElement(ElementDto.fromElement(e));

        Mockito.verify(elRepo).flush();
    }



    @Test
    @DisplayName("delete chapter by id error test")
    public void DeleteChapterErrorTest(){
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ChapterNotFoundException.class,()->{
            service.deleteChapterById(1L);
        });
    }

    @Test
    @DisplayName("delete chapter by id test")
    public void shouldDeleteChapter(){
        Element e1=new Element();
        e1.setId(1L);
        Element e2=new Element();
        e2.setId(2L);
        Chapter c=new Chapter();
        c.setDescription("description");
        c.setId(1L);
        c.setElements(List.of(e1,e2));
        
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));

        service.deleteChapterById(1L);

        Mockito.verify(chRepo).deleteById(1L);
    }



    @Test
    @DisplayName("mark chapter as unfinished test")
    public void shoudDeleteFromFinishedChapters(){

        Chapter c1=new Chapter();
        c1.setId(1L);
        c1.setTitle("title");
        FinishedChapter fc=new FinishedChapter();
        fc.setChapter(c1);
        fc.setId(1L);
        fc.setUserId(15L);

        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.of(fc));
        when(chRepo.findById(1L)).thenReturn(Optional.of(c1));
        //when
        service.markChapterAsUnFinished(15L, 1L);

        //thene
        Mockito.verify(fcRepo).deleteById(1L);

    }

    @Test
    @DisplayName("mark chapter as finished test")
    public void shouldAddToFinishedChapters(){
        Chapter c1=new Chapter();
        c1.setId(1L);
        c1.setTitle("title");

        when(chRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.empty());

        //when
        service.markChapterAsFinished(15L,1L);

        //then
        FinishedChapter fc=new FinishedChapter();
        fc.setChapter(c1);
        fc.setUserId(15L);
        Mockito.verify(fcRepo).save(fc);
    }

    @Test 
    @DisplayName("mark chapter as fnished error test")
    public void shouldThrowChapterNotFoundException(){
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ChapterNotFoundException.class,()->{
            service.markChapterAsFinished(15L, 1L);
        });  
        assertEquals("chapter with id : 1 not found", exception.getMessage());      
    }

    @Test
    @DisplayName("mark chapter as finished error test")
    public void shouldThrowChapterAlreadyFinishedException(){
        Chapter c=new Chapter();
        c.setId(1L);
        c.setTitle("title");
        
        FinishedChapter fc=new FinishedChapter();
        fc.setId(45L);
        fc.setChapter(c);
        fc.setUserId(15L);

        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.of(fc));
        
        Exception exception=assertThrows(ChapterAlreadyFinishedException.class,()->{
            service.markChapterAsFinished(15L, 1L);
        });
        assertEquals("chapter with id : 1 already finished",exception.getMessage());
    }
    
    
    @Test
    @DisplayName("Mark chapter as unfinished error test")
    public void shouldThrowChapterAlreadyUnFinishedException(){
        Chapter c=new Chapter();
        c.setId(1L);
        c.setTitle("title");
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(ChapterAlreadyUnFinishedException.class,()->{
            service.markChapterAsUnFinished(15L, 1L);
        });
        assertEquals("chapter with id : 1 already not finished", exception.getMessage());
    }

}
