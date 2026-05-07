package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.cinema.enums.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReservationResponse {
    private UUID id;
    private StatutReservation statut;
    private LocalDateTime createdAt;
    private UUID clientId;
    private String clientNom;
    private UUID seanceId;
    private LocalDateTime seanceDateHeure;
    private String filmTitre;
    private List<SiegeResponse> sieges;
}
