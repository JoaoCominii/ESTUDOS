package com.rotaclara.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Catálogo de pontos (endereço + coordenadas), reutilizável entre
 * roteiros de dias diferentes.
 * <p>
 * A especificação original (seção 8) descreve um único "Ponto" que já
 * carrega ordem, chegada e saída — aqui isso foi normalizado em dois
 * conceitos: este catálogo (RF03) e {@link PontoRoteiro}, que é a
 * ocorrência de um Ponto dentro de um Roteiro específico (RF04/RF05).
 * Isso evita recadastrar endereço/coordenadas a cada novo roteiro que
 * passa pelo mesmo lugar.
 */
@Entity
@Table(name = "ponto")
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    protected Ponto() {
    }

    public Ponto(String endereco, double latitude, double longitude) {
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
