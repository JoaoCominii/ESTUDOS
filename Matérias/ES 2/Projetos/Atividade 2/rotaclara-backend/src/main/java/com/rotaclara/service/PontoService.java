package com.rotaclara.service;

import com.rotaclara.domain.Ponto;
import com.rotaclara.exception.RecursoNaoEncontradoException;
import com.rotaclara.repository.PontoRepository;
import com.rotaclara.web.dto.PontoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PontoService {

    private final PontoRepository repository;

    public PontoService(PontoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Ponto> listar() {
        return repository.findAll();
    }

    @Transactional
    public Ponto cadastrar(PontoRequest request) {
        return repository.save(new Ponto(request.endereco(), request.latitude(), request.longitude()));
    }

    @Transactional(readOnly = true)
    public Ponto buscarOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto não encontrado: " + id));
    }
}
