package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface CancelContractRepositoryCustom {
    Optional<CancelContract> findCancelContractWithSignsById(Long cancelContractId);

    List<CancelContract> findCancelContractsByUserAndState(User user, CancelContractState state);

}
