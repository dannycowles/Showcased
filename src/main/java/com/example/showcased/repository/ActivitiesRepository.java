package com.example.showcased.repository;

import com.example.showcased.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitiesRepository extends JpaRepository<Activity,Long> {
}
