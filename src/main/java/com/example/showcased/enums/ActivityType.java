package com.example.showcased.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    FOLLOW(1),
    LIKE_SHOW_REVIEW(2),
    COMMENT_SHOW_REVIEW(3),
    LIKE_EPISODE_REVIEW(4);

    private final int dbValue;

    ActivityType(int num) {
        this.dbValue = num;
    }
}
