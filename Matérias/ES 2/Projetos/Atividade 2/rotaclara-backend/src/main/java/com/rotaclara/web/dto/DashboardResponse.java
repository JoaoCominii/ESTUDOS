package com.rotaclara.web.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardResponse(
        Kpis kpis,
        List<PontoDoGrafico> tempoParadoPorDia,
        List<RoteiroResumo> roteiros
) {
    public record Kpis(
            int tempoParadoTotalMinutos,
            double custoEstimadoTotal,
            int roteirosConcluidos,
            int roteirosTotal,
            int mediaParadaPorRoteiroMinutos
    ) {
    }

    public record PontoDoGrafico(LocalDate data, int minutos) {
    }

    public record RoteiroResumo(
            String id,
            String motoristaNome,
            int quantidadePontos,
            int tempoTotalParadoMinutos,
            double custoEstimado,
            String status
    ) {
    }
}
