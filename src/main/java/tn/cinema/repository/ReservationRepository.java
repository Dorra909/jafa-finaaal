package tn.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cinema.entity.Reservation;
import tn.cinema.enums.StatutReservation;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId ORDER BY r.createdAt DESC")
    List<Reservation> findByClientId(@Param("clientId") UUID clientId);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        JOIN r.sieges s
        WHERE r.seance.id = :seanceId
          AND s.id = :siegeId
          AND r.statut IN :statuts
        """)
    boolean existsReservationForSiegeAndSeance(
        @Param("seanceId") UUID seanceId,
        @Param("siegeId") UUID siegeId,
        @Param("statuts") List<StatutReservation> statuts
    );

    @Query("""
        SELECT s.id FROM Reservation r
        JOIN r.sieges s
        WHERE r.seance.id = :seanceId
          AND r.statut IN :statuts
        """)
    List<UUID> findOccupiedSiegeIds(
        @Param("seanceId") UUID seanceId,
        @Param("statuts") List<StatutReservation> statuts
    );

    @Query("""
    SELECT r FROM Reservation r
    ORDER BY r.createdAt DESC
    """)
    List<Reservation> findAllReservations();
    List<Reservation> findByStatut(StatutReservation statut);
    @Query("""
    SELECT r FROM Reservation r
    WHERE LOWER(r.seance.film.titre)
    LIKE LOWER(CONCAT('%', :titre, '%'))
    """)
    List<Reservation> searchByFilm(@Param("titre") String titre);
    @Query("""
    SELECT r FROM Reservation r
    WHERE LOWER(r.client.nom)
    LIKE LOWER(CONCAT('%', :nom, '%'))
    """)
    List<Reservation> searchByClient(@Param("nom") String nom);

    @Query("SELECT COUNT(r) FROM Reservation r")
    long countAllReservations();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statut = 'EN_ATTENTE'")
    long countPending();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statut = 'CONFIRMEE'")
    long countConfirmed();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.statut = 'ANNULEE'")
    long countCancelled();

}
