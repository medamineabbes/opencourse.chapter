package com.opencourse.chapter.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.opencourse.chapter.dtos.ElementDto;
import com.opencourse.chapter.services.ChapterService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/element")
@AllArgsConstructor
public class ElementController {
    private final ChapterService service;

    @PostMapping
    public ResponseEntity<Long> addElement(@RequestBody @Valid ElementDto element){
        return ResponseEntity.ok(service.addElement(element));
    }

    @PutMapping
    public ResponseEntity<Void> updateElement(@RequestBody @Valid ElementDto element){
        service.updateElement(element);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable Long id){
        service.deleteElementById(id);
        return ResponseEntity.ok().build();
    }
}
