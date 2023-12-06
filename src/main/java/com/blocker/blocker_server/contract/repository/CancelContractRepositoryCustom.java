package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;

import java.util.Optional;

public interface CancelContractRepositoryCustom {
    Optional<CancelContract> findCancelContractWithSignsById(Long cancelContractId);

}
