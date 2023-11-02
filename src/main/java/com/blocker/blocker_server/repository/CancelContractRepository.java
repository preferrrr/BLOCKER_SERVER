package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.CancelContractState;
import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CancelContractRepository extends JpaRepository<CancelContract, Long>, CancelContractRepositoryCustom {
    boolean existsByContract(Contract contract);

    List<CancelContract> findByUserAndCancelContractState(User user, CancelContractState state);

}
