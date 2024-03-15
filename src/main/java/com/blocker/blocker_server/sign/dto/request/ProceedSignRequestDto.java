package com.blocker.blocker_server.sign.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProceedSignRequestDto {
    @NotBlank(message = "계약서 인덱스는 null 또는 공백일 수 없습니다.")
    private Long contractId;
    @Size(min = 1, message = "계약 참여할 사람은 1명보다 많아야 합니다.")
    private List<String> contractors;

    @Builder
    public ProceedSignRequestDto(Long contractId, List<String> contractors) {
        this.contractId = contractId;
        this.contractors = contractors;
    }
}
