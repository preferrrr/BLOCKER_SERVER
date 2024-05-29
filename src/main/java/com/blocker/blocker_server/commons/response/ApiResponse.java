package com.blocker.blocker_server.commons.response;

import com.blocker.blocker_server.commons.response.response_dto.ResponseListDto;
import com.blocker.blocker_server.commons.response.response_dto.ResponseMessageDto;
import com.blocker.blocker_server.commons.response.response_dto.ResponseSingleDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiResponse<T> extends ResponseEntity {

    public ApiResponse(final SuccessCode successCode) {
        super(new ResponseMessageDto(successCode.getMessage()), successCode.getHttpStatus());
    }

    public ApiResponse(final T data, final SuccessCode successCode) {
        super(new ResponseSingleDto<>(data, successCode.getMessage()), successCode.getHttpStatus());
    }

    public ApiResponse(final List<T> data, final SuccessCode successCode) {
        super(new ResponseListDto<>(data, successCode.getMessage()), successCode.getHttpStatus());
    }

    public ApiResponse(final HttpHeaders httpHeaders, final SuccessCode successCode) {
        super(new ResponseMessageDto(successCode.getMessage()), httpHeaders, successCode.getHttpStatus());
    }

    public static ApiResponse of(final SuccessCode successCode) {
        return new ApiResponse<>(successCode);
    }

    public static <T> ApiResponse<T> of(final T data, final SuccessCode successCode) {
        return new ApiResponse<>(data, successCode);
    }

    public static <T> ApiResponse<T> of(final List<T> data, final SuccessCode successCode) {
        return new ApiResponse<>(data, successCode);
    }

    public static ApiResponse<HttpHeaders> of(final HttpHeaders headers, final SuccessCode successCode) {
        return new ApiResponse(headers, successCode);
    }
}
