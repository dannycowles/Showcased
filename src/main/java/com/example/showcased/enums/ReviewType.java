package com.example.showcased.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewType {
    SHOW("Show"),
    EPISODE("Episode");

    private final String type;

    ReviewType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
