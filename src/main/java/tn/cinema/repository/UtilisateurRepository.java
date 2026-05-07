package tn.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cinema.entity.Utilisateur;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {

    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email")
    Optional<Utilisateur> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Utilisateur u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.createdAt >= :startDate")
    long countNewClientsLast30Days(@Param("startDate") LocalDate startDate);
}
