package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.Sign;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignRepository extends JpaRepository<Sign, Long> {
    boolean existsByContract(Contract contract);
    Optional<Sign> findByContractAndUser(Contract contract, User user);
}
