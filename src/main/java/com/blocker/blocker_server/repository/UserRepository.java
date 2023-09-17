package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshtokenValue(String value);

}
