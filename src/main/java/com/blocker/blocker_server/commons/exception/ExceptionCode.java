package com.blocker.blocker_server.commons.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus getHttpStatus();
    String getMessage();
}
