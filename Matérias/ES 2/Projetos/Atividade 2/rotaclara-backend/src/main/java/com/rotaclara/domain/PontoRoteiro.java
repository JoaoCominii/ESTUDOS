package com.rotaclara.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Um ponto dentro de um roteiro específico, em uma posição sequencial
 * (RN06). Guarda a chegada/saída registradas pelo motorista (RF05) e o
 * tempo parado já calculado (RN01, RN02).
 */
@Entity
@Table(
        name = "ponto_roteiro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"roteiro_id", "ordem"})
)
public class PontoRoteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roteiro_id", nullable = false)
    private Roteiro roteiro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ponto_id", nullable = false)
    private Ponto ponto;

    /** Posição sequencial no trajeto do dia; 1 = ponto de partida (RN01, RN06). */
    @Column(nullable = false)
    private int ordem;

    @Column(name = "data_hora_chegada")
    private OffsetDateTime dataHoraChegada;

    @Column(name = "data_hora_saida")
    private OffsetDateTime dataHoraSaida;

    /**
     * Tempo parado em minutos, calculado por {@code RegrasNegocioService}
     * (RN01/RN02). Fica {@code null} enquanto o ponto está em aberto
     * (sem chegada e/ou saída registradas).
     */
    @Column(name = "tempo_parado_minutos")
    private Integer tempoParadoMinutos;

    protected PontoRoteiro() {
    }

    public PontoRoteiro(Ponto ponto, int ordem) {
        this.ponto = ponto;
        this.ordem = ordem;
    }

    public boolean isPartida() {
        return ordem == 1;
    }

    /**
     * RN02 — o tempo parado em um ponto é a diferença entre o horário
     * de saída e o horário de chegada naquele ponto. RN01 — a partida
     * nunca conta tempo parado.
     * <p>
     * Este método recalcula e também atualiza o campo de cache
     * {@code tempoParadoMinutos}, para que ele fique disponível em
     * consultas simples sem reprocessar a regra.
     */
    public Integer recalcularTempoParado() {
        if (isPartida()) {
            this.tempoParadoMinutos = 0;
            return this.tempoParadoMinutos;
        }
        if (dataHoraChegada == null || dataHoraSaida == null) {
            this.tempoParadoMinutos = null;
            return null;
        }
        long minutos = Duration.between(dataHoraChegada, dataHoraSaida).toMinutes();
        this.tempoParadoMinutos = (int) Math.max(minutos, 0);
        return this.tempoParadoMinutos;
    }

    public String getId() {
        return id;
    }

    public Roteiro getRoteiro() {
        return roteiro;
    }

    public void setRoteiro(Roteiro roteiro) {
        this.roteiro = roteiro;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public OffsetDateTime getDataHoraChegada() {
        return dataHoraChegada;
    }

    public void setDataHoraChegada(OffsetDateTime dataHoraChegada) {
        this.dataHoraChegada = dataHoraChegada;
    }

    public OffsetDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(OffsetDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public Integer getTempoParadoMinutos() {
        return tempoParadoMinutos;
    }
}
