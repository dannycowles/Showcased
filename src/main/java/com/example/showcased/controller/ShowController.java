package com.example.showcased.controller;

import com.example.showcased.dto.ShowDto;
import com.example.showcased.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/show")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowById(@PathVariable("id") String id) {
        ShowDto show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }
}
