package com.example.showcased.repository;

import com.example.showcased.entity.UserCharacterRanking;
import com.example.showcased.entity.UserCharacterRankingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCharacterRankingRepository extends JpaRepository<UserCharacterRanking, UserCharacterRankingId> {
}
