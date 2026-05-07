package tn.cinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.request.SalleRequest;
import tn.cinema.dto.response.SalleResponse;
import tn.cinema.service.SalleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService salleService;

    @GetMapping
    public ResponseEntity<List<SalleResponse>> getAllSalles() {
        return ResponseEntity.ok(salleService.getAllSalles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleResponse> getSalleById(@PathVariable UUID id) {
        return ResponseEntity.ok(salleService.getSalleById(id));
    }

    @PostMapping
    public ResponseEntity<SalleResponse> createSalle(@Valid @RequestBody SalleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salleService.createSalle(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleResponse> updateSalle(@PathVariable UUID id, @Valid @RequestBody SalleRequest request) {
        return ResponseEntity.ok(salleService.updateSalle(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable UUID id) {
        salleService.deleteSalle(id);
        return ResponseEntity.noContent().build();
    }


}
