package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "films")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer dureeMinutes;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String afficheUrl;

    @Column(nullable = false)
    private boolean actif = true;
}
