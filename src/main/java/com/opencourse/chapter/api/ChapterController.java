package com.opencourse.chapter.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.opencourse.chapter.dtos.ChapterDto;
import com.opencourse.chapter.dtos.ChapterLimitedDto;
import com.opencourse.chapter.services.ChapterService;

import lombok.AllArgsConstructor;
//https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
@RestController
@RequestMapping("/api/v1/chapter")
@AllArgsConstructor
public class ChapterController {
    private final ChapterService service;

    @PostMapping
    public ResponseEntity<Long> addChapter(@RequestBody(required=true) @Valid ChapterDto chapter){
        return ResponseEntity.ok(service.addChapter(chapter));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getById(@PathVariable(required=true) Long id){
        return ResponseEntity.ok(service.getChapterById(id));
    }
    
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<ChapterLimitedDto>> getChaptersBySectionId(@PathVariable(name = "sectionId") Long sectionId){
        return ResponseEntity.ok(service.getChaptersBySectionId(sectionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable(required=true) Long chapterId){
        service.deleteCahpterById(chapterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<Void> finishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=1L;//get from spring security later
        service.markChapterAsFinished(userId, chapterId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/unfinish/{id}")
    public ResponseEntity<Void> unfinishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=1L;
        service.markChapterAsUnFinished(userId, chapterId);
        return ResponseEntity.ok().build();
    }
}
