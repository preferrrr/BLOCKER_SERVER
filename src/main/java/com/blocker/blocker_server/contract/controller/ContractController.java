package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.commons.response.ListResponse;
import com.blocker.blocker_server.commons.response.SingleResponse;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.GetConcludeContractResponseDto;
import com.blocker.blocker_server.contract.service.ContractService;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetProceedContractResponseDto;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.contract.exception.InvalidContractStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 계약서 등록
     * /contracts
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse> saveContract(@AuthenticationPrincipal User user,
                                                     @RequestBody SaveContractRequestDto requestDto) {

        contractService.saveContract(user, requestDto);

        return new ResponseEntity<>(
                BaseResponse.of(HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    /**
     * 계약서 수정
     * /contracts/{contractId}
     */
    @PatchMapping("/{contractId}")
    public ResponseEntity<BaseResponse> modifyContract(@AuthenticationPrincipal User user,
                                                       @RequestBody ModifyContractRequestDto requestDto,
                                                       @PathVariable Long contractId) {

        contractService.modifyContract(user, contractId, requestDto);

        return ResponseEntity.ok(
                BaseResponse.ok()
        );
    }


    /**
     * 계약서 리스트 조회
     * /contracts
     */
    @GetMapping("")
    public ResponseEntity<ListResponse<GetContractResponseDto>> getContracts(@AuthenticationPrincipal User user,
                                                                             @RequestParam(name = "state") ContractState state) {

        if (!state.equals(ContractState.PROCEED) && !state.equals(ContractState.NOT_PROCEED) && !state.equals(ContractState.CONCLUDE))
            throw new InvalidContractStateException();

        return ResponseEntity.ok(
                ListResponse.ok(contractService.getContracts(user, state))
        );
    }

    /**
     * 미체결 계약서 조회
     * /contracts/not-proceed/{contractId}
     */
    @GetMapping("/not-proceed/{contractId}")
    public ResponseEntity<SingleResponse<GetContractResponseDto>> getContract(@PathVariable("contractId") Long contractId) {

        return ResponseEntity.ok(
                SingleResponse.ok(contractService.getNotProceedContract(contractId))
        );
    }

    /**
     * 계약서 삭제
     * /contracts/{contractId}
     */
    @DeleteMapping("/{contractId}")
    public ResponseEntity<BaseResponse> deleteContract(@AuthenticationPrincipal User user,
                                                       @PathVariable("contractId") Long contractId) {

        contractService.deleteContract(user, contractId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 진행 중 계약서 조회
     * /contracts/proceed/{contractId}
     */
    @GetMapping("/proceed/{contractId}")
    public ResponseEntity<SingleResponse<GetProceedContractResponseDto>> getProceedContract(@AuthenticationPrincipal User user,
                                                                                            @PathVariable("contractId") Long contractId) {

        return ResponseEntity.ok(
                SingleResponse.ok(contractService.getProceedContract(user, contractId))
        );
    }

    /**
     * 계약서가 포함된 게시글까지 모두 delete
     * /contracts/with-boards/{contractId}
     */
    @DeleteMapping("/with-boards/{contractId}")
    public ResponseEntity<BaseResponse> deleteContractWithBoards(@AuthenticationPrincipal User user,
                                                                 @PathVariable("contractId") Long contractId) {
        contractService.deleteContractWithBoards(user, contractId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 체결 계약서 조회
     * /contracts/conclude/{contractId}
     */
    @GetMapping("/conclude/{contractId}")
    public ResponseEntity<SingleResponse<GetConcludeContractResponseDto>> getConcludeContract(@AuthenticationPrincipal User user,
                                                                                              @PathVariable("contractId") Long contractId) {

        return ResponseEntity.ok(
                SingleResponse.ok(contractService.getConcludeContract(user, contractId))
        );
    }

}
