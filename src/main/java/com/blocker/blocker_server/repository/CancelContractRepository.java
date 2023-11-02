package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.CancelContractState;
import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelContractRepository extends JpaRepository<CancelContract, Long> {
    boolean existsByContract(Contract contract);

    List<CancelContract> findByUserAndCancelContractState(User user, CancelContractState state);

}
