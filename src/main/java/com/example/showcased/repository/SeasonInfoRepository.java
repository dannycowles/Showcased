package com.example.showcased.repository;

import com.example.showcased.entity.SeasonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonInfoRepository extends JpaRepository<SeasonInfo, Long> {
}
