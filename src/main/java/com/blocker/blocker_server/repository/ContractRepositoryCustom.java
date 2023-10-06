package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;

import java.util.List;
import java.util.Optional;

public interface ContractRepositoryCustom {

    List<Contract> findNotProceedContracts(User user, ContractState state);

    Optional<Contract> findProceedContractWithSignById(Long contractId);

}
