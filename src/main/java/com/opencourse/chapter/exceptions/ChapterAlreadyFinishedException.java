package com.opencourse.chapter.exceptions;

public class ChapterAlreadyFinishedException extends RuntimeException{
    public ChapterAlreadyFinishedException(Long id){
        super("chapter with id : " + id + " already finished");
    }
}
