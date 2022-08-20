package com.opencourse.chapter.exceptions;

public class UnAuhorizedActionException extends RuntimeException{
    public UnAuhorizedActionException(){
        super("unauthorized action");
    }
}
