package com.opencourse.chapter.exceptions;

public class CustomAuthenticationException extends RuntimeException{
    public CustomAuthenticationException(){
        super("authentcation error");
    }
}
