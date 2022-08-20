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
import com.opencourse.chapter.exceptions.UnAuhorizedActionException;
import com.opencourse.chapter.externalservices.CourseService;
import com.opencourse.chapter.exceptions.ChapterAlreadyUnFinishedException;

@Service
@Transactional
@AllArgsConstructor
public class ChapterService {
    private final ChapterRepo chapterRepo;
    private final ElementRepo elementRepo;
    private final FinishedChapterRepo finishedChapterRepo;
    private final CourseService courseService;
    

    //only teachers
    public Long addChapter(ChapterDto chapterDto,Long userId){
        
        //make sure user created Section
        if(!courseService.userCreatedSection(chapterDto.getSectionId(), userId))
        throw new UnAuhorizedActionException();

        Chapter chapter=ChapterDto.fromDto(chapterDto);
        chapterRepo.save(chapter);
        return chapter.getId();
    }
    //only teachers 
    public Long addElement(ElementDto element,Long userId){

        //make sure chapter exists
        Chapter c=chapterRepo
        .findById(element.getChapterId())
        .orElseThrow(()->new ChapterNotFoundException(element.getChapterId()));
        
        //make sure user created Section
        if(!courseService.userCreatedSection(c.getSectionId(), userId))
        throw new UnAuhorizedActionException();

        Element e=ElementDto.fromDto(element);
        e.setChapter(c);
        elementRepo.save(e);
        return e.getId();
    }

    //authentic users
    public ChapterDto getChapterById(Long id,Long userId){
        
        //make sure chapter exists
        Chapter chapter=chapterRepo
        .findById(id)
        .orElseThrow(()->new ChapterNotFoundException(id));
        
        //make sure user hase access to chapter
        if(!courseService.userHasAccessToSection(chapter.getSectionId(), userId))    
        throw new ChapterNotFoundException(id);
        return ChapterDto.fromChapter(chapter);
    }

    //authentic users
    public List<ChapterDto> getChaptersBySectionId(Long id,Long userId){
        //make sure user has access
        if(!courseService.userHasAccessToSection(id, userId))
        throw new ChapterNotFoundException("not chapters found");

        return chapterRepo.findBySectionId(id)
        .stream().map(chapter-> ChapterDto.fromChapter(chapter))
        .collect(Collectors.toList());
    } 

    //auhentic users
    public ElementDto getElementById(Long id,Long userId){
        
        //make sure elements exists
        Element e=elementRepo
        .findById(id)
        .orElseThrow(()->new ElementNotFoundException(id));
        
        //make sure user has access
        if(!courseService.userHasAccessToSection(e.getChapter().getSectionId(), userId))
        throw new ElementNotFoundException(id);

        return ElementDto.fromElement(e);
    }
    
    //authetic users
    public List<ElementDto> getElementsByChapterId(Long id,Long userId){

        //make sure chapter exists
        Chapter chapter=chapterRepo
        .findById(id)
        .orElseThrow(()->new ChapterNotFoundException(id));
        
        //make sure user has acces
        if(!courseService.userHasAccessToSection(chapter.getSectionId(), userId))
        throw new ChapterNotFoundException(id);
        
        return chapter.getElements()
        .stream()
        .map(element->ElementDto.fromElement(element))
        .collect(Collectors.toList());

    }
    
    //only teachers
    public void updateChapter(ChapterDto cld,Long userId){

        //make sure chapter exists
        Chapter c=chapterRepo.findById(cld.getId())
        .orElseThrow(()->new ChapterNotFoundException(cld.getId()));

        //make sure chapter created by user
        if(!courseService.userCreatedSection(c.getSectionId(), userId))
        throw new UnAuhorizedActionException();

        //update chapter
        c.setDescription(cld.getDescription());
        c.setTitle(cld.getTitle());
        c.setVideoUrl(cld.getVideoUrl());

        chapterRepo.flush();
    }

    //only teachers
    public void updateElement(ElementDto ed,Long userId){
        
        //make sure element exists
        Element e=elementRepo.findById(ed.getId())
        .orElseThrow(()-> new ElementNotFoundException(ed.getId()));
        
        //make sure user created chapter
        if(!courseService.userCreatedSection(e.getChapter().getSectionId(),userId))
        throw new UnAuhorizedActionException();

        //update element
        e.setMarkdownContent(ed.getMarkdownContent());
        e.setTitle(ed.getTitle());
        elementRepo.flush();
    }

    //only teachers
    public void deleteChapterById(Long id,Long userId){
        //make sure chapter exists
        Chapter c=chapterRepo.findById(id)
        .orElseThrow(()->new ChapterNotFoundException(id));

        //make sure chapter created by user
        if(!courseService.userCreatedSection(c.getSectionId(), userId))
        throw new UnAuhorizedActionException();

        chapterRepo.deleteById(c.getId());
    }  

    //only teachers
    public void deleteElementById(Long id,Long userId){
        
        //make sure element exists
        Element element=elementRepo.findById(id)
        .orElseThrow(()->new ElementNotFoundException(id));
        
        //make sure chapter created by user
        if(courseService.userCreatedSection(element.getChapter().getSectionId(), userId))
        throw new UnAuhorizedActionException();

        elementRepo.deleteById(id);
    }
    
    //only authentic users 
    public void markChapterAsFinished(Long chapterId,Long userId){

        //make sure chapter exists
        Chapter chapter=chapterRepo.findById(chapterId)
        .orElseThrow(()->new ChapterNotFoundException(chapterId));

        //make sure use has access to chapter
        if(!courseService.userHasAccessToSection(chapter.getSectionId(), userId))
        throw new ChapterNotFoundException(chapterId);


        Optional<FinishedChapter> isFinished=finishedChapterRepo
        .findByUserIdAndChapterId(userId, chapterId);

        
        if(isFinished.isEmpty()){//if chapter is nt finished 
            FinishedChapter fc=new FinishedChapter();
            fc.setChapter(chapter);
            fc.setUserId(userId);
            finishedChapterRepo.save(fc);
        }else{//if chapter is finished
            throw new ChapterAlreadyFinishedException(chapterId);
        }
    }
    
    //only authentic users
    public void markChapterAsUnFinished(Long chapterId,Long userId){
        //make sure chapter exists
        Chapter chapter=chapterRepo.findById(chapterId)
        .orElseThrow(()-> new ChapterNotFoundException(chapterId));

        //make sure user has access to chapter
        if(!courseService.userHasAccessToSection(chapter.getSectionId(), userId))
        throw new ChapterNotFoundException(chapterId);

        //if chapter is unfinished throw exception
        FinishedChapter isFinished=finishedChapterRepo.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(
            ()->new ChapterAlreadyUnFinishedException(chapterId)
            );
        //else remove from finished chapters 
        finishedChapterRepo.deleteById(isFinished.getId());
    }

    //only course service
    public boolean validSections(List<Long> sectionIds){
        for(int i=0;i<sectionIds.size();i++){
            Long sectionId=sectionIds.get(i);
            List<Chapter> chapters=chapterRepo.findBySectionId(sectionId);

            //make sure more than 2 chapters per section
            if(chapters.size()<2)
            return false;
            for(int j=0;j<chapters.size();j++){
                Chapter chapter=chapters.get(j);

                //make sure more than 2 elements per chapter
                if(chapter.getElements().size()<2)
                return false;
            }
            
        }
        return true;
    }

}
