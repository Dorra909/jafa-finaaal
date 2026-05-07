package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SeanceResponse {
    private UUID id;
    private LocalDateTime dateHeure;
    private BigDecimal prixDt;
    private UUID filmId;
    private String filmTitre;
    private UUID salleId;
    private String salleNom;
}
