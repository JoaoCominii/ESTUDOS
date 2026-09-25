package com.rotaclara.web.controller;

import com.rotaclara.domain.Parametro;
import com.rotaclara.domain.Roteiro;
import com.rotaclara.service.ParametroService;
import com.rotaclara.service.RegrasNegocioService;
import com.rotaclara.service.RoteiroService;
import com.rotaclara.web.dto.RoteiroCriarRequest;
import com.rotaclara.web.dto.RoteiroResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/roteiros")
public class RoteiroController {

    private final RoteiroService roteiroService;
    private final ParametroService parametroService;
    private final RegrasNegocioService regras;

    public RoteiroController(RoteiroService roteiroService, ParametroService parametroService, RegrasNegocioService regras) {
        this.roteiroService = roteiroService;
        this.parametroService = parametroService;
        this.regras = regras;
    }

    /** RF04 — monta o roteiro diário. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoteiroResponse criar(@Valid @RequestBody RoteiroCriarRequest request) {
        Roteiro roteiro = roteiroService.criar(request);
        return converter(roteiro);
    }

    @GetMapping("/{id}")
    public RoteiroResponse buscar(@PathVariable String id) {
        return converter(roteiroService.buscarOuFalhar(id));
    }

    /** RF07 — histórico de roteiros/pontos por período. */
    @GetMapping
    public List<RoteiroResponse> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return roteiroService.buscarPorPeriodo(inicio, fim).stream().map(this::converter).toList();
    }

    /** RF05 — o motorista registra a chegada em um ponto do roteiro. */
    @PostMapping("/{roteiroId}/pontos/{pontoRoteiroId}/chegada")
    public RoteiroResponse registrarChegada(@PathVariable String roteiroId, @PathVariable String pontoRoteiroId) {
        return converter(roteiroService.registrarChegada(roteiroId, pontoRoteiroId));
    }

    /** RF05 — o motorista registra a saída, o que dispara o cálculo do tempo parado (RN02). */
    @PostMapping("/{roteiroId}/pontos/{pontoRoteiroId}/saida")
    public RoteiroResponse registrarSaida(@PathVariable String roteiroId, @PathVariable String pontoRoteiroId) {
        return converter(roteiroService.registrarSaida(roteiroId, pontoRoteiroId));
    }

    private RoteiroResponse converter(Roteiro roteiro) {
        Parametro parametro = parametroService.buscarOuCriarPadrao();
        return RoteiroResponse.de(roteiro, parametro, regras);
    }
}
