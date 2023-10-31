package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelContractRepository extends JpaRepository<CancelContract, Long> {
    boolean existsByContract(Contract contract);

}
