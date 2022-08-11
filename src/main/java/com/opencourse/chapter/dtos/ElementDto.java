package com.opencourse.chapter.dtos;

import lombok.Getter;
import lombok.Setter;
import com.opencourse.chapter.entities.Element;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ElementDto {
    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters is 50")
    private String title;
    @NotBlank
    private String markdownContent;
    @NotNull
    private Long chapterId;
    public static ElementDto fromElement(Element e){
        ElementDto ed=new ElementDto();
        ed.setTitle(e.getTitle());
        ed.setMarkdownContent(e.getMarkdownContent());
        ed.setChapterId(e.getChapter().getId());
        return ed;
    }
    
    public static Element fromDto(ElementDto ed){
        Element e=new Element();
        e.setMarkdownContent(ed.getMarkdownContent());
        e.setTitle(ed.getTitle());
        return e;
    }
}
