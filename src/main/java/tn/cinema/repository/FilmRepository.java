package tn.cinema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cinema.entity.Film;

import java.util.Optional;
import java.util.UUID;

public interface FilmRepository extends JpaRepository<Film, UUID> {

    @Query("SELECT f FROM Film f WHERE f.actif = true AND (:genre IS NULL OR f.genre = :genre) AND (:q IS NULL OR LOWER(f.titre) LIKE LOWER(CONCAT('%', cast(:q as string), '%')))")
    Page<Film> findAllActifsWithFilters(@Param("genre") String genre, @Param("q") String q, Pageable pageable);

    @Query("SELECT f FROM Film f WHERE f.actif = true AND f.id = :id")
    Optional<Film> findByIdAndActif(@Param("id") UUID id);
}
