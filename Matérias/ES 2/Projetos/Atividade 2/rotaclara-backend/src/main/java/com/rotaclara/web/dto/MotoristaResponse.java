package com.rotaclara.web.dto;

import com.rotaclara.domain.Motorista;

public record MotoristaResponse(
        String id,
        String nome,
        String telefone,
        String documento,
        String veiculo,
        double rendimentoKmLitro
) {
    public static MotoristaResponse de(Motorista m) {
        return new MotoristaResponse(
                m.getId(), m.getNome(), m.getTelefone(), m.getDocumento(),
                m.getVeiculo(), m.getRendimentoKmLitro()
        );
    }
}
