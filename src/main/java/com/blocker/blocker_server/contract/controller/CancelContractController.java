package com.blocker.blocker_server.contract.controller;

import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.service.CancelContractService;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<GetCancelContractResponseDto>> getCancelContractList(@AuthenticationPrincipal User user,
                                                                                    @RequestParam(name = "state") CancelContractState state) {

        List<GetCancelContractResponseDto> response = cancelContractService.getCancelContractList(user, state);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 파기 진행 중 계약서 조회
     * /cancel-contracts/canceling/{cancelContractId}
     */
    @GetMapping("/canceling/{cancelContractId}")
    public ResponseEntity<GetCancelContractWithSignStateResponseDto> getCancelingContract(@AuthenticationPrincipal User user,
                                                                                          @PathVariable Long cancelContractId) {

        GetCancelContractWithSignStateResponseDto response = cancelContractService.getCancelingContract(user, cancelContractId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 파기 체결 계약서 조회
     * /cancel-contracts/canceled/{cancelContractId}
     * */
    @GetMapping("/canceled/{cancelContractId}")
    public ResponseEntity<GetCancelContractWithSignStateResponseDto> getCanceledContract(@AuthenticationPrincipal User user,
                                                                                         @PathVariable Long cancelContractId) {

        GetCancelContractWithSignStateResponseDto response = cancelContractService.getCanceledContract(user, cancelContractId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
