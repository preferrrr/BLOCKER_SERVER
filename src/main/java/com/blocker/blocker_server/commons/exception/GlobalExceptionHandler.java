package com.blocker.blocker_server.commons.exception;

import com.blocker.blocker_server.bookmark.exception.IsAlreadyBookmarkedException;
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


        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<?> handleForbiddenException(final ForbiddenException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /**403, 권한 없음. ex) 게시글 삭제*/
    }

    @ExceptionHandler({InvalidImageException.class})
    public ResponseEntity<?> handleInvalidImageException(final InvalidImageException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); /**204, 이미지 파일 안 보냄.*/
    }

    @ExceptionHandler({IsAlreadyBookmarkedException.class})
    public ResponseEntity<?> handleDuplicateBookmarkException(final IsAlreadyBookmarkedException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /**409, 이미 북마크로 등록했음.*/
    }

    @ExceptionHandler({InvalidQueryStringException.class})
    public ResponseEntity<?> handleInvalidQueryStringExceptionException(final InvalidQueryStringException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /**400, 잘못된 쿼리스트링.*/
    }

}
