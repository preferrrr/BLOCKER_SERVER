package com.blocker.blocker_server.sign.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.sign.dto.request.ProceedSignRequestDto;
import com.blocker.blocker_server.sign.service.AgreementSignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.AgreementSignResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agreement-signs")
public class AgreementSignController {

    private final AgreementSignService agreementSignService;

    // rest api 관점에서 /signs 관련 uri로 들어가는 것이 맞을까 ?
    // 계약서의 상태를 바꾸고 계약의 엔티티를 create 함
    // 계약서의 상태를 바꾼다는 것에 초점을 두면 PATCH /contracts/{contractId}로 할 수 있겠고,
    // 계약 테이블에 create 한다는 것에 초점을 두면 POST /signs가 될 수 있겠다.
    // 나는 SIGN 테이블에 초점을 둔다.

    /**
     * 계약 진행.
     * 참여자들을 초대하고, 계약서의 상태도 진행 중으로 바뀜.
     * /signs
     */
    @PostMapping("")
    public ApiResponse<Void> proceedContract(@RequestBody @Valid ProceedSignRequestDto request) {

        agreementSignService.proceedContract(request);

        return ApiResponse.of(POST_SIGN);
    }


    /**
     * 서명
     * /signs/contract/{contractId}
     */
    @PatchMapping("/contract/{contractId}")
    public ApiResponse<Void> signContract(@PathVariable("contractId") Long contractId) {

        agreementSignService.signContract(contractId);

        return ApiResponse.of(SIGN_CONTRACT);
    }

    /**
     * 진행 중 계약 파기
     * /signs/contract/{contractId}
     */
    @DeleteMapping("/contract/{contractId}")
    public ApiResponse<Void> breakContract(@PathVariable("contractId") Long contractId) {

        agreementSignService.breakContract(contractId);

        return ApiResponse.of(DELETE_CONTRACT);

    }

}
