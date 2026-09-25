package com.rotaclara.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record RoteiroCriarRequest(
        @NotNull LocalDate data,
        @NotBlank String motoristaId,
        @Positive double distanciaTotalKm,
        @NotEmpty @Valid List<PontoRoteiroItemRequest> pontos
) {
    /** Um item da lista ordenada de pontos do roteiro (RN06). */
    public record PontoRoteiroItemRequest(
            @NotBlank String pontoId,
            @Positive int ordem
    ) {
    }
}
