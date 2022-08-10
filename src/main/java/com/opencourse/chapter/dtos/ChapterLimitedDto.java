package com.opencourse.chapter.dtos;
import com.opencourse.chapter.entities.Chapter;
import lombok.Data;

@Data
public class ChapterLimitedDto {
    private Long id;
    private String title;
    private String videoUrl;
    private String description;
    private Long sectionId;
    public static ChapterLimitedDto fromChapter(Chapter c){
        ChapterLimitedDto cd=new ChapterLimitedDto();
        cd.setDescription(c.getDescription());
        cd.setId(c.getId());
        cd.setSectionId(c.getSectionId());
        cd.setTitle(c.getTitle());
        cd.setVideoUrl(c.getVideoUrl());
        return cd;
    }
}
