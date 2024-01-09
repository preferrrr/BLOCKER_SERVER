package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    List<Contract> findByContractIdInAndContractState(List<Long> contractIds, ContractState state);

    @Query("select c from Contract c " +
            "join fetch c.agreementSigns s " +
            "join fetch s.user " +
            "where c.contractId = :contractId")
    Optional<Contract> findContractWithSignsByContractId(Long contractId);
}
