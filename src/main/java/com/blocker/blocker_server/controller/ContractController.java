package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.SaveModifyContractRequestDto;
import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.dto.response.GetProceedContractResponseDto;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.InvalidQueryStringException;
import com.blocker.blocker_server.service.ContractService;
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
    public ResponseEntity<HttpStatus> saveContract(@AuthenticationPrincipal User user,
                                                   @RequestBody SaveModifyContractRequestDto requestDto) {

        requestDto.validateFieldsNotNull();

        contractService.saveContract(user, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 계약서 수정
     * /contracts/{contractId}
     */
    @PatchMapping("/{contractId}")
    public ResponseEntity<HttpStatus> modifyContract(@AuthenticationPrincipal User user,
                                                     @RequestBody SaveModifyContractRequestDto requestDto,
                                                     @PathVariable Long contractId) {
        requestDto.validateFieldsNotNull();

        contractService.modifyContract(user, contractId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 계약서 리스트 조회
     * /contracts
     */
    @GetMapping("")
    public ResponseEntity<List<GetContractResponseDto>> getContracts(@AuthenticationPrincipal User user,
                                                                     @RequestParam(name = "state") ContractState state) {

        if (!state.equals(ContractState.PROCEED) && !state.equals(ContractState.NOT_PROCEED) && !state.equals(ContractState.CONCLUDE))
            throw new InvalidQueryStringException("[get contract] email, state : " + user.getEmail() + ", " + state);

        List<GetContractResponseDto> response = contractService.getContracts(user, state);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 미체결 계약서 조회
     * /contracts/not-proceed/{contractId}
     */
    @GetMapping("/not-proceed/{contractId}")
    public ResponseEntity<GetContractResponseDto> getContract(@PathVariable("contractId") Long contractId) {

        GetContractResponseDto response = contractService.getContract(contractId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 계약서 삭제
     * /contracts/{contractId}
     */
    @DeleteMapping("/{contractId}")
    public ResponseEntity<HttpStatus> deleteContract(@AuthenticationPrincipal User user,
                                                     @PathVariable("contractId") Long contractId) {
        contractService.deleteContract(user, contractId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 진행 중 계약서 조회
     * /contracts/proceed/{contractId}
     */
    @GetMapping("/proceed/{contractId}")
    public ResponseEntity<GetProceedContractResponseDto> getProceedContract(@AuthenticationPrincipal User user,
                                                                            @PathVariable("contractId") Long contractId) {
        GetProceedContractResponseDto response = contractService.getProceedContract(user, contractId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/with-boards/{contractId}")
    public ResponseEntity<HttpStatus> deleteContractWithBoards(@AuthenticationPrincipal User user,
                                                               @PathVariable("contractId") Long contractId) {
        contractService.deleteContractWithBoards(user, contractId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
