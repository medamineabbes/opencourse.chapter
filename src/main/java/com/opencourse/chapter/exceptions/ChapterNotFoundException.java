package com.opencourse.chapter.exceptions;

public class ChapterNotFoundException extends RuntimeException{
    public ChapterNotFoundException(Long id){
        super("chapter with id : " + id +" not found");
    }
}
