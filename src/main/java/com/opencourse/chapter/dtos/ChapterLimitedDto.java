package com.opencourse.chapter.dtos;
import com.opencourse.chapter.entities.Chapter;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChapterLimitedDto {
    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters is 50")
    private String title;
    @NotBlank(message="video is mandatory")
    private String videoUrl;
    @NotBlank(message="description is mandatory")
    @Size(max=300,message="max number of characters is 300")
    private String description;
    @NotNull
    private Long sectionId;
    public static ChapterLimitedDto fromChapter(Chapter c){
        ChapterLimitedDto cd=new ChapterLimitedDto();
        cd.setDescription(c.getDescription());
        cd.setSectionId(c.getSectionId());
        cd.setTitle(c.getTitle());
        cd.setVideoUrl(c.getVideoUrl());
        return cd;
    }
}
