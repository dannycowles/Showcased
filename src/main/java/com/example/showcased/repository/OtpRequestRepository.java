package com.example.showcased.repository;

import com.example.showcased.entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long> {
}
