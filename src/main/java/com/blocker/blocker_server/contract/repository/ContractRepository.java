package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    List<Contract> findByContractIdInAndContractState(List<Long> contractIds, ContractState state);
}
