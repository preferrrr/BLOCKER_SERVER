package com.blocker.blocker_server.user.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String name;
    private String picture;

    public void validateFieldsNotNull() {
        if(email == null || email.isEmpty() || email.isBlank())
            throw new InvalidRequestParameterException("Invalid email");
        if(name == null || name.isEmpty() || name.isBlank())
            throw new InvalidRequestParameterException("Invalid name");
        if(picture == null || picture.isEmpty() || picture.isBlank())
            throw new InvalidRequestParameterException("Invalid picture");
    }
}
