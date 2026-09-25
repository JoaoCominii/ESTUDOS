package com.rotaclara.web.controller;

import com.rotaclara.service.MotoristaService;
import com.rotaclara.web.dto.MotoristaRequest;
import com.rotaclara.web.dto.MotoristaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    private final MotoristaService service;

    public MotoristaController(MotoristaService service) {
        this.service = service;
    }

    @GetMapping
    public List<MotoristaResponse> listar() {
        return service.listar().stream().map(MotoristaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public MotoristaResponse buscar(@PathVariable String id) {
        return MotoristaResponse.de(service.buscarOuFalhar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MotoristaResponse cadastrar(@Valid @RequestBody MotoristaRequest request) {
        return MotoristaResponse.de(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public MotoristaResponse atualizar(@PathVariable String id, @Valid @RequestBody MotoristaRequest request) {
        return MotoristaResponse.de(service.atualizar(id, request));
    }
}
