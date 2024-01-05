package com.blocker.blocker_server.sign.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelSignRepository extends JpaRepository<CancelSign, Long> {

    List<CancelSign> findByCancelContract(CancelContract cancelContract);

    CancelSign findByUserAndCancelContract(User user, CancelContract cancelContract);
}
