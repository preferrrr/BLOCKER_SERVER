package com.blocker.blocker_server.commons.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidRequestParameterException.class})
    public ResponseEntity<?> handleDuplicateUsernameException(final InvalidRequestParameterException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
