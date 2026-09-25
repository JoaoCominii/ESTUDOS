package com.rotaclara.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PontoRequest(
        @NotBlank String endereco,
        double latitude,
        double longitude
) {
}
