package com.rotaclara.web.controller;

import com.rotaclara.service.DashboardService;
import com.rotaclara.web.dto.DashboardResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * RF08 — o cliente (front-end) decide o recorte (dia/mês/período) e
 * envia o intervalo [inicio, fim] já calculado; este endpoint apenas
 * agrega os dados desse intervalo.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse montar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return dashboardService.montar(inicio, fim);
    }
}
