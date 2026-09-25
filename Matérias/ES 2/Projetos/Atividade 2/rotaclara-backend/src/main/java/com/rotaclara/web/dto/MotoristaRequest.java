package com.rotaclara.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MotoristaRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank String documento,
        @NotBlank String veiculo,
        @Positive double rendimentoKmLitro
) {
}
