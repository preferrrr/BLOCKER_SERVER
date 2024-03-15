package com.blocker.blocker_server.contract.controller;

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
    public ResponseEntity<HttpStatus> saveContract(@AuthenticationPrincipal User user,
                                                   @RequestBody SaveContractRequestDto requestDto) {

        contractService.saveContract(user, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 계약서 수정
     * /contracts/{contractId}
     */
    @PatchMapping("/{contractId}")
    public ResponseEntity<HttpStatus> modifyContract(@AuthenticationPrincipal User user,
                                                     @RequestBody ModifyContractRequestDto requestDto,
                                                     @PathVariable Long contractId) {

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
            throw new InvalidContractStateException();


        return new ResponseEntity<>(
                contractService.getContracts(user, state),
                HttpStatus.OK
        );
    }

    /**
     * 미체결 계약서 조회
     * /contracts/not-proceed/{contractId}
     */
    @GetMapping("/not-proceed/{contractId}")
    public ResponseEntity<GetContractResponseDto> getContract(@PathVariable("contractId") Long contractId) {

        return new ResponseEntity<>(
                contractService.getNotProceedContract(contractId),
                HttpStatus.OK
        );
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

        return new ResponseEntity<>(
                contractService.getProceedContract(user, contractId),
                HttpStatus.OK
        );
    }

    /**
     * 계약서가 포함된 게시글까지 모두 delete
     * /contracts/with-boards/{contractId}
     */
    @DeleteMapping("/with-boards/{contractId}")
    public ResponseEntity<HttpStatus> deleteContractWithBoards(@AuthenticationPrincipal User user,
                                                               @PathVariable("contractId") Long contractId) {
        contractService.deleteContractWithBoards(user, contractId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 체결 계약서 조회
     * /contracts/conclude/{contractId}
     */
    @GetMapping("/conclude/{contractId}")
    public ResponseEntity<GetConcludeContractResponseDto> getConcludeContract(@AuthenticationPrincipal User user,
                                                                              @PathVariable("contractId") Long contractId) {

        return new ResponseEntity<>(
                contractService.getConcludeContract(user, contractId),
                HttpStatus.OK
        );
    }

}
