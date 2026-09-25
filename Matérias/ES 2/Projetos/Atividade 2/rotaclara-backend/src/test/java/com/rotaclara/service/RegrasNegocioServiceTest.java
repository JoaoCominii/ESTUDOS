package com.rotaclara.service;

import com.rotaclara.domain.*;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegrasNegocioServiceTest {

    private final RegrasNegocioService regras = new RegrasNegocioService();

    private Parametro parametroPadrao() {
        return new Parametro(6.19, 0.42, 8, 30);
    }

    @Test
    void rn01_pontoDePartidaNuncaContaTempoParado() {
        Ponto ponto = new Ponto("Seg. Família", -19.92, -43.93);
        PontoRoteiro partida = new PontoRoteiro(ponto, 1);
        partida.setDataHoraChegada(OffsetDateTime.parse("2026-09-23T08:00:00-03:00"));
        partida.setDataHoraSaida(OffsetDateTime.parse("2026-09-23T08:40:00-03:00")); // mesmo com "saída" tardia

        assertThat(regras.recalcularTempoParadoPonto(partida)).isZero();
    }

    @Test
    void rn02_tempoParadoEhDiferencaEntreSaidaEChegada() {
        Ponto ponto = new Ponto("Rua Peru, 55", -19.94, -43.93);
        PontoRoteiro p2 = new PontoRoteiro(ponto, 2);
        p2.setDataHoraChegada(OffsetDateTime.parse("2026-09-23T08:22:00-03:00"));
        p2.setDataHoraSaida(OffsetDateTime.parse("2026-09-23T08:37:00-03:00"));

        assertThat(regras.recalcularTempoParadoPonto(p2)).isEqualTo(15);
    }

    @Test
    void rn02_pontoEmAbertoNaoTemTempoParadoCalculado() {
        Ponto ponto = new Ponto("Av. João César, 340", -19.90, -43.94);
        PontoRoteiro p4 = new PontoRoteiro(ponto, 4);
        p4.setDataHoraChegada(OffsetDateTime.now());
        // sem data de saída ainda

        assertThat(regras.recalcularTempoParadoPonto(p4)).isNull();
    }

    @Test
    void rn03_tempoTotalParadoSomaTodosOsPontosExcetoAPartida() {
        Motorista motorista = new Motorista("Carlos Mendes", "31999990001", "111", "Moto", 35);
        Roteiro roteiro = new Roteiro(java.time.LocalDate.now(), motorista, 18.4);

        adicionarPontoComTempos(roteiro, 1, null, null); // partida
        adicionarPontoComTempos(roteiro, 2, "08:22", "08:37"); // 15 min
        adicionarPontoComTempos(roteiro, 3, "08:51", "09:01"); // 10 min
        adicionarPontoComTempos(roteiro, 4, "09:20", "10:10"); // 50 min

        assertThat(regras.calcularTempoTotalParadoRoteiro(roteiro)).isEqualTo(75);
    }

    @Test
    void rn07_custoDoRoteiroEhDistanciaVezesCustoPorKm() {
        Motorista motorista = new Motorista("Carlos Mendes", "31999990001", "111", "Moto", 35);
        Roteiro roteiro = new Roteiro(java.time.LocalDate.now(), motorista, 18.4);
        Parametro parametro = parametroPadrao(); // custoPorKm = 0.42

        double custo = regras.calcularCustoRoteiro(roteiro, parametro);

        assertThat(custo).isEqualTo(7.73); // 18.4 * 0.42, arredondado
    }

    @Test
    void rn07_custoPorKmEhCombustivelDivididoPorRendimento() {
        double custoPorKm = regras.calcularCustoPorKm(6.19, 35);
        assertThat(custoPorKm).isEqualTo(0.18); // 6.19 / 35, arredondado
    }

    @Test
    void pontoEmAlertaQuandoTempoParadoPassaDoLimiteParametrizado() {
        Ponto ponto = new Ponto("Av. João César, 340", -19.90, -43.94);
        PontoRoteiro p4 = new PontoRoteiro(ponto, 4);
        p4.setDataHoraChegada(OffsetDateTime.parse("2026-09-23T09:20:00-03:00"));
        p4.setDataHoraSaida(OffsetDateTime.parse("2026-09-23T10:10:00-03:00")); // 50 min
        regras.recalcularTempoParadoPonto(p4);

        assertThat(regras.pontoEmAlerta(p4, parametroPadrao())).isTrue(); // limite é 30 min
    }

    private void adicionarPontoComTempos(Roteiro roteiro, int ordem, String chegada, String saida) {
        Ponto ponto = new Ponto("Ponto " + ordem, 0, 0);
        PontoRoteiro pr = new PontoRoteiro(ponto, ordem);
        if (chegada != null) {
            pr.setDataHoraChegada(OffsetDateTime.parse("2026-09-23T" + chegada + ":00-03:00"));
            pr.setDataHoraSaida(OffsetDateTime.parse("2026-09-23T" + saida + ":00-03:00"));
        }
        roteiro.adicionarPonto(pr);
    }
}
