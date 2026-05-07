package tn.cinema.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilmRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "Le genre est obligatoire")
    private String genre;

    @Min(value = 1, message = "La durée doit être positive")
    private Integer dureeMinutes;

    private String synopsis;
    private String afficheUrl;
}
