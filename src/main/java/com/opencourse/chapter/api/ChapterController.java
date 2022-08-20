package com.opencourse.chapter.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.opencourse.chapter.dtos.ChapterDto;
import com.opencourse.chapter.services.ChapterService;

import lombok.AllArgsConstructor;
//https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
@RestController
@RequestMapping("/api/v1/chapter")
@AllArgsConstructor
public class ChapterController {
    private final ChapterService service;

    //only teachers
    @PostMapping
    public ResponseEntity<Long> addChapter(@RequestBody(required=true) @Valid ChapterDto chapter){
        Long userId=15L;
        return ResponseEntity.ok(service.addChapter(chapter,userId));
    }
    
    //authentic users
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getById(@PathVariable(required=true) Long id){
        Long userId=15L;
        return ResponseEntity.ok(service.getChapterById(id,userId));
    }
    
    //authentic users
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<ChapterDto>> getChaptersBySectionId(@PathVariable(name = "sectionId") Long sectionId){
        Long userId=15L;
        return ResponseEntity.ok(service.getChaptersBySectionId(sectionId,userId));
    }

    //only teachers
    @PutMapping
    public ResponseEntity<Void> updateChapter(@RequestBody(required=true) @Valid ChapterDto chapter){
        Long userId=15L;
        service.updateChapter(chapter,userId);
        return ResponseEntity.ok().build();
    }

    //only authentic users
    @DeleteMapping("/{chapterId}")
    public ResponseEntity<Void> deleteChapter(@PathVariable(required=true) Long chapterId){
        Long userId=15L;
        service.deleteChapterById(chapterId,userId);
        return ResponseEntity.ok().build();
    }

    //authentic users
    @GetMapping("/finish/{chapterId}")
    public ResponseEntity<Void> finishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=15L;
        service.markChapterAsFinished(chapterId,userId);
        return ResponseEntity.ok().build();
    }
    
    //authentic users
    @GetMapping("/unfinish/{chapterId}")
    public ResponseEntity<Void> unfinishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=1L;
        service.markChapterAsUnFinished(chapterId,userId);
        return ResponseEntity.ok().build();
    }

    //only courseService
    @PostMapping("/valid")
    public ResponseEntity<Boolean> validSections(@RequestBody(required = true) List<Long> sectionIds){
        return ResponseEntity.ok(service.validSections(sectionIds));
    }
    
}
