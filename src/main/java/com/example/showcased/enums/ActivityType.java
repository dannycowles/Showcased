package com.example.showcased.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    FOLLOW(1),
    LIKE_SHOW_REVIEW(2);

    private final int dbValue;

    ActivityType(int num) {
        this.dbValue = num;
    }
}
