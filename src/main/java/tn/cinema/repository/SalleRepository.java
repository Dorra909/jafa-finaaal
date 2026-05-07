package tn.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.cinema.entity.Salle;

import java.util.List;
import java.util.UUID;

public interface SalleRepository extends JpaRepository<Salle, UUID> {

    @Query("SELECT s FROM Salle s WHERE s.actif = true")
    List<Salle> findAllActives();
}
