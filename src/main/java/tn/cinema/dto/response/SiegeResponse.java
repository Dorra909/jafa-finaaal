package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SiegeResponse {
    private UUID id;
    private String rangee;
    private Integer numero;
    private UUID salleId;
}
