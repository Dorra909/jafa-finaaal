package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.cinema.dto.request.FilmRequest;
import tn.cinema.dto.response.FilmDetailResponse;
import tn.cinema.dto.response.FilmResponse;
import tn.cinema.dto.response.SeanceResponse;
import tn.cinema.entity.Film;
import tn.cinema.exception.ResourceNotFoundException;
import tn.cinema.repository.FilmRepository;
import tn.cinema.repository.SeanceRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final SeanceRepository seanceRepository;

    public Page<FilmResponse> getAllFilms(int page, int size, String genre, String q) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("titre").ascending());
        return filmRepository.findAllActifsWithFilters(genre, q, pageable)
                .map(this::toResponse);
    }

    public FilmDetailResponse getFilmById(UUID id) {
        Film film = filmRepository.findByIdAndActif(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film non trouvé : " + id));

        List<SeanceResponse> seances = seanceRepository.findFuturesByFilmId(id).stream()
                .map(s -> SeanceResponse.builder()
                        .id(s.getId())
                        .dateHeure(s.getDateHeure())
                        .prixDt(s.getPrixDt())
                        .filmId(s.getFilm().getId())
                        .filmTitre(s.getFilm().getTitre())
                        .salleId(s.getSalle().getId())
                        .salleNom(s.getSalle().getNom())
                        .build())
                .collect(Collectors.toList());

        return FilmDetailResponse.builder()
                .film(toResponse(film))
                .seancesDisponibles(seances)
                .build();
    }

    public FilmResponse createFilm(FilmRequest request) {
        Film film = Film.builder()
                .titre(request.getTitre())
                .genre(request.getGenre())
                .dureeMinutes(request.getDureeMinutes())
                .synopsis(request.getSynopsis())
                .afficheUrl(request.getAfficheUrl())
                .actif(true)
                .build();
        return toResponse(filmRepository.save(film));
    }

    public FilmResponse updateFilm(UUID id, FilmRequest request) {
        Film film = filmRepository.findByIdAndActif(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film non trouvé : " + id));

        film.setTitre(request.getTitre());
        film.setGenre(request.getGenre());
        film.setDureeMinutes(request.getDureeMinutes());
        film.setSynopsis(request.getSynopsis());
        film.setAfficheUrl(request.getAfficheUrl());

        return toResponse(filmRepository.save(film));
    }

    public void deleteFilm(UUID id) {
        Film film = filmRepository.findByIdAndActif(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film non trouvé : " + id));
        film.setActif(false);
        filmRepository.save(film);
    }

    private FilmResponse toResponse(Film film) {
        return FilmResponse.builder()
                .id(film.getId())
                .titre(film.getTitre())
                .genre(film.getGenre())
                .dureeMinutes(film.getDureeMinutes())
                .synopsis(film.getSynopsis())
                .afficheUrl(film.getAfficheUrl())
                .actif(film.isActif())
                .build();
    }
}
