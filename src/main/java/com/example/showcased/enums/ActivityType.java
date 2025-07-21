package com.example.showcased.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    FOLLOW(1),
    LIKE_SHOW_REVIEW(2),
    COMMENT_SHOW_REVIEW(3),
    LIKE_EPISODE_REVIEW(4),
    COMMENT_EPISODE_REVIEW(5),
    LIKE_SHOW_REVIEW_COMMENT(6),
    LIKE_EPISODE_REVIEW_COMMENT(7);

    private final int dbValue;

    ActivityType(int num) {
        this.dbValue = num;
    }
}
