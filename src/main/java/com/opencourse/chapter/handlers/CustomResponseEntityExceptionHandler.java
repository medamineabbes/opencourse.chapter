package com.opencourse.chapter.handlers;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.opencourse.chapter.exceptions.ChapterAlreadyFinishedException;
import com.opencourse.chapter.exceptions.ChapterAlreadyUnFinishedException;
import com.opencourse.chapter.exceptions.ChapterNotFoundException;
import com.opencourse.chapter.exceptions.CustomAuthenticationException;
import com.opencourse.chapter.exceptions.ElementNotFoundException;
import com.opencourse.chapter.exceptions.UnAuhorizedActionException;

import lombok.Setter;
import lombok.Getter;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler{


    
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){
        List<String> errors=new ArrayList<String>();
        for(FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.add(error.getDefaultMessage());
        }
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + 
            violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ApiError apiError = new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setErrors(List.of(error));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ChapterNotFoundException.class})
    public ResponseEntity<Object> handleChapterNotFoundException(ChapterNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMsg(ex.getLocalizedMessage());
        error.setErrors(List.of(ex.getMessage()));
        return new ResponseEntity<Object>(error,new HttpHeaders(),error.getStatus());
    }

    @ExceptionHandler({ElementNotFoundException.class})
    public ResponseEntity<Object> handleElementNotFoundException(ElementNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMsg(ex.getLocalizedMessage());
        error.setErrors(List.of(ex.getMessage()));
        return new ResponseEntity<Object>(error,new HttpHeaders(),error.getStatus());
    }

    @ExceptionHandler({ChapterAlreadyFinishedException.class})
    public ResponseEntity<Object> handleChapterAlreadyFinishedExceptions(ChapterAlreadyFinishedException ex,WebRequest request){
        ApiError apiError=new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMsg(ex.getMessage());
        return new ResponseEntity<Object>(apiError,new HttpHeaders(),HttpStatus.CONFLICT);        
    }

    @ExceptionHandler({ChapterAlreadyUnFinishedException.class})
    public ResponseEntity<Object> handleChapterAlreadyUnFinishedExceptions(ChapterAlreadyUnFinishedException ex,WebRequest request){
        ApiError apiError=new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMsg(ex.getMessage());
        return new ResponseEntity<Object>(apiError,new HttpHeaders(),HttpStatus.CONFLICT);        
    }

    @ExceptionHandler({CustomAuthenticationException.class})
    public ResponseEntity<Object> handleCustomAuthenticationExceptions(CustomAuthenticationException ex,WebRequest request){
        ApiError apiError=new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMsg(ex.getMessage());
        return new ResponseEntity<Object>(apiError,new HttpHeaders(),HttpStatus.CONFLICT);        
    }

    @ExceptionHandler({UnAuhorizedActionException.class})
    public ResponseEntity<Object> handleUnAuhorizedActionExceptions(UnAuhorizedActionException ex,WebRequest request){
        ApiError apiError=new ApiError();
        apiError.setStatus(HttpStatus.UNAUTHORIZED);
        apiError.setMsg(ex.getMessage());
        return new ResponseEntity<Object>(apiError,new HttpHeaders(),HttpStatus.CONFLICT);        
    }
}


@Getter
@Setter
class ApiError{
    private HttpStatus status;
    private String msg;
    private List<String> errors;
}