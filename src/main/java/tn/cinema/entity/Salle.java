package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "salles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Integer capacite;

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Siege> sieges = new ArrayList<>();
}
