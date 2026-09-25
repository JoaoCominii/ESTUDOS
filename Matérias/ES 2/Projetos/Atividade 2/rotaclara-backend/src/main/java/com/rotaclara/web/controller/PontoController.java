package com.rotaclara.web.controller;

import com.rotaclara.service.PontoService;
import com.rotaclara.web.dto.PontoRequest;
import com.rotaclara.web.dto.PontoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pontos")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PontoResponse> listar() {
        return service.listar().stream().map(PontoResponse::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PontoResponse cadastrar(@Valid @RequestBody PontoRequest request) {
        return PontoResponse.de(service.cadastrar(request));
    }
}
