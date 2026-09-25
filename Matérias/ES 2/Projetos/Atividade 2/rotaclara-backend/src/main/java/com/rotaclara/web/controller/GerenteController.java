package com.rotaclara.web.controller;

import com.rotaclara.service.GerenteService;
import com.rotaclara.web.dto.GerenteRequest;
import com.rotaclara.web.dto.GerenteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gerentes")
public class GerenteController {

    private final GerenteService service;

    public GerenteController(GerenteService service) {
        this.service = service;
    }

    @GetMapping
    public List<GerenteResponse> listar() {
        return service.listar().stream().map(GerenteResponse::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GerenteResponse cadastrar(@Valid @RequestBody GerenteRequest request) {
        return GerenteResponse.de(service.cadastrar(request));
    }
}
