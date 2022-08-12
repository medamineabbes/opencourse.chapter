package com.opencourse.chapter.exceptions;

public class ChapterAlreadyUnFinishedException extends RuntimeException{
    public ChapterAlreadyUnFinishedException(Long id){
        super("chapter with id : " + id + " already not finished");
    }
}
