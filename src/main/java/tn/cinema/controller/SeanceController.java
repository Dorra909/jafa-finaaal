package tn.cinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.request.SeanceRequest;
import tn.cinema.dto.response.SeanceResponse;
import tn.cinema.dto.response.SiegeDisponibiliteResponse;
import tn.cinema.service.SeanceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceController {

    private final SeanceService seanceService;

    @GetMapping
    public ResponseEntity<List<SeanceResponse>> getAllSeances(
            @RequestParam(required = false) UUID filmId) {
        return ResponseEntity.ok(seanceService.getAllSeances(filmId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeanceResponse> getSeanceById(@PathVariable UUID id) {
        return ResponseEntity.ok(seanceService.getSeanceById(id));
    }

    @PostMapping
    public ResponseEntity<SeanceResponse> createSeance(@Valid @RequestBody SeanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seanceService.createSeance(request));
    }

    @GetMapping("/{id}/sieges")
    public ResponseEntity<List<SiegeDisponibiliteResponse>> getDisponibiliteSieges(@PathVariable UUID id) {
        return ResponseEntity.ok(seanceService.getDisponibiliteSieges(id));
    }
}
