package tn.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.cinema.dto.response.DashboardResponse;
import tn.cinema.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService service;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return service.getDashboard();
    }
}