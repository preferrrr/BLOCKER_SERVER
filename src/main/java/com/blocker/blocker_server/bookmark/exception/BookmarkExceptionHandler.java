package com.blocker.blocker_server.bookmark.exception;

import com.blocker.blocker_server.board.exception.BoardNotFoundException;
import com.blocker.blocker_server.board.exception.UnauthorizedDeleteBoardException;
import com.blocker.blocker_server.board.exception.UnauthorizedModifyBoardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BookmarkExceptionHandler {

    @ExceptionHandler(IsAlreadyBookmarkedException.class)
    public ResponseEntity<HttpStatus> handleIsAlreadyBookmarkedException(final IsAlreadyBookmarkedException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /** 이미 북마크 되어 있음 */
    }

    @ExceptionHandler(IsNotBookmarkedException.class)
    public ResponseEntity<HttpStatus> handleIsNotBookmarkedException(final IsNotBookmarkedException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /** 북마크 되어 있지 않음 */
    }

    @ExceptionHandler(UnauthorizedModifyBoardException.class)
    public ResponseEntity<HttpStatus> handleUnauthorizedModifyBoardException(final UnauthorizedModifyBoardException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /** 게시글 수정 권한 없음 */
    }
}
