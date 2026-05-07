package tn.cinema.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import tn.cinema.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean actif = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
