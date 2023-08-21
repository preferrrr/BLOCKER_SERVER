package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Signature;
import com.blocker.blocker_server.entity.SignatureId;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature, SignatureId> {
    boolean existsByUser(User user);
}
