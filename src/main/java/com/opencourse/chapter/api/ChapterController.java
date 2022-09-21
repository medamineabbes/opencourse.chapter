package com.opencourse.chapter.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.opencourse.chapter.dtos.ChapterDto;
import com.opencourse.chapter.services.ChapterService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/chapter")
@AllArgsConstructor
@Slf4j
public class ChapterController {
    
    private final ChapterService service;

    //only teachers
    @PostMapping
    public ResponseEntity<Long> addChapter(@RequestBody(required=true) @Valid ChapterDto chapter){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.addChapter(chapter,userId));
    }
    
    //authentic users
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getById(@PathVariable(required=true) Long id){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getChapterById(id,userId));
    }
    
    //authentic users
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<ChapterDto>> getChaptersBySectionId(@PathVariable(name = "sectionId") Long sectionId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getChaptersBySectionId(sectionId,userId));
    }

    //only teachers
    @PutMapping
    public ResponseEntity<Object> updateChapter(@RequestBody(required=true) @Valid ChapterDto chapter){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.updateChapter(chapter,userId);
        return ResponseEntity.ok().build();
    }

    //only teacher
    @DeleteMapping("/{chapterId}")
    public ResponseEntity<Object> deleteChapter(@PathVariable(required=true) Long chapterId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.deleteChapterById(chapterId,userId);
        return ResponseEntity.ok().build();
    }

    //authentic users
    @GetMapping("/finish/{chapterId}")
    public ResponseEntity<Object> finishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.markChapterAsFinished(chapterId,userId);
        return ResponseEntity.ok().build();
    }
    
    //authentic users
    @GetMapping("/unfinish/{chapterId}")
    public ResponseEntity<Object> unfinishChapter(@PathVariable(required=true) Long chapterId){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.markChapterAsUnFinished(chapterId,userId);
        return ResponseEntity.ok().build();
    }

    //only courseService
    @PostMapping("/valid")
    public ResponseEntity<Boolean> validSections(@RequestParam(required = true) List<Long> sectionIds){
        log.info("size is " + sectionIds.size());
        return ResponseEntity.ok(service.validSections(sectionIds));
    }
    
}
