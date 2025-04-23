package com.example.showcased.repository;

import com.example.showcased.entity.EpisodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeInfoRepository extends JpaRepository<EpisodeInfo, Long> {
}
