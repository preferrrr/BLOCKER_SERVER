package com.blocker.blocker_server.user.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "이름은 null 또는 공백일 수 없습니다.")
    private String name;
    private String picture;

}
