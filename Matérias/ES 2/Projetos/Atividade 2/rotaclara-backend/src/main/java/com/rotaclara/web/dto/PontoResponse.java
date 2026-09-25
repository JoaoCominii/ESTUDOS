package com.rotaclara.web.dto;

import com.rotaclara.domain.Ponto;

public record PontoResponse(String id, String endereco, double latitude, double longitude) {
    public static PontoResponse de(Ponto p) {
        return new PontoResponse(p.getId(), p.getEndereco(), p.getLatitude(), p.getLongitude());
    }
}
