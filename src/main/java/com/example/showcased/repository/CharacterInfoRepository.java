package com.example.showcased.repository;

import com.example.showcased.entity.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, String> {
}
