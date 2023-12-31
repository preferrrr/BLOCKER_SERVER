package com.blocker.blocker_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidRequestParameterException.class})
    public ResponseEntity<?> handleDuplicateUsernameException(final InvalidRequestParameterException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);


        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleExistUsernameException(final NotFoundException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); /**404, db에 없음.*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({FailSaveSignatureException.class})
    public ResponseEntity<?> handleFailSaveSignatureException(final FailSaveSignatureException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); /**500, 전자 서명 저장 실패.*/
    }



    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidRefreshTokenException.class})
    public ResponseEntity<?> handleInvalidRefreshTokenException(final InvalidRefreshTokenException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); /**401, 유효하지 않은 리프레스 토큰.*/
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({ExistsSignatureException.class})
    public ResponseEntity<?> handleExistsSignatureException(final ExistsSignatureException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /**409, 등록한 전자서명이 이미 존재.*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<?> handleForbiddenException(final ForbiddenException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN); /**403, 권한 없음. ex) 게시글 삭제*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidImageException.class})
    public ResponseEntity<?> handleInvalidImageException(final InvalidImageException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); /**204, 이미지 파일 안 보냄.*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({DuplicateBookmarkException.class})
    public ResponseEntity<?> handleDuplicateBookmarkException(final DuplicateBookmarkException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.CONFLICT); /**409, 이미 북마크로 등록했음.*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ModifyContractException.class})
    public ResponseEntity<?> ModifyContractException(final ModifyContractException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /**400, 진행 중이거나 완료된 계약은 수정 불가.*/
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidQueryStringException.class})
    public ResponseEntity<?> InvalidQueryStringExceptionException(final InvalidQueryStringException e) {

        String msg = e.getNAME() + ": [" + e.getMessage() + "]";
        log.error(msg);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /**400, 잘못된 쿼리스트링.*/
    }
}
