package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cinema.dto.request.SalleRequest;
import tn.cinema.dto.response.SalleResponse;
import tn.cinema.dto.response.SiegeResponse;
import tn.cinema.entity.Salle;
import tn.cinema.entity.Siege;
import tn.cinema.exception.ResourceNotFoundException;
import tn.cinema.repository.SalleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalleService {

    private final SalleRepository salleRepository;

    public List<SalleResponse> getAllSalles() {
        return salleRepository.findAllActives().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SalleResponse getSalleById(UUID id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle non trouvée : " + id));
        return toResponse(salle);
    }

    @Transactional
    public SalleResponse createSalle(SalleRequest request) {
        Salle salle = Salle.builder()
                .nom(request.getNom())
                .capacite(request.getCapacite())
                .actif(true)
                .sieges(new ArrayList<>())
                .build();

        salle = salleRepository.save(salle);
        genererSieges(salle, request.getCapacite());

        return toResponse(salleRepository.save(salle));
    }

    @Transactional
    public SalleResponse updateSalle(UUID id, SalleRequest request) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle non trouvée : " + id));
        salle.setNom(request.getNom());
        return toResponse(salleRepository.save(salle));
    }

    public void deleteSalle(UUID id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle non trouvée : " + id));
        salle.setActif(false);
        salleRepository.save(salle);
    }

    private void genererSieges(Salle salle, int capacite) {
        int siegesParRangee = 10;
        char rangee = 'A';
        int count = 0;

        while (count < capacite) {
            int numero = 1;
            while (numero <= siegesParRangee && count < capacite) {
                Siege siege = Siege.builder()
                        .rangee(String.valueOf(rangee))
                        .numero(numero)
                        .salle(salle)
                        .build();
                salle.getSieges().add(siege);
                numero++;
                count++;
            }
            rangee++;
        }
    }

    private SalleResponse toResponse(Salle salle) {
        List<SiegeResponse> sieges = salle.getSieges().stream()
                .map(s -> SiegeResponse.builder()
                        .id(s.getId())
                        .rangee(s.getRangee())
                        .numero(s.getNumero())
                        .salleId(salle.getId())
                        .build())
                .collect(Collectors.toList());

        return SalleResponse.builder()
                .id(salle.getId())
                .nom(salle.getNom())
                .capacite(salle.getCapacite())
                .actif(salle.isActif())
                .sieges(sieges)
                .build();
    }
}
