package tn.cinema.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SeanceRequest {

    @NotNull(message = "La date/heure est obligatoire")
    private LocalDateTime dateHeure;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.001", message = "Le prix doit être positif")
    private BigDecimal prixDt;

    @NotNull(message = "L'identifiant du film est obligatoire")
    private UUID filmId;

    @NotNull(message = "L'identifiant de la salle est obligatoire")
    private UUID salleId;
}
