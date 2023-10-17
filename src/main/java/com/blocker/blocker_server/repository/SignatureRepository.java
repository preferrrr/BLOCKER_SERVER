package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Signature;
import com.blocker.blocker_server.entity.SignatureId;
import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature, SignatureId> {
    boolean existsByUser(User user);
    @Query("SELECT s FROM Signature s WHERE s.user = :user ORDER BY s.createdAt DESC LIMIT 1")
    Optional<Signature> findByUserOrderByCreatedAtDesc(@Param("user") User user);
}
