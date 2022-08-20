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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.opencourse.chapter.repos.*;
import com.opencourse.chapter.entities.*;
import com.opencourse.chapter.dtos.*;
import com.opencourse.chapter.exceptions.ChapterNotFoundException;
import com.opencourse.chapter.exceptions.ElementNotFoundException;
import com.opencourse.chapter.externalservices.CourseService;
import com.opencourse.chapter.exceptions.ChapterAlreadyFinishedException;
import com.opencourse.chapter.exceptions.ChapterAlreadyUnFinishedException;

public class ChapterServiceTest {
    ChapterRepo chRepo=mock(ChapterRepo.class);
    ElementRepo elRepo=mock(ElementRepo.class);
    FinishedChapterRepo fcRepo=mock(FinishedChapterRepo.class);
    CourseService courseService=mock(CourseService.class);

    ChapterService service=new ChapterService(chRepo,elRepo,fcRepo,courseService);
    
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
    @DisplayName("should return chapter")
    public void getChapterByIdTest(){

        //given 
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(courseService.userHasAccessToSection(c.getSectionId(), 15L)).thenReturn(true);

        //whene
        ChapterDto cd=service.getChapterById(1L,15L);

        //thene
        assertEquals(c.getDescription(),cd.getDescription());

    }

    @Test
    @DisplayName("should throw chapter not found exception")
    public void getChapterBySectionError()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class,()->{
            service.getChapterById(1L,15L);
        });
    }

    @Test
    @DisplayName("should throw chapter not found exception")
    public void updateChapterErrorTest()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class,()->{
            service.updateChapter(ChapterDto.fromChapter(c),15L);
        });
    }

    @Test
    @DisplayName("should update chapter")
    public void updateChapterTest()throws Exception{
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(courseService.userCreatedSection(c.getSectionId(), 15L)).thenReturn(true);
        service.updateChapter(ChapterDto.fromChapter(c),15L);
        Mockito.verify(chRepo).flush();
    }

    @Test
    @DisplayName("should throw element not found exception")
    public void updateElementError(){
        when(elRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ElementNotFoundException.class,()->{
            service.updateElement(ElementDto.fromElement(e),15L);
        });
    }
    
    @Test
    @DisplayName("should update element")
    public void updateElementTest()throws Exception{
        when(elRepo.findById(1L)).thenReturn(Optional.of(e));
        when(courseService.userCreatedSection(e.getChapter().getSectionId(), 15L)).thenReturn(true);

        service.updateElement(ElementDto.fromElement(e),15L);

        Mockito.verify(elRepo).flush();
    }

    @Test
    @DisplayName("should throw chapter not found exception")
    public void DeleteChapterErrorTest(){
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class,()->{
            service.deleteChapterById(1L,15L);
        });
    }

    @Test
    @DisplayName("should delete chapter")
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
        when(courseService.userCreatedSection(c.getSectionId(), 15L)).thenReturn(true);

        service.deleteChapterById(1L,15L);

        Mockito.verify(chRepo).deleteById(1L);
    }

    @Test
    @DisplayName("should mark chapter as unfinished")
    public void unFinishChapterTest(){

        Chapter c1=new Chapter();
        c1.setId(1L);
        c1.setTitle("title");
        c1.setSectionId(2L);

        FinishedChapter fc=new FinishedChapter();
        fc.setChapter(c1);
        fc.setId(1L);
        fc.setUserId(15L);

        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.of(fc));
        when(chRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(courseService.userHasAccessToSection(c1.getSectionId(), 15L)).thenReturn(true);
        //when
        service.markChapterAsUnFinished( 1L,15L);

        //thene
        Mockito.verify(fcRepo).deleteById(1L);

    }

    @Test
    @DisplayName("should Add To Finished Chapters")
    public void finishChapterTest(){
        Chapter c1=new Chapter();
        c1.setId(1L);
        c1.setTitle("title");
        c1.setSectionId(2L);
        when(chRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.empty());
        when(courseService.userHasAccessToSection(c1.getSectionId(), 15L)).thenReturn(true);

        //when
        service.markChapterAsFinished(1L,15L);

        //then
        FinishedChapter fc=new FinishedChapter();
        fc.setChapter(c1);
        fc.setUserId(15L);
        Mockito.verify(fcRepo).save(fc);
    }

    @Test 
    @DisplayName("should Throw Chapter Not Found Exception")
    public void markChapterAsFinishedTest(){
        when(chRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class,()->{
            service.markChapterAsFinished( 1L,15L);
        });       
    }

    @Test
    @DisplayName("shouldThrowChapterAlreadyFinishedException")
    public void markChapterAsFinishedErrorTest(){
        Chapter c=new Chapter();
        c.setId(1L);
        c.setTitle("title");
        c.setSectionId(44L);
        FinishedChapter fc=new FinishedChapter();
        fc.setId(45L);
        fc.setChapter(c);
        fc.setUserId(15L);

        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.of(fc));
        when(courseService.userHasAccessToSection(c.getSectionId(), 15L)).thenReturn(true);

        assertThrows(ChapterAlreadyFinishedException.class,()->{
            service.markChapterAsFinished( 1L,15L);
        });
    }    
    
    @Test
    @DisplayName("should Throw Chapter Already UnFinished Exception")
    public void markchapterAsUnFinishedErrorTest(){

        Chapter c=new Chapter();
        c.setId(1L);
        c.setTitle("title");
        c.setSectionId(155L);
        when(chRepo.findById(1L)).thenReturn(Optional.of(c));
        when(fcRepo.findByUserIdAndChapterId(15L, 1L)).thenReturn(Optional.empty());
        when(courseService.userHasAccessToSection(c.getSectionId(), 15L)).thenReturn(true);
        
        assertThrows(ChapterAlreadyUnFinishedException.class,()->{
            service.markChapterAsUnFinished( 1L,15L);
        });
    }

    @Test
    @DisplayName("should return false")
    public void validSectionsTest(){
        Chapter c1=new Chapter();
        Chapter c2=new Chapter();
        Chapter c3=new Chapter();
        Chapter c4=new Chapter();
        Chapter c5=new Chapter();
        Element e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        e1=new Element();e2=new Element();e3=new Element();e4=new Element();e5=new Element();e6=new Element();e7=new Element();e8=new Element();e9=new Element();e10=new Element();
        c1.setElements(List.of(e1,e2));
        c2.setElements(List.of(e3,e4));
        c3.setElements(List.of(e5,e6));
        c4.setElements(List.of(e7,e8));
        c5.setElements(List.of(e9,e10));
        
        when(chRepo.findBySectionId(1L)).thenReturn(List.of(c1,c2));
        when(chRepo.findBySectionId(2L)).thenReturn(List.of(c3,c4));
        when(chRepo.findBySectionId(3L)).thenReturn(List.of(c5)); 

        boolean valid=service.validSections(List.of(1L,2L,3L));
        assertFalse(valid);
    }

    @Test
    @DisplayName("should return true")
    public void validSectionsTestsuccess(){
        Chapter c1=new Chapter();
        Chapter c2=new Chapter();
        Chapter c3=new Chapter();
        Chapter c4=new Chapter();
        Element e1,e2,e3,e4,e5,e6,e7,e8;
        e1=new Element();e2=new Element();e3=new Element();e4=new Element();e5=new Element();e6=new Element();e7=new Element();e8=new Element();
        c1.setElements(List.of(e1,e2));
        c2.setElements(List.of(e3,e4));
        c3.setElements(List.of(e5,e6));
        c4.setElements(List.of(e7,e8));
        
        when(chRepo.findBySectionId(1L)).thenReturn(List.of(c1,c2));
        when(chRepo.findBySectionId(2L)).thenReturn(List.of(c3,c4));

        boolean valid=service.validSections(List.of(1L,2L));
        assertTrue(valid);
    }

    
}
