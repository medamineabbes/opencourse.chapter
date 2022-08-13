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

@Service
@Transactional
@AllArgsConstructor
public class ChapterService {
    private final ChapterRepo chapterRepo;
    private final ElementRepo elementRepo;
    private final FinishedChapterRepo finishedChapterRepo;
    //add security
    public Long addChapter(ChapterDto chapterDto){
        /*
         * 
         * test if course created by user
         */


        Chapter chapter=ChapterDto.fromDto(chapterDto);
        chapterRepo.save(chapter);
        return chapter.getId();
    }
    //add security
    public Long addElement(ElementDto element){

        //test if chapter exists
        Chapter c=chapterRepo
        .findById(element.getChapterId())
        .orElseThrow(()->new ChapterNotFoundException(element.getChapterId()));
        /**
         * test if course created by user
         */
        Element e=ElementDto.fromDto(element);
        e.setChapter(c);
        elementRepo.save(e);
        return e.getId();
    }

    public ChapterDto getChapterById(Long id)throws ChapterNotFoundException{
        return ChapterDto.fromChapter(
            chapterRepo.findById(id)
            .orElseThrow(
                ()->new ChapterNotFoundException(id)
                )
        );
    }

    public List<ChapterDto> getChaptersBySectionId(Long id){
        return chapterRepo.findBySectionId(id)
        .stream().map(chapter-> ChapterDto.fromChapter(chapter))
        .collect(Collectors.toList());
    } 

    public ElementDto getElementById(Long id){
        Element e=elementRepo
        .findById(id)
        .orElseThrow(()->new ElementNotFoundException(id));
        return ElementDto.fromElement(e);
    }
    
    public List<ElementDto> getElementsByChapterId(Long id){
        Chapter chapter=chapterRepo
        .findById(id)
        .orElseThrow(()->new ChapterNotFoundException(id));
        return chapter.getElements()
        .stream()
        .map(element->ElementDto.fromElement(element))
        .collect(Collectors.toList());
    }
    //add security
    public void updateChapter(ChapterDto cld){

        /*
         * test if chapter created by user
         */

        //make sure chapter exists
        Chapter c=chapterRepo.findById(cld.getId())
        .orElseThrow(()->new ChapterNotFoundException(cld.getId()));

        //update chapter
        c.setDescription(cld.getDescription());
        c.setTitle(cld.getTitle());
        c.setVideoUrl(cld.getVideoUrl());

        chapterRepo.flush();
    }

    //add security
    public void updateElement(ElementDto ed){
        /*
         * test if course created by user
         */

         //make sure element exists
        Element e=elementRepo.findById(ed.getId())
        .orElseThrow(()-> new ElementNotFoundException(ed.getId()));

        //update element
        e.setMarkdownContent(ed.getMarkdownContent());
        e.setTitle(ed.getTitle());
        elementRepo.flush();
    }

    //add security
    public void deleteChapterById(Long id)throws ChapterNotFoundException{
        Chapter c=chapterRepo.findById(id).orElseThrow(()->new ChapterNotFoundException(id));
        //delete elements thene chapter
        chapterRepo.deleteById(c.getId());
    }  

    //add security
    public void deleteElementById(Long id){
        elementRepo.deleteById(id);
    }
    
    public void markChapterAsFinished(Long userId,Long chapterId){
        //if chapter is allready finished thene ignore
        Optional<FinishedChapter> isFinished=finishedChapterRepo.findByUserIdAndChapterId(userId, chapterId);
        
        //make sure chapter exists
        Chapter chapter=chapterRepo.findById(chapterId)
        .orElseThrow(()->new ChapterNotFoundException(chapterId));
        
        if(isFinished.isEmpty()){//if chapter is nt finished 
            FinishedChapter fc=new FinishedChapter();
            fc.setChapter(chapter);
            fc.setUserId(userId);
            finishedChapterRepo.save(fc);
        }else{//if chapter is finished
            throw new ChapterAlreadyFinishedException(chapterId);
        }
    }

    public void markChapterAsUnFinished(Long userId,Long chapterId){
        //make sure chapter exists
        chapterRepo.findById(chapterId)
        .orElseThrow(
            ()-> new ChapterNotFoundException(chapterId)
            );
        //if chapter is unfinished throw exception
        FinishedChapter isFinished=finishedChapterRepo.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(
            ()->new ChapterAlreadyUnFinishedException(chapterId)
            );
        //else remove from finished chapters 
        finishedChapterRepo.deleteById(isFinished.getId());
    }
}
