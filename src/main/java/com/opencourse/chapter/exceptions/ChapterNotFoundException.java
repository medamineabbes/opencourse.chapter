package com.opencourse.chapter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChapterNotFoundException extends RuntimeException{
    public ChapterNotFoundException(Long id){
        super("chapter with id : " + id +" not found");
    }
}
