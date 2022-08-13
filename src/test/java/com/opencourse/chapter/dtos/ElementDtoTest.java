package com.opencourse.chapter.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import com.opencourse.chapter.entities.Element;

public class ElementDtoTest {
    @Test
    public void conversionToDtoTest(){
        //given 
        Element e=new Element();
        e.setId(1L);
        e.setMarkdownContent("content");
        e.setTitle("title");
        //whene
        ElementDto ed=ElementDto.fromElement(e);

        //thene assert(expected,actual)
        assertEquals(e.getTitle(),ed.getTitle());
        assertEquals(e.getMarkdownContent(),ed.getMarkdownContent());
    }
    @Test
    public void conversionFromDtoTest(){
        //given
        ElementDto ed=new ElementDto();
        ed.setMarkdownContent("markdownContent ##egeg");
        ed.setTitle("title ed");

        //whene 
        Element e= ElementDto.fromDto(ed);

        //thene

        assertEquals(ed.getTitle(),e.getTitle());
        assertEquals(ed.getMarkdownContent(),e.getMarkdownContent());
    }
}
