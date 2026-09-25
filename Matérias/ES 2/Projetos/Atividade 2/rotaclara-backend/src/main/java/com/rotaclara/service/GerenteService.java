package com.rotaclara.service;

import com.rotaclara.domain.GerenteCoordenador;
import com.rotaclara.exception.RecursoNaoEncontradoException;
import com.rotaclara.repository.GerenteCoordenadorRepository;
import com.rotaclara.web.dto.GerenteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GerenteService {

    private final GerenteCoordenadorRepository repository;

    public GerenteService(GerenteCoordenadorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GerenteCoordenador> listar() {
        return repository.findAll();
    }

    @Transactional
    public GerenteCoordenador cadastrar(GerenteRequest request) {
        return repository.save(new GerenteCoordenador(request.nome(), request.telefone(), request.email()));
    }

    @Transactional(readOnly = true)
    public GerenteCoordenador buscarOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gerente não encontrado: " + id));
    }
}
