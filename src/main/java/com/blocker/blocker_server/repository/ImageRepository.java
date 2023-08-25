package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
