package com.rotaclara.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "motorista")
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String veiculo;

    /** Rendimento do veículo em km por litro — usado no cálculo de custo (RN07). */
    @Positive
    @Column(name = "rendimento_km_litro", nullable = false)
    private double rendimentoKmLitro;

    protected Motorista() {
    }

    public Motorista(String nome, String telefone, String documento, String veiculo, double rendimentoKmLitro) {
        this.nome = nome;
        this.telefone = telefone;
        this.documento = documento;
        this.veiculo = veiculo;
        this.rendimentoKmLitro = rendimentoKmLitro;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public double getRendimentoKmLitro() {
        return rendimentoKmLitro;
    }

    public void setRendimentoKmLitro(double rendimentoKmLitro) {
        this.rendimentoKmLitro = rendimentoKmLitro;
    }
}
