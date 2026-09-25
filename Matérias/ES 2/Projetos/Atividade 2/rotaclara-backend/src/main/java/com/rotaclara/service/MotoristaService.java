package com.rotaclara.service;

import com.rotaclara.domain.Motorista;
import com.rotaclara.exception.RecursoNaoEncontradoException;
import com.rotaclara.exception.RegraDeNegocioException;
import com.rotaclara.repository.MotoristaRepository;
import com.rotaclara.web.dto.MotoristaRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotoristaService {

    private final MotoristaRepository repository;

    public MotoristaService(MotoristaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Motorista> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Motorista buscarOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado: " + id));
    }

    @Transactional
    public Motorista cadastrar(MotoristaRequest request) {
        repository.findByDocumento(request.documento()).ifPresent(m -> {
            throw new RegraDeNegocioException("Já existe um motorista cadastrado com este documento.");
        });
        Motorista motorista = new Motorista(
                request.nome(), request.telefone(), request.documento(),
                request.veiculo(), request.rendimentoKmLitro()
        );
        return repository.save(motorista);
    }

    @Transactional
    public Motorista atualizar(String id, MotoristaRequest request) {
        Motorista motorista = buscarOuFalhar(id);
        motorista.setNome(request.nome());
        motorista.setTelefone(request.telefone());
        motorista.setDocumento(request.documento());
        motorista.setVeiculo(request.veiculo());
        motorista.setRendimentoKmLitro(request.rendimentoKmLitro());
        return motorista;
    }
}
