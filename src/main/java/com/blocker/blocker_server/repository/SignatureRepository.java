package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Signature;
import com.blocker.blocker_server.entity.SignatureId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepository extends JpaRepository<Signature, SignatureId> {
}
