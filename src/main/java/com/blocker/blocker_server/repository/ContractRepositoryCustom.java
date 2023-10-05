package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.ContractState;
import com.blocker.blocker_server.entity.User;

import java.util.List;

public interface ContractRepositoryCustom {

    List<Contract> findNotProceedContracts(User user, ContractState state);

}
