package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cinema.dto.request.SeanceRequest;
import tn.cinema.dto.response.SeanceResponse;
import tn.cinema.dto.response.SiegeDisponibiliteResponse;
import tn.cinema.entity.Film;
import tn.cinema.entity.Salle;
import tn.cinema.entity.Seance;
import tn.cinema.exception.ResourceNotFoundException;
import tn.cinema.repository.FilmRepository;
import tn.cinema.repository.ReservationRepository;
import tn.cinema.repository.SalleRepository;
import tn.cinema.repository.SeanceRepository;
import tn.cinema.repository.SiegeRepository;
import tn.cinema.enums.StatutReservation;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final FilmRepository filmRepository;
    private final SalleRepository salleRepository;
    private final SiegeRepository siegeRepository;
    private final ReservationRepository reservationRepository;

    public List<SeanceResponse> getAllSeances(UUID filmId) {
        List<Seance> seances = (filmId != null)
                ? seanceRepository.findByFilmId(filmId)
                : seanceRepository.findAllOrderByDate();

        return seances.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SeanceResponse getSeanceById(UUID id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée : " + id));
        return toResponse(seance);
    }

    public SeanceResponse createSeance(SeanceRequest request) {
        Film film = filmRepository.findByIdAndActif(request.getFilmId())
                .orElseThrow(() -> new ResourceNotFoundException("Film non trouvé : " + request.getFilmId()));
        Salle salle = salleRepository.findById(request.getSalleId())
                .orElseThrow(() -> new ResourceNotFoundException("Salle non trouvée : " + request.getSalleId()));

        Seance seance = Seance.builder()
                .dateHeure(request.getDateHeure())
                .prixDt(request.getPrixDt())
                .film(film)
                .salle(salle)
                .build();

        return toResponse(seanceRepository.save(seance));
    }

    public List<SiegeDisponibiliteResponse> getDisponibiliteSieges(UUID seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée : " + seanceId));

        List<UUID> occupes = reservationRepository.findOccupiedSiegeIds(
                seanceId,
                List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE)
        );
        Set<UUID> occupesSet = Set.copyOf(occupes);

        return siegeRepository.findBySalleId(seance.getSalle().getId()).stream()
                .map(s -> SiegeDisponibiliteResponse.builder()
                        .id(s.getId())
                        .rangee(s.getRangee())
                        .numero(s.getNumero())
                        .statut(occupesSet.contains(s.getId()) ? "OCCUPE" : "LIBRE")
                        .build())
                .collect(Collectors.toList());
    }

    private SeanceResponse toResponse(Seance seance) {
        return SeanceResponse.builder()
                .id(seance.getId())
                .dateHeure(seance.getDateHeure())
                .prixDt(seance.getPrixDt())
                .filmId(seance.getFilm().getId())
                .filmTitre(seance.getFilm().getTitre())
                .salleId(seance.getSalle().getId())
                .salleNom(seance.getSalle().getNom())
                .build();
    }
}