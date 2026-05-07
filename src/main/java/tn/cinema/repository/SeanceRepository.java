package tn.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cinema.entity.Seance;

import java.util.List;
import java.util.UUID;

public interface SeanceRepository extends JpaRepository<Seance, UUID> {

    @Query("SELECT s FROM Seance s ORDER BY s.dateHeure DESC")
    List<Seance> findAllOrderByDate();

    @Query("SELECT s FROM Seance s WHERE s.film.id = :filmId ORDER BY s.dateHeure DESC")
    List<Seance> findByFilmId(@Param("filmId") UUID filmId);

    @Query("SELECT s FROM Seance s WHERE s.film.id = :filmId AND s.dateHeure > CURRENT_TIMESTAMP ORDER BY s.dateHeure")
    List<Seance> findFuturesByFilmId(@Param("filmId") UUID filmId);
}