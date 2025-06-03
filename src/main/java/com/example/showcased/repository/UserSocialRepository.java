package com.example.showcased.repository;

import com.example.showcased.dto.SocialAccountReturnDto;
import com.example.showcased.entity.UserSocial;
import com.example.showcased.entity.UserSocialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSocialRepository extends JpaRepository<UserSocial, UserSocialId> {
    @Query("SELECT new com.example.showcased.dto.SocialAccountReturnDto(s.id, s.name, u.handle, s.url) " +
            "FROM SocialPlatform s " +
            "LEFT JOIN UserSocial u ON u.id.socialId = s.id AND u.id.userId = :userId " +
            "ORDER BY s.id ASC")
    List<SocialAccountReturnDto> findByIdUserId(@Param("userId") Long userId);
}
