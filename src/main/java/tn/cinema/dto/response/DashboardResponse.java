package tn.cinema.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {
    private long totalReservations;
    private long enAttente;
    private long confirmees;
    private long annulees;
    private List<Integer> series7j;
    private List<String> dayLabels;
    private List<Integer> byWeekday;

    private double tauxConfirmation;
    private double revenuMoyen;
    private String dureeAttenteMoyenne;
    private double satisfactionScore;
    private String topSource;
    private long nouveauxClients;
    private String nouveauxClientsTrend;
}
