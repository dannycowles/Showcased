package com.example.showcased.repository;

import com.example.showcased.entity.ShowInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowInfoRepository extends JpaRepository<ShowInfo, Long> {
}
