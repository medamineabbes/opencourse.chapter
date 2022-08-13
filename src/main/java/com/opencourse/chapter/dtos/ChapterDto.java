package com.opencourse.chapter.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import com.opencourse.chapter.entities.Chapter;
import com.opencourse.chapter.entities.FinishedChapter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChapterDto {

    private Long id;

    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters in title is 50")
    private String title;

    @NotBlank(message="video is mandatory")
    private String videoUrl;

    @NotBlank(message="description is mandatory")
    @Size(max=300,message="max number of characters in description is 300")
    private String description;

    @NotNull(message="sectionId is mandatory")
    private Long sectionId;

    private Boolean finished;

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
        return cd;
    }
    public static Chapter fromDto(ChapterDto cd){
        Chapter c=new Chapter();
        c.setDescription(cd.getDescription());
        c.setId(cd.getId());
        c.setSectionId(cd.getSectionId());
        c.setTitle(cd.getTitle());
        c.setVideoUrl(cd.getVideoUrl());
        c.setFinishedChapters(new ArrayList<FinishedChapter>());
        return c;
    }
}













