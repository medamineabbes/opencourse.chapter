package com.opencourse.chapter.dtos;

import lombok.Getter;
import lombok.Setter;
import com.opencourse.chapter.entities.Element;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ElementDto {
    private Long id;
    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters is 50")
    private String title;
    @NotBlank
    private String markdownContent;
    private Long chapterId;

    public static ElementDto fromElement(Element e){
        ElementDto ed=new ElementDto();
        ed.setId(e.getId());
        ed.setTitle(e.getTitle());
        ed.setMarkdownContent(e.getMarkdownContent());
        if(e.getChapter() != null)
        ed.setChapterId(e.getChapter().getId());
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
