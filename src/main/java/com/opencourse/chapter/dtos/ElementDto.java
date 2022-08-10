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
}
