package com.rotaclara.service;

import com.rotaclara.domain.Roteiro;
import com.rotaclara.domain.StatusRoteiro;
import com.rotaclara.web.dto.DashboardResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * RF08 — monta o painel com os gráficos de tempo parado por dia, mês
 * ou período. O "recorte" (dia/mês/período) é resolvido pelo
 * controller antes de chamar este serviço: aqui só recebemos o
 * intervalo [inicio, fim] já calculado.
 */
@Service
public class DashboardService {

    private final RoteiroService roteiroService;

    public DashboardService(RoteiroService roteiroService) {
        this.roteiroService = roteiroService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse montar(LocalDate inicio, LocalDate fim) {
        List<Roteiro> roteiros = roteiroService.buscarPorPeriodo(inicio, fim);

        int tempoTotal = roteiros.stream().mapToInt(Roteiro::getTempoTotalParadoMinutos).sum();
        double custoTotal = roteiros.stream().mapToDouble(Roteiro::getCustoEstimado).sum();
        long concluidos = roteiros.stream().filter(r -> r.getStatus() == StatusRoteiro.CONCLUIDO).count();
        int media = roteiros.isEmpty() ? 0 : tempoTotal / roteiros.size();

        DashboardResponse.Kpis kpis = new DashboardResponse.Kpis(
                tempoTotal, custoTotal, (int) concluidos, roteiros.size(), media
        );

        Map<LocalDate, Integer> porDia = new TreeMap<>();
        for (Roteiro r : roteiros) {
            porDia.merge(r.getData(), r.getTempoTotalParadoMinutos(), Integer::sum);
        }
        List<DashboardResponse.PontoDoGrafico> grafico = porDia.entrySet().stream()
                .map(e -> new DashboardResponse.PontoDoGrafico(e.getKey(), e.getValue()))
                .toList();

        List<DashboardResponse.RoteiroResumo> resumos = roteiros.stream()
                .sorted(Comparator.comparing(Roteiro::getData).reversed())
                .map(r -> new DashboardResponse.RoteiroResumo(
                        r.getId(),
                        r.getMotorista().getNome(),
                        r.getPontos().size(),
                        r.getTempoTotalParadoMinutos(),
                        r.getCustoEstimado(),
                        r.getStatus().name()
                ))
                .collect(Collectors.toList());

        return new DashboardResponse(kpis, grafico, resumos);
    }
}
