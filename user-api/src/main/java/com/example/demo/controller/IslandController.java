package com.example.demo.controller;

import com.example.demo.controller.dto.NewIslandDTO;
import com.example.demo.application.IslandApplicationService;
import com.example.demo.repository.entity.Island;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/islands")
public class IslandController {
    private final IslandApplicationService islandApplicationService;

    public IslandController(IslandApplicationService islandApplicationService) {
        this.islandApplicationService = islandApplicationService;
    }

    @PostMapping
    public ResponseEntity<Island> create(@Valid @RequestBody NewIslandDTO dto) {
        var response = islandApplicationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/allocate/{userId}")
    public ResponseEntity<Void> allocateWorkstation(@Valid @PathVariable Integer userId) {
        islandApplicationService.alocarWorkstationDisponivel(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Island>> listAll() {
        var islands = islandApplicationService.listarIslandsComDisponibilidade();
        return ResponseEntity.ok(islands);
    }
}