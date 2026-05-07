package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal prixDt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;
}
