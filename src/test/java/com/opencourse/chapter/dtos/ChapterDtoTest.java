package com.opencourse.chapter.dtos;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; 
import com.opencourse.chapter.entities.*;
public class ChapterDtoTest {
    @Test
    public void conversionToDtoTest(){

        //given 
        Element e1=new Element();
        e1.setId(1L);
        e1.setMarkdownContent("mark down");
        e1.setTitle("isTitle");
        Element e2=new Element();
        e2.setId(2L);
        e2.setMarkdownContent("mark down 2 ");
        e2.setTitle("isTitle  22kk");

        Chapter chapter =new Chapter();
        chapter.setDescription("description");
        chapter.setElements(List.of(e1,e2));
        chapter.setId(1L);
        chapter.setSectionId(2L);
        chapter.setTitle("title  nu2");
        chapter.setVideoUrl("videoUrl here");
        e1.setChapter(chapter);
        e2.setChapter(chapter);

        // whene
        ChapterDto cd=ChapterDto.fromChapter(chapter);

        //thene
        assertEquals(chapter.getId(),cd.getId());
        assertEquals(chapter.getDescription(),cd.getDescription());
        
        assertEquals(chapter.getSectionId(),cd.getSectionId());
        assertEquals(chapter.getTitle(),cd.getTitle());
        assertEquals(chapter.getVideoUrl(),cd.getVideoUrl());
        assertEquals(chapter.getElements().size(),cd.getElements().size());
        assertEquals(chapter.getElements().get(0).getId(),cd.getElements().get(0).getId());

    }
    @Test
    public void conversionFromDtoTest(){
        //given

        ElementDto ed1=new ElementDto();
        ed1.setId(1L);
        ed1.setMarkdownContent("markdownContent dd1");
        ed1.setTitle("title ed11 ");
        ElementDto ed2=new ElementDto();
        ed2.setId(2L);
        ed2.setMarkdownContent("markdownContent ffc2");
        ed2.setTitle("title addd22 ");

        ChapterDto cd=new ChapterDto();
        cd.setDescription("description dto");
        cd.setElements(List.of(ed1,ed2));
        cd.setId(1L);
        cd.setSectionId(2L);
        cd.setTitle("chap title");
        cd.setVideoUrl("url to video");
        //whene
        Chapter c=ChapterDto.fromDto(cd);

        //thene
        assertEquals(cd.getId(),c.getId());
        assertEquals(cd.getDescription(),c.getDescription());
        
        assertEquals(cd.getSectionId(),c.getSectionId());
        assertEquals(cd.getTitle(),c.getTitle());
        assertEquals(cd.getVideoUrl(),c.getVideoUrl());
        assertEquals(cd.getElements().size(),c.getElements().size());
        assertEquals(cd.getElements().get(0).getId(),c.getElements().get(0).getId());
        assertEquals(c,c.getElements().get(0).getChapter());
    }
}
