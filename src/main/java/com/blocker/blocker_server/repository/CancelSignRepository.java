package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.CancelSign;
import com.blocker.blocker_server.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelSignRepository extends JpaRepository<CancelSign, Long> {

    List<CancelSign> findByCancelContract(CancelContract cancelContract);
}
