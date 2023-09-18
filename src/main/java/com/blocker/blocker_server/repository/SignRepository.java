package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.Sign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignRepository extends JpaRepository<Sign, Long> {
    boolean existsByContract(Contract contract);
}
