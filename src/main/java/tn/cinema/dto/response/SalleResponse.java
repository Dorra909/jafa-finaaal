package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SalleResponse {
    private UUID id;
    private String nom;
    private Integer capacite;
    private boolean actif;
    private List<SiegeResponse> sieges;
}
