package tn.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.cinema.entity.Siege;

import java.util.List;
import java.util.UUID;

public interface SiegeRepository extends JpaRepository<Siege, UUID> {

    @Query("SELECT s FROM Siege s WHERE s.salle.id = :salleId ORDER BY s.rangee, s.numero")
    List<Siege> findBySalleId(@Param("salleId") UUID salleId);
}
