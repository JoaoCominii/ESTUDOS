package com.rotaclara.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * RN05 — cada roteiro pertence a um único motorista e a uma única data.
 */
@Entity
@Table(
        name = "roteiro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"motorista_id", "data"})
)
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista;

    @OneToMany(mappedBy = "roteiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PontoRoteiro> pontos = new ArrayList<>();

    @Column(name = "distancia_total_km")
    private double distanciaTotalKm;

    /**
     * Campos de cache, recalculados pelo serviço de domínio (RN03, RN07)
     * sempre que um ponto do roteiro é atualizado. Mantidos na tabela
     * para que o dashboard (RNF03 — resposta &lt; 3s) não precise
     * recalcular tudo a cada consulta de período.
     */
    @Column(name = "tempo_total_parado_minutos")
    private int tempoTotalParadoMinutos;

    @Column(name = "custo_estimado")
    private double custoEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusRoteiro status = StatusRoteiro.PLANEJADO;

    protected Roteiro() {
    }

    public Roteiro(LocalDate data, Motorista motorista, double distanciaTotalKm) {
        this.data = data;
        this.motorista = motorista;
        this.distanciaTotalKm = distanciaTotalKm;
    }

    /** Pontos do roteiro, sempre em ordem sequencial (RN06). */
    public List<PontoRoteiro> getPontosOrdenados() {
        return pontos.stream()
                .sorted(Comparator.comparingInt(PontoRoteiro::getOrdem))
                .toList();
    }

    public void adicionarPonto(PontoRoteiro pontoRoteiro) {
        pontoRoteiro.setRoteiro(this);
        this.pontos.add(pontoRoteiro);
    }

    public String getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public List<PontoRoteiro> getPontos() {
        return pontos;
    }

    public double getDistanciaTotalKm() {
        return distanciaTotalKm;
    }

    public void setDistanciaTotalKm(double distanciaTotalKm) {
        this.distanciaTotalKm = distanciaTotalKm;
    }

    public int getTempoTotalParadoMinutos() {
        return tempoTotalParadoMinutos;
    }

    public void setTempoTotalParadoMinutos(int tempoTotalParadoMinutos) {
        this.tempoTotalParadoMinutos = tempoTotalParadoMinutos;
    }

    public double getCustoEstimado() {
        return custoEstimado;
    }

    public void setCustoEstimado(double custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public StatusRoteiro getStatus() {
        return status;
    }

    public void setStatus(StatusRoteiro status) {
        this.status = status;
    }
}
