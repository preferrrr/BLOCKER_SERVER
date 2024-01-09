package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;

import java.util.List;
import java.util.Optional;

public interface ContractRepositoryCustom {

    List<Contract> findNotProceedContractsByUserEmailAndContractState(String email, ContractState state);

    Optional<Contract> findProceedContractWithSignById(Long contractId);

    List<Contract> findProceedOrConcludeContractListByUserEmailAndContractState(String email, ContractState state);

}
