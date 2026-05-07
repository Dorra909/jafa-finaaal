package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FilmResponse {
    private UUID id;
    private String titre;
    private String genre;
    private Integer dureeMinutes;
    private String synopsis;
    private String afficheUrl;
    private boolean actif;
}
