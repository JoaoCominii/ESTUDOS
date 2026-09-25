package com.rotaclara.web.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public record LoginRequest(@NotBlank String login, @NotBlank String senha) {
    }

    public record LoginResponse(String token, String perfil, String nome) {
    }
}
