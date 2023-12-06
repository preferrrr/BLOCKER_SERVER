package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
