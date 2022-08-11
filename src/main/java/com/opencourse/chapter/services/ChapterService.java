package com.opencourse.chapter.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import com.opencourse.chapter.repos.*;
import com.opencourse.chapter.entities.Chapter;
import com.opencourse.chapter.entities.Element;
import com.opencourse.chapter.entities.FinishedChapter;
import com.opencourse.chapter.dtos.*;
import com.opencourse.chapter.exceptions.ChapterAlreadyFinishedException;
import com.opencourse.chapter.exceptions.ChapterNotFoundException;
import com.opencourse.chapter.exceptions.ElementNotFoundException;
import com.opencourse.chapter.exceptions.ChapterAlreadyUnFinishedException;
/*
 * 
 * Post || Update || Delete request must be made by creator of the course
 * 
 * add preauth filter (spring security)
 * 
 * 
*/
@Service
@Transactional
@AllArgsConstructor
public class ChapterService {
    private final ChapterRepo chapterRepo;
    private final ElementRepo elementRepo;
    private final FinishedChapterRepo finishedChapterRepo;
    //add security
    public ChapterLimitedDto addChapter(ChapterDto chapterDto){
        //add chapter thene elements
        Chapter chapter=ChapterDto.fromDto(chapterDto);
        chapterRepo.save(chapter);
        elementRepo.saveAll(chapter.getElements());
        return ChapterLimitedDto.fromChapter(chapter);
    }
    //add security
    public ElementDto addElement(ElementDto element){
        Element e=ElementDto.fromDto(element);
        elementRepo.save(e);
        return ElementDto.fromElement(e);
    }

    public ChapterDto getChapterById(Long id)throws ChapterNotFoundException{
        return ChapterDto.fromChapter(
            chapterRepo.findById(id)
            .orElseThrow(
                ()->new ChapterNotFoundException(id)
                )
        );
    }

    public List<ChapterLimitedDto> getChaptersBySectionId(Long id){
        return chapterRepo.findBySectionId(id)
        .stream().map(chapter-> ChapterLimitedDto.fromChapter(chapter))
        .collect(Collectors.toList());
    } 

    //add security
    public void updateChapter(ChapterLimitedDto cld){
        Chapter c=chapterRepo.findById(cld.getId())
        .orElseThrow(()->new ChapterNotFoundException(cld.getId()));
        c.setDescription(cld.getDescription());
        //c.setSectionId(sectionId);
        c.setTitle(cld.getTitle());
        c.setVideoUrl(cld.getVideoUrl());
        chapterRepo.flush();
    }

    //add security
    public void updateElement(ElementDto ed){
        Element e=elementRepo.findById(ed.getId())
        .orElseThrow(()-> new ElementNotFoundException(ed.getId()));
        e.setMarkdownContent(ed.getMarkdownContent());
        e.setTitle(ed.getTitle());
        elementRepo.flush();
    }

    //add security
    public void deleteCahpterById(Long id)throws ChapterNotFoundException{
        //delete elements thene chapter
        Chapter c=chapterRepo.findById(id)
        .orElseThrow(()->new ChapterNotFoundException(id));
        c.getElements().stream()
        .forEach((e)->elementRepo.deleteById(e.getId()));
        chapterRepo.deleteById(id);
    }  

    //add security
    public void deleteElementById(Long id){
        elementRepo.deleteById(id);
    }
    
    public void markChapterAsFinished(Long userId,Long chapterId){
        //if chapter is allready finished thene ignore
        Optional<FinishedChapter> isFinished=finishedChapterRepo.findByUserIdAndChapterId(userId, chapterId);
        Chapter chapter=chapterRepo.findById(chapterId)
        .orElseThrow(()->new ChapterNotFoundException(chapterId));
        
        if(isFinished.isEmpty()){
            FinishedChapter fc=new FinishedChapter();
            fc.setChapter(chapter);
            fc.setUserId(userId);
            finishedChapterRepo.save(fc);
        }else{
            throw new ChapterAlreadyFinishedException(chapterId);
        }
    }

    public void markChapterAsUnFinished(Long userId,Long chapterId){
        chapterRepo.findById(chapterId)
        .orElseThrow(
            ()-> new ChapterNotFoundException(chapterId)
            );

        FinishedChapter isFinished=finishedChapterRepo.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(
            ()->new ChapterAlreadyUnFinishedException(chapterId)
            );
        
        finishedChapterRepo.deleteById(isFinished.getId());
    }
}
