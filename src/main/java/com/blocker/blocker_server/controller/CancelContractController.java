package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.response.GetCancelContractResponse;
import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.CancelContractState;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.CancelContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.image.ReplicateScaleFilter;
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
    public ResponseEntity<List<GetContractResponseDto>> getCancelContractList(@AuthenticationPrincipal User user,
                                                                              @RequestParam(name = "state") CancelContractState state) {

        List<GetContractResponseDto> response = cancelContractService.getCancelContractList(user, state);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 파기 진행 중 계약서 조회
     * /cancel-contracts/canceling/{cancelContractId}
     */
    @GetMapping("/canceling/{cancelContractId}")
    public ResponseEntity<GetCancelContractResponse> getCancelingContract(@AuthenticationPrincipal User user,
                                                                          @PathVariable Long cancelContractId) {

        GetCancelContractResponse response = cancelContractService.getCancelingContract(user, cancelContractId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 파기 체결 계약서 조회
     * /cancel-contracts/canceled/{cancelContractId}
     * */
    @GetMapping("/canceled/{cancelContractId}")
    public ResponseEntity<GetCancelContractResponse> getCanceledContract(@AuthenticationPrincipal User user,
                                                                         @PathVariable Long cancelContractId) {

        GetCancelContractResponse response = cancelContractService.getCanceledContract(user, cancelContractId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
