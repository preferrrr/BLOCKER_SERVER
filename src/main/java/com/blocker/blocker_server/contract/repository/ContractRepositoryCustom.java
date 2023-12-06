package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface ContractRepositoryCustom {

    List<Contract> findNotProceedContracts(User user, ContractState state);

    Optional<Contract> findProceedContractWithSignById(Long contractId);

}
