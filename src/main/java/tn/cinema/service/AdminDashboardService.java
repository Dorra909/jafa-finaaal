package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cinema.dto.response.DashboardResponse;
import tn.cinema.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final ReservationRepository reservationRepository;

    public DashboardResponse getDashboard() {

        long total = reservationRepository.countAllReservations();
        long pending = reservationRepository.countPending();
        long confirmed = reservationRepository.countConfirmed();
        long cancelled = reservationRepository.countCancelled();

        return DashboardResponse.builder()
                .totalReservations(total)
                .enAttente(pending)
                .confirmees(confirmed)
                .annulees(cancelled)

                // TEMP DATA (you can replace later with real analytics)
                .series7j(List.of(12, 18, 9, 22, 15, 27, 25))
                .dayLabels(List.of("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"))
                .byWeekday(List.of(8, 14, 11, 16, 22, 30, 27))

                .tauxConfirmation(total == 0 ? 0 : (confirmed * 100.0 / total))
                .revenuMoyen(185)
                .dureeAttenteMoyenne("2.4h")
                .satisfactionScore(4.3)
                .topSource("Site web")
                .nouveauxClients(23)           // replace with real query
                .nouveauxClientsTrend("+12% vs mois dernier")

                .build();
    }
}