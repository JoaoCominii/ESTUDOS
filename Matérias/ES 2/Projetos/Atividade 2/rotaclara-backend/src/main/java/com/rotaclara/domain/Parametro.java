package com.rotaclara.domain;

import jakarta.persistence.*;

/**
 * Parâmetros de custo e jornada. Existe sempre exatamente uma linha
 * (id fixo {@link #ID_UNICO}) — é intencionalmente um singleton, para
 * atender ao critério de aceitação "os parâmetros podem ser alterados
 * sem alteração de código".
 */
@Entity
@Table(name = "parametro")
public class Parametro {

    public static final String ID_UNICO = "GLOBAL";

    @Id
    private String id = ID_UNICO;

    @Column(name = "valor_combustivel_por_litro", nullable = false)
    private double valorCombustivelPorLitro;

    @Column(name = "custo_por_km", nullable = false)
    private double custoPorKm;

    /** RN04 — jornada padrão de trabalho, base percentual dos indicadores. */
    @Column(name = "jornada_padrao_horas", nullable = false)
    private double jornadaPadraoHoras = 8;

    @Column(name = "limite_alerta_parada_minutos", nullable = false)
    private int limiteAlertaParadaMinutos = 30;

    protected Parametro() {
    }

    public Parametro(double valorCombustivelPorLitro, double custoPorKm,
                      double jornadaPadraoHoras, int limiteAlertaParadaMinutos) {
        this.id = ID_UNICO;
        this.valorCombustivelPorLitro = valorCombustivelPorLitro;
        this.custoPorKm = custoPorKm;
        this.jornadaPadraoHoras = jornadaPadraoHoras;
        this.limiteAlertaParadaMinutos = limiteAlertaParadaMinutos;
    }

    public String getId() {
        return id;
    }

    public double getValorCombustivelPorLitro() {
        return valorCombustivelPorLitro;
    }

    public void setValorCombustivelPorLitro(double valorCombustivelPorLitro) {
        this.valorCombustivelPorLitro = valorCombustivelPorLitro;
    }

    public double getCustoPorKm() {
        return custoPorKm;
    }

    public void setCustoPorKm(double custoPorKm) {
        this.custoPorKm = custoPorKm;
    }

    public double getJornadaPadraoHoras() {
        return jornadaPadraoHoras;
    }

    public void setJornadaPadraoHoras(double jornadaPadraoHoras) {
        this.jornadaPadraoHoras = jornadaPadraoHoras;
    }

    public int getLimiteAlertaParadaMinutos() {
        return limiteAlertaParadaMinutos;
    }

    public void setLimiteAlertaParadaMinutos(int limiteAlertaParadaMinutos) {
        this.limiteAlertaParadaMinutos = limiteAlertaParadaMinutos;
    }
}
