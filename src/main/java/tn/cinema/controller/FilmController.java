package tn.cinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.request.FilmRequest;
import tn.cinema.dto.response.FilmDetailResponse;
import tn.cinema.dto.response.FilmResponse;
import tn.cinema.service.FilmService;

import java.util.UUID;

@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Page<FilmResponse>> getAllFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(filmService.getAllFilms(page, size, genre, q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDetailResponse> getFilmById(@PathVariable UUID id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<FilmResponse> createFilm(@Valid @RequestBody FilmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmResponse> updateFilm(@PathVariable UUID id, @Valid @RequestBody FilmRequest request) {
        return ResponseEntity.ok(filmService.updateFilm(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable UUID id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}
