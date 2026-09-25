package com.rotaclara.web.controller;

import com.rotaclara.service.ParametroService;
import com.rotaclara.web.dto.ParametroDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService service;

    public ParametroController(ParametroService service) {
        this.service = service;
    }

    @GetMapping
    public ParametroDto.Response buscar() {
        return ParametroDto.Response.de(service.buscarOuCriarPadrao());
    }

    @PutMapping
    public ParametroDto.Response atualizar(@Valid @RequestBody ParametroDto.Request request) {
        return ParametroDto.Response.de(service.atualizar(request));
    }
}
