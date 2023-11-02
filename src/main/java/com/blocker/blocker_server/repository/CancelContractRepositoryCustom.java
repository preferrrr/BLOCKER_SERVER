package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelContract;

import java.util.Optional;

public interface CancelContractRepositoryCustom {
    Optional<CancelContract> findCancelContractWithSignsById(Long cancelContractId);

}
