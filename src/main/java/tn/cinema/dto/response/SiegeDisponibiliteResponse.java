package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SiegeDisponibiliteResponse {
    private UUID id;
    private String rangee;
    private Integer numero;
    private String statut; // LIBRE ou OCCUPE
}
