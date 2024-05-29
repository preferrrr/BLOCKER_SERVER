package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.service.CancelContractService;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.CancelContractResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cancel-contracts")
public class CancelContractController {

    private final CancelContractService cancelContractService;

    /**
     * 파기 계약서 리스트 조회
     * /cancel-contracts
     */
    @GetMapping("")
    public ApiResponse<GetCancelContractResponseDto> getCancelContractList(@RequestParam(name = "state") CancelContractState state) {

        return ApiResponse.of(
                cancelContractService.getCancelContractList(state),
                GET_CANCEL_CONTRACT_LIST
        );
    }

    /**
     * 파기 진행 중 계약서 조회
     * /cancel-contracts/canceling/{cancelContractId}
     */
    @GetMapping("/canceling/{cancelContractId}")
    public ApiResponse<GetCancelContractWithSignStateResponseDto> getCancelingContract(@PathVariable Long cancelContractId) {

        return ApiResponse.of(
                cancelContractService.getCancelingContract(cancelContractId),
                GET_CANCELING_CONTRACT
        );

    }

    /**
     * 파기 체결 계약서 조회
     * /cancel-contracts/canceled/{cancelContractId}
     */
    @GetMapping("/canceled/{cancelContractId}")
    public ApiResponse<GetCancelContractWithSignStateResponseDto> getCanceledContract(@PathVariable Long cancelContractId) {


        return ApiResponse.of(
                cancelContractService.getCanceledContract(cancelContractId),
                GET_CANCELED_CONTRACT
        );

    }
}
