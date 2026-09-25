package com.rotaclara.web.dto;

import com.rotaclara.domain.GerenteCoordenador;

public record GerenteResponse(String id, String nome, String telefone, String email) {
    public static GerenteResponse de(GerenteCoordenador g) {
        return new GerenteResponse(g.getId(), g.getNome(), g.getTelefone(), g.getEmail());
    }
}
