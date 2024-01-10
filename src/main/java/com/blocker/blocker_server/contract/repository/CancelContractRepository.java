package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CancelContractRepository extends JpaRepository<CancelContract, Long>, CancelContractRepositoryCustom {
    boolean existsByContract(Contract contract);

    List<CancelContract> findByUserAndCancelContractState(User user, CancelContractState state);

    CancelContract findByContract(Contract contract);

}
