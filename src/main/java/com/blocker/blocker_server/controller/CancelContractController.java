package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.response.GetContractResponseDto;
import com.blocker.blocker_server.entity.CancelContractState;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.CancelContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cancel-contract")
public class CancelContractController {

    private final CancelContractService cancelContractService;

    /**파기 계약서 리스트 조회*/
    @GetMapping("")
    public ResponseEntity<List<GetContractResponseDto>> getCancelContractList(@AuthenticationPrincipal User user,
                                                                              @RequestParam(name = "state") CancelContractState state) {

        List<GetContractResponseDto> response = cancelContractService.getCancelContractList(user,state);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
