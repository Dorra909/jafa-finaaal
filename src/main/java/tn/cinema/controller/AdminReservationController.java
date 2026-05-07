package tn.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.response.ReservationResponse;
import tn.cinema.enums.StatutReservation;
import tn.cinema.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(
                reservationService.getAllReservations());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ReservationResponse>> getByStatut(
            @PathVariable StatutReservation statut) {

        return ResponseEntity.ok(
                reservationService.getReservationsByStatut(statut));
    }

    @PutMapping("/{id}/confirmer")
    public ResponseEntity<ReservationResponse> confirmer(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reservationService.confirmerReservation(id));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponse> annuler(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reservationService.annulerReservationAdmin(id));
    }
}