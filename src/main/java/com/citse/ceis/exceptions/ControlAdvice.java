package com.citse.ceis.exceptions;

import com.citse.ceis.models.GUSResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControlAdvice {
    @ExceptionHandler(value = {GUSException.class})
    public ResponseEntity<GUSResponse> HookExceptionHandler(GUSException gusException, HttpServletRequest request){
        GUSResponse errorReported = new GUSResponse(request.getRequestURI(),gusException.getOrigin(),gusException.getData(),gusException.getStatus().name());
        return new ResponseEntity<>(errorReported,gusException.getStatus());
    }
}