package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
}
