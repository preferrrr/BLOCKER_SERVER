package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.commons.response.ApiResponse;
import com.blocker.blocker_server.contract.dto.request.ModifyContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.GetConcludeContractResponseDto;
import com.blocker.blocker_server.contract.service.ContractService;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.dto.request.SaveContractRequestDto;
import com.blocker.blocker_server.contract.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetProceedContractResponseDto;
import com.blocker.blocker_server.contract.exception.InvalidContractStateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.ContractResponseCode.*;


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
    public ApiResponse<Void> saveContract(@RequestBody @Valid SaveContractRequestDto requestDto) {

        contractService.saveContract(requestDto);

        return ApiResponse.of(SAVE_CONTRACT);
    }

    /**
     * 계약서 수정
     * /contracts/{contractId}
     */
    @PatchMapping("/{contractId}")
    public ApiResponse<Void> modifyContract(@RequestBody @Valid ModifyContractRequestDto requestDto,
                                                       @PathVariable Long contractId) {

        contractService.modifyContract(contractId, requestDto);

        return ApiResponse.of(MODIFY_CONTRACT);
    }


    /**
     * 계약서 리스트 조회
     * /contracts
     */
    @GetMapping("")
    public ApiResponse<GetContractResponseDto> getContracts(@RequestParam(name = "state") ContractState state) {

        if (!state.equals(ContractState.PROCEED) && !state.equals(ContractState.NOT_PROCEED) && !state.equals(ContractState.CONCLUDE))
            throw new InvalidContractStateException();

        return ApiResponse.of(
                contractService.getContracts(state),
                GET_CONTRACT_LIST
        );
    }

    /**
     * 미체결 계약서 조회
     * /contracts/not-proceed/{contractId}
     */
    @GetMapping("/not-proceed/{contractId}")
    public ApiResponse<GetContractResponseDto> getContract(@PathVariable("contractId") Long contractId) {

        return ApiResponse.of(
                contractService.getNotProceedContract(contractId),
                GET_NOT_PROCEED_CONTRACT
        );
    }

    /**
     * 계약서 삭제
     * /contracts/{contractId}
     */
    @DeleteMapping("/{contractId}")
    public ApiResponse<Void> deleteContract(@PathVariable("contractId") Long contractId) {

        contractService.deleteContract(contractId);

        return ApiResponse.of(DELETE_CONTRACT);
    }

    /**
     * 진행 중 계약서 조회
     * /contracts/proceed/{contractId}
     */
    @GetMapping("/proceed/{contractId}")
    public ApiResponse<GetProceedContractResponseDto> getProceedContract(@PathVariable("contractId") Long contractId) {

        return ApiResponse.of(
                contractService.getProceedContract(contractId),
                GET_PROCEED_CONTRACT
        );
    }

    /**
     * 계약서가 포함된 게시글까지 모두 delete
     * /contracts/with-boards/{contractId}
     */
    @DeleteMapping("/with-boards/{contractId}")
    public ApiResponse<Void> deleteContractWithBoards(@PathVariable("contractId") Long contractId) {

        contractService.deleteContractWithBoards(contractId);

        return ApiResponse.of(DELETE_CONTRACT_WITH_BOARD);
    }

    /**
     * 체결 계약서 조회
     * /contracts/conclude/{contractId}
     */
    @GetMapping("/conclude/{contractId}")
    public ApiResponse<GetConcludeContractResponseDto> getConcludeContract(@PathVariable("contractId") Long contractId) {

        return ApiResponse.of(
                contractService.getConcludeContract(contractId),
                GET_CONCLUDE_CONTRACT
        );
    }

}
