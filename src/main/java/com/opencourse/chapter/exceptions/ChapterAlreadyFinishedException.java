package com.opencourse.chapter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ChapterAlreadyFinishedException extends RuntimeException{
    public ChapterAlreadyFinishedException(Long id){
        super("chapter with id : " + id + " already finished");
    }
}
