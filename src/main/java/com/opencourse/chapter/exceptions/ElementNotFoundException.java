package com.opencourse.chapter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ElementNotFoundException extends RuntimeException{
    public ElementNotFoundException(Long id){
        super("element with id : " + id + " not found");
    }
}
