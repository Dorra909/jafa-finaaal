package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cinema.dto.request.ReservationRequest;
import tn.cinema.dto.response.ReservationResponse;
import tn.cinema.dto.response.SiegeResponse;
import tn.cinema.entity.Reservation;
import tn.cinema.entity.Seance;
import tn.cinema.entity.Siege;
import tn.cinema.entity.Utilisateur;
import tn.cinema.enums.StatutReservation;
import tn.cinema.exception.BusinessException;
import tn.cinema.exception.ResourceNotFoundException;
import tn.cinema.exception.SiegeDejaReserveException;
import tn.cinema.repository.ReservationRepository;
import tn.cinema.repository.SeanceRepository;
import tn.cinema.repository.SiegeRepository;
import tn.cinema.repository.UtilisateurRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeanceRepository seanceRepository;
    private final SiegeRepository siegeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmailService emailService;        // ← Ajouté

    @Transactional
    public ReservationResponse creerReservation(String clientEmail, ReservationRequest request) {
        Utilisateur client = utilisateurRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Seance seance = seanceRepository.findById(request.getSeanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée : " + request.getSeanceId()));

        List<Siege> sieges = siegeRepository.findAllById(request.getSiegeIds());

        if (sieges.size() != request.getSiegeIds().size()) {
            throw new ResourceNotFoundException("Un ou plusieurs sièges n'existent pas");
        }

        UUID salleSeance = seance.getSalle().getId();
        for (Siege siege : sieges) {
            if (!siege.getSalle().getId().equals(salleSeance)) {
                throw new BusinessException("Le siège " + siege.getRangee() + siege.getNumero() + " n'appartient pas à la salle de cette séance");
            }

            boolean dejaReserve = reservationRepository.existsReservationForSiegeAndSeance(
                    seance.getId(), siege.getId(),
                    List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE));

            if (dejaReserve) {
                throw new SiegeDejaReserveException("Le siège " + siege.getRangee() + siege.getNumero() + " est déjà réservé");
            }
        }

        Reservation reservation = Reservation.builder()
                .client(client)
                .seance(seance)
                .sieges(sieges)
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getReservationsClient(String clientEmail) {
        Utilisateur client = utilisateurRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return reservationRepository.findByClientId(client.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse annulerReservation(UUID reservationId, String clientEmail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée : " + reservationId));

        if (!reservation.getClient().getEmail().equals(clientEmail)) {
            throw new BusinessException("Vous n'êtes pas autorisé à annuler cette réservation");
        }

        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new BusinessException("Cette réservation est déjà annulée");
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse confirmerReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée"));

        if (reservation.getStatut() == StatutReservation.CONFIRMEE) {
            throw new BusinessException("Réservation déjà confirmée");
        }

        reservation.setStatut(StatutReservation.CONFIRMEE);
        Reservation savedReservation = reservationRepository.save(reservation);

        // ==================== ENVOI EMAIL AVEC QR CODE ====================
        try {
            emailService.sendConfirmationEmail(savedReservation);
            System.out.println("✅ Email avec QR Code envoyé à : " + savedReservation.getClient().getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Erreur lors de l'envoi de l'email : " + e.getMessage());
            // On ne bloque pas la confirmation si l'email échoue
        }

        return toResponse(savedReservation);
    }

    @Transactional
    public ReservationResponse annulerReservationAdmin(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée"));

        reservation.setStatut(StatutReservation.ANNULEE);
        return toResponse(reservationRepository.save(reservation));
    }

    private ReservationResponse toResponse(Reservation r) {
        List<SiegeResponse> siegesResponse = r.getSieges().stream()
                .map(s -> SiegeResponse.builder()
                        .id(s.getId())
                        .rangee(s.getRangee())
                        .numero(s.getNumero())
                        .salleId(s.getSalle().getId())
                        .build())
                .collect(Collectors.toList());

        return ReservationResponse.builder()
                .id(r.getId())
                .statut(r.getStatut())
                .createdAt(r.getCreatedAt())
                .clientId(r.getClient().getId())
                .clientNom(r.getClient().getNom())
                .seanceId(r.getSeance().getId())
                .seanceDateHeure(r.getSeance().getDateHeure())
                .filmTitre(r.getSeance().getFilm().getTitre())
                .sieges(siegesResponse)
                .build();
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAllReservations()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}