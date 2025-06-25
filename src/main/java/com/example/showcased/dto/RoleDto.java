package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RoleDto {
    @JsonAlias("credit_id")
    private String id;
    @JsonAlias("character")
    private String name;
    private int episode_count;
}
