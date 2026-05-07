package tn.cinema.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReservationRequest {

    @NotNull(message = "L'identifiant de la séance est obligatoire")
    private UUID seanceId;

    @NotEmpty(message = "La liste des sièges est obligatoire")
    private List<UUID> siegeIds;
}
