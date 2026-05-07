package tn.cinema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FilmDetailResponse {
    private FilmResponse film;
    private List<SeanceResponse> seancesDisponibles;
}
