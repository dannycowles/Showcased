package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RoleDto {
    @JsonAlias("credit_id")
    private String id;
    private String character;
    private int episode_count;
}
