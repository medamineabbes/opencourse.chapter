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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.Setter;
import lombok.Getter;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler{
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
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
        apiError.setStatus(HttpStatus.NOT_FOUND);
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




}


@Getter
@Setter
class ApiError{
    private HttpStatus status;
    private String msg;
    private List<String> errors;
}