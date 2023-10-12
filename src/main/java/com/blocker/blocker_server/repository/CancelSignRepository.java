package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelSign;
import com.blocker.blocker_server.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelSignRepository extends JpaRepository<CancelSign, Long> {
    boolean existsByContract(Contract contract);
}
