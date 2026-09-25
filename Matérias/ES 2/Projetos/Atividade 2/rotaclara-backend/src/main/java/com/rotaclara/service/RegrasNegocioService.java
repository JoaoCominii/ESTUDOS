package com.rotaclara.service;

import com.rotaclara.domain.Parametro;
import com.rotaclara.domain.PontoRoteiro;
import com.rotaclara.domain.Roteiro;
import org.springframework.stereotype.Service;

/**
 * Implementação das regras de negócio RN01–RN07 da especificação.
 * Mantidas isoladas dos controllers e repositórios para poderem ser
 * testadas isoladamente (ver {@code RegrasNegocioServiceTest}) e para
 * espelhar exatamente a mesma lógica já usada no front-end
 * (src/domain/regras.ts), evitando que as duas pontas calculem tempo
 * parado e custo de formas diferentes.
 */
@Service
public class RegrasNegocioService {

    /**
     * RN01 + RN02 — recalcula o tempo parado de um ponto do roteiro e
     * grava o resultado na própria entidade (campo de cache).
     */
    public Integer recalcularTempoParadoPonto(PontoRoteiro pontoRoteiro) {
        return pontoRoteiro.recalcularTempoParado();
    }

    /**
     * RN03 — o tempo total parado do roteiro é a soma dos tempos
     * parados de todos os pontos, exceto o ponto de partida. Pontos
     * ainda em aberto (sem saída registrada) não entram na soma.
     */
    public int calcularTempoTotalParadoRoteiro(Roteiro roteiro) {
        int total = 0;
        for (PontoRoteiro p : roteiro.getPontos()) {
            if (p.isPartida()) continue;
            Integer tempo = recalcularTempoParadoPonto(p);
            total += (tempo != null ? tempo : 0);
        }
        return total;
    }

    /**
     * RN07 — o custo do trajeto é calculado a partir do custo por km
     * já parametrizado (derivado de combustível ÷ rendimento) e da
     * distância percorrida.
     */
    public double calcularCustoRoteiro(Roteiro roteiro, Parametro parametro) {
        double custo = roteiro.getDistanciaTotalKm() * parametro.getCustoPorKm();
        return Math.round(custo * 100) / 100.0;
    }

    /**
     * Auxiliar de RN07: deriva o custo por km a partir do valor do
     * combustível e do rendimento (km/litro) de um veículo ou da frota.
     */
    public double calcularCustoPorKm(double valorCombustivelPorLitro, double rendimentoKmLitro) {
        if (rendimentoKmLitro <= 0) return 0;
        return Math.round((valorCombustivelPorLitro / rendimentoKmLitro) * 100) / 100.0;
    }

    /**
     * RN04 — a jornada padrão serve de base percentual para os
     * indicadores de tempo parado.
     */
    public double percentualDaJornadaParado(int tempoParadoMinutos, Parametro parametro) {
        double jornadaMinutos = parametro.getJornadaPadraoHoras() * 60;
        if (jornadaMinutos <= 0) return 0;
        return tempoParadoMinutos / jornadaMinutos;
    }

    /** Indica se um ponto passou do limite de alerta parametrizado. */
    public boolean pontoEmAlerta(PontoRoteiro pontoRoteiro, Parametro parametro) {
        Integer tempo = pontoRoteiro.getTempoParadoMinutos();
        return tempo != null && tempo > parametro.getLimiteAlertaParadaMinutos();
    }

    /**
     * Recalcula e grava, na própria entidade Roteiro, os campos de
     * cache (tempo total parado e custo estimado), a partir das regras
     * acima. Deve ser chamado sempre que um ponto do roteiro for
     * criado/atualizado.
     */
    public void recalcularTotaisRoteiro(Roteiro roteiro, Parametro parametro) {
        roteiro.setTempoTotalParadoMinutos(calcularTempoTotalParadoRoteiro(roteiro));
        roteiro.setCustoEstimado(calcularCustoRoteiro(roteiro, parametro));
    }
}
