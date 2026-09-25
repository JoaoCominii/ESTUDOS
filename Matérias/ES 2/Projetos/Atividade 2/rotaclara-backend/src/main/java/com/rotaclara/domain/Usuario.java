package com.rotaclara.domain;

import jakarta.persistence.*;

/**
 * Conta de acesso ao sistema. É mantida separada de {@link Motorista} e
 * {@link GerenteCoordenador} de propósito: nem todo motorista/gerente
 * precisa necessariamente de login (por ex. em uma fase de rollout
 * gradual), e um Administrador não tem cadastro operacional próprio na
 * especificação — só existe como perfil de acesso.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    /** Preenchido apenas quando perfil = MOTORISTA. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    /** Preenchido apenas quando perfil = GERENTE. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente_id")
    private GerenteCoordenador gerente;

    protected Usuario() {
        // exigido pelo JPA
    }

    public Usuario(String login, String senhaHash, PerfilUsuario perfil) {
        this.login = login;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public GerenteCoordenador getGerente() {
        return gerente;
    }

    public void setGerente(GerenteCoordenador gerente) {
        this.gerente = gerente;
    }
}
