package com.opencourse.chapter.exceptions;

public class ChapterNotFoundException extends RuntimeException{
    public ChapterNotFoundException(Long id){
        super("chapter with id : " + id +" not found");
    }
    public ChapterNotFoundException(String msg){
        super(msg);
    }
}
