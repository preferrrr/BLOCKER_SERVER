package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BoardExceptionHandler {

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<HttpStatus> handleBoardNotFoundException(final BoardNotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /** 게시글 찾을 수 없음 */
    }

    @ExceptionHandler(UnauthorizedDeleteBoardException.class)
    public ResponseEntity<HttpStatus> handleUnauthorizedDeleteBoardException(final UnauthorizedDeleteBoardException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 게시글 삭제 권한 없음 */
    }

    @ExceptionHandler(UnauthorizedModifyBoardException.class)
    public ResponseEntity<HttpStatus> handleUnauthorizedModifyBoardException(final UnauthorizedModifyBoardException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 게시글 수정 권한 없음 */
    }
}
