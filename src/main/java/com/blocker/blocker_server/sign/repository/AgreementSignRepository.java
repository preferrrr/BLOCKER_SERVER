package com.blocker.blocker_server.sign.repository;

import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgreementSignRepository extends JpaRepository<AgreementSign, Long> {
    boolean existsByContract(Contract contract);
    Optional<AgreementSign> findByContractAndUser(Contract contract, User user);
    List<AgreementSign> findByContract(Contract contract);
    List<AgreementSign> findByUser(User user);

    boolean existsByUserAndContract(User user, Contract contract);
}
