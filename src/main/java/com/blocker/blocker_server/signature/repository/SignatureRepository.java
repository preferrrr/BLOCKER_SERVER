package com.blocker.blocker_server.signature.repository;

import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.signature.domain.SignatureId;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature, SignatureId> {
    boolean existsByUser(User user);
    @Query("SELECT s FROM Signature s WHERE s.user = :user ORDER BY s.createdAt DESC LIMIT 1")
    Optional<Signature> findByUserOrderByCreatedAtDesc(@Param("user") User user);
}
