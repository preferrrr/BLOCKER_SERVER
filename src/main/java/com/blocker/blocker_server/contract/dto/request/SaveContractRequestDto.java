package com.blocker.blocker_server.contract.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveContractRequestDto {

    @NotBlank(message = "제목은 null 또는 공백일 수 없습니다.")
    private String title;
    @NotBlank(message = "내용은 null 또는 공백일 수 없습니다.")
    private String content;


    @Builder
    public SaveContractRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
