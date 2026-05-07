package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "sieges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 5)
    private String rangee;

    @Column(nullable = false)
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;
}
