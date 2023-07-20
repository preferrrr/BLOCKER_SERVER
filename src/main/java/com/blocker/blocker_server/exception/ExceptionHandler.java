package com.blocker.blocker_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {


//    @org.springframework.web.bind.annotation.ExceptionHandler({DuplicateUsernameException.class})
//    public ResponseEntity<?> handleDuplicateUsernameException(final DuplicateUsernameException e) {
//
//        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
//        log.error(msg);
//
//        ExceptionMessage exceptionMessage = new ExceptionMessage("이미 존재하는 아이디입니다.");
//
//        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
//    }


}
