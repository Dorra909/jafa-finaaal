package tn.cinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.request.ReservationRequest;
import tn.cinema.dto.response.ReservationResponse;
import tn.cinema.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> creerReservation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creerReservation(userDetails.getUsername(), request));
    }

    @GetMapping("/mes-reservations")
    public ResponseEntity<List<ReservationResponse>> getMesReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reservationService.getReservationsClient(userDetails.getUsername()));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponse> annulerReservation(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reservationService.annulerReservation(id, userDetails.getUsername()));
    }
}
