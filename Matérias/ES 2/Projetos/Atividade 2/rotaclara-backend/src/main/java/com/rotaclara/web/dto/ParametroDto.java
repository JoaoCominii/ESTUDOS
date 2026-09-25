package com.rotaclara.web.dto;

import com.rotaclara.domain.Parametro;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ParametroDto {

    public record Request(
            @Positive double valorCombustivelPorLitro,
            @Positive double custoPorKm,
            @Positive double jornadaPadraoHoras,
            @PositiveOrZero int limiteAlertaParadaMinutos
    ) {
    }

    public record Response(
            double valorCombustivelPorLitro,
            double custoPorKm,
            double jornadaPadraoHoras,
            int limiteAlertaParadaMinutos
    ) {
        public static Response de(Parametro p) {
            return new Response(
                    p.getValorCombustivelPorLitro(),
                    p.getCustoPorKm(),
                    p.getJornadaPadraoHoras(),
                    p.getLimiteAlertaParadaMinutos()
            );
        }
    }
}
