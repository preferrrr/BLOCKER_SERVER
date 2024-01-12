package com.blocker.blocker_server.contract.service;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.dto.response.CancelContractorAndSignState;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractResponseDto;
import com.blocker.blocker_server.contract.dto.response.GetCancelContractWithSignStateResponseDto;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelContractService {

    private final CancelContractServiceSupport contractServiceSupport;

    public List<GetCancelContractResponseDto> getCancelContractList(User me, CancelContractState state) {

        //파기 계약서 리스트 조회
        List<CancelContract> cancelContracts = contractServiceSupport.getCancelContractsByUserAndState(me, state);

        //dto로 변환해서 반환
        return contractServiceSupport.entityListToDtoList(cancelContracts);
    }



    public GetCancelContractWithSignStateResponseDto getCancelingContract(User me, Long cancelContractId) {

        //조회할 계약서
        CancelContract cancelContract = contractServiceSupport.getCancelContractWithSignsById(cancelContractId);

        //파기 진행 중 계약서인지 검사
        contractServiceSupport.checkIsCancelingCancelContract(cancelContract);

        //파기 계약 참여자인지 검사
        contractServiceSupport.checkIsCancelContractParticipant(me, cancelContract);

        //참여자 서명들
        List<CancelContractorAndSignState> contractorAndSignStates = contractServiceSupport.getCancelContractorAndSignState(cancelContract.getCancelSigns());

        return GetCancelContractWithSignStateResponseDto.of(cancelContract, contractorAndSignStates);
    }

    public GetCancelContractWithSignStateResponseDto getCanceledContract(User me, Long cancelContractId) {

        CancelContract cancelContract = contractServiceSupport.getCancelContractWithSignsById(cancelContractId);

        //파기 체결된 계약서가 아니면 예외 반환
        contractServiceSupport.checkIsCanceledCancelContract(cancelContract);

        //파기 계약 참여자인지 검사
        contractServiceSupport.checkIsCancelContractParticipant(me, cancelContract);

        //참여자 서명들
        List<CancelContractorAndSignState> contractorAndSignStates = contractServiceSupport.getCancelContractorAndSignState(cancelContract.getCancelSigns());

        return GetCancelContractWithSignStateResponseDto.of(cancelContract, contractorAndSignStates);
    }
}
