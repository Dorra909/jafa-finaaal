package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import tn.cinema.enums.StatutReservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    @ManyToMany
    @JoinTable(
        name = "reservation_siege",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "siege_id")
    )
    @Builder.Default
    private List<Siege> sieges = new ArrayList<>();
}
