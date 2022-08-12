package com.opencourse.chapter.exceptions;

public class ElementNotFoundException extends RuntimeException{
    public ElementNotFoundException(Long id){
        super("element with id : " + id + " not found");
    }
}
