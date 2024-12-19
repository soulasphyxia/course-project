package org.soulasphyxia.webcourseproject.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenAccessException.class)
    public String handleForbiddenAccessException() {
        return "exception/forbidden";
    }
}
