package com.example.showcased.repository;

import com.example.showcased.entity.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionsRepository extends JpaRepository<UserCollection, Long> {
}
