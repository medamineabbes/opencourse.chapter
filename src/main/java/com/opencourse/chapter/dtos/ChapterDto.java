package com.opencourse.chapter.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import com.opencourse.chapter.entities.Chapter;
@Getter
@Setter
public class ChapterDto {
    private Long id;
    private String title;
    private String videoUrl;
    private String description;
    private Long sectionId;
    private Boolean finished;
    private List<ElementDto> elements;
    public ChapterDto(){
        
    }
    public static ChapterDto fromChapter(Chapter c){
        ChapterDto cd=new ChapterDto();
        cd.setDescription(c.getDescription());
        cd.setId(c.getId());
        cd.setSectionId(c.getSectionId());
        cd.setTitle(c.getTitle());
        cd.setFinished(false);
        cd.setVideoUrl(c.getVideoUrl());
        cd.setElements(
            c.getElements().stream()
            .map(e->ElementDto.fromElement(e))
            .collect(Collectors.toList())
        );
        return cd;
    }
    public static Chapter fromDto(ChapterDto cd){
        Chapter c=new Chapter();
        c.setDescription(cd.getDescription());
        c.setId(cd.getId());
        c.setSectionId(cd.getSectionId());
        c.setTitle(cd.getTitle());
        c.setVideoUrl(cd.getVideoUrl());
        c.setElements(
            cd.getElements().stream()
            .map((e)->ElementDto.fromDto(e))
            .collect(Collectors.toList())
        );
        return c;
    }
}













