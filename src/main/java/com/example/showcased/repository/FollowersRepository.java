package com.example.showcased.repository;

import com.example.showcased.entity.Follower;
import com.example.showcased.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowersRepository extends JpaRepository<Follower, FollowerId> {
}
