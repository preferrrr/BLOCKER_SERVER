package com.blocker.blocker_server.Image.repository;

import com.blocker.blocker_server.Image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
