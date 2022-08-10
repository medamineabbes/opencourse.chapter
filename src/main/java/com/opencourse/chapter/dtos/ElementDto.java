package com.opencourse.chapter.dtos;

import lombok.Getter;
import lombok.Setter;
import com.opencourse.chapter.entities.Element;


@Getter
@Setter
public class ElementDto {
    private Long id;
    private String title;
    private String markdownContent;

    public static ElementDto fromElement(Element e){
        ElementDto ed=new ElementDto();
        ed.setId(e.getId());
        ed.setTitle(e.getTitle());
        ed.setMarkdownContent(e.getMarkdownContent());
        return ed;
    }
    
    public static Element fromDto(ElementDto ed){
        Element e=new Element();
        e.setId(ed.getId());
        e.setMarkdownContent(ed.getMarkdownContent());
        e.setTitle(ed.getTitle());
        return e;
    }
}
