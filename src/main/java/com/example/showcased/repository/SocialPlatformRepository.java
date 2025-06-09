package com.example.showcased.repository;

import com.example.showcased.entity.SocialPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialPlatformRepository extends JpaRepository<SocialPlatform, Long> {
}
