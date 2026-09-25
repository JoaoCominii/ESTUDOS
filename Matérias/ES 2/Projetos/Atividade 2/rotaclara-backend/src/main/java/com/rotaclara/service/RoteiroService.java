package com.rotaclara.service;

import com.rotaclara.domain.*;
import com.rotaclara.exception.RecursoNaoEncontradoException;
import com.rotaclara.exception.RegraDeNegocioException;
import com.rotaclara.repository.MotoristaRepository;
import com.rotaclara.repository.PontoRepository;
import com.rotaclara.repository.PontoRoteiroRepository;
import com.rotaclara.repository.RoteiroRepository;
import com.rotaclara.web.dto.RoteiroCriarRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class RoteiroService {

    private final RoteiroRepository roteiroRepository;
    private final MotoristaRepository motoristaRepository;
    private final PontoRepository pontoRepository;
    private final PontoRoteiroRepository pontoRoteiroRepository;
    private final RegrasNegocioService regras;
    private final ParametroService parametroService;

    public RoteiroService(
            RoteiroRepository roteiroRepository,
            MotoristaRepository motoristaRepository,
            PontoRepository pontoRepository,
            PontoRoteiroRepository pontoRoteiroRepository,
            RegrasNegocioService regras,
            ParametroService parametroService
    ) {
        this.roteiroRepository = roteiroRepository;
        this.motoristaRepository = motoristaRepository;
        this.pontoRepository = pontoRepository;
        this.pontoRoteiroRepository = pontoRoteiroRepository;
        this.regras = regras;
        this.parametroService = parametroService;
    }

    /**
     * RF04 — monta o roteiro diário associando pontos em ordem
     * sequencial a um motorista e a uma data.
     * <p>
     * RN05 — valida que não existe já um roteiro para o mesmo
     * motorista na mesma data. RN06 — valida que a ordem começa em 1
     * e não tem repetições nem buracos.
     */
    @Transactional
    public Roteiro criar(RoteiroCriarRequest request) {
        Motorista motorista = motoristaRepository.findById(request.motoristaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Motorista não encontrado: " + request.motoristaId()));

        roteiroRepository.findByMotorista_IdAndData(request.motoristaId(), request.data())
                .ifPresent(r -> {
                    throw new RegraDeNegocioException(
                            "RN05: o motorista " + motorista.getNome()
                                    + " já possui um roteiro cadastrado em " + request.data());
                });

        validarOrdemSequencial(request.pontos());

        Roteiro roteiro = new Roteiro(request.data(), motorista, request.distanciaTotalKm());

        for (RoteiroCriarRequest.PontoRoteiroItemRequest item : request.pontos()) {
            Ponto ponto = pontoRepository.findById(item.pontoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Ponto não encontrado: " + item.pontoId()));
            roteiro.adicionarPonto(new PontoRoteiro(ponto, item.ordem()));
        }

        regras.recalcularTotaisRoteiro(roteiro, parametroService.buscarOuCriarPadrao());
        return roteiroRepository.save(roteiro);
    }

    private void validarOrdemSequencial(List<RoteiroCriarRequest.PontoRoteiroItemRequest> pontos) {
        List<Integer> ordens = pontos.stream()
                .map(RoteiroCriarRequest.PontoRoteiroItemRequest::ordem)
                .sorted()
                .toList();
        for (int i = 0; i < ordens.size(); i++) {
            if (ordens.get(i) != i + 1) {
                throw new RegraDeNegocioException(
                        "RN06: a ordem dos pontos deve ser sequencial, começando em 1 (recebido: " + ordens + ")");
            }
        }
    }

    /**
     * RF05 — registra a chegada em um ponto. Para o ponto de partida
     * (ordem 1), chegada e saída são gravadas juntas, pois ele nunca
     * conta tempo parado (RN01).
     */
    @Transactional
    public Roteiro registrarChegada(String roteiroId, String pontoRoteiroId) {
        Roteiro roteiro = buscarOuFalhar(roteiroId);
        PontoRoteiro ponto = localizarPonto(roteiro, pontoRoteiroId);

        OffsetDateTime agora = OffsetDateTime.now();
        ponto.setDataHoraChegada(agora);
        if (ponto.isPartida()) {
            ponto.setDataHoraSaida(agora); // RN01: partida não acumula tempo parado
        }

        if (roteiro.getStatus() == StatusRoteiro.PLANEJADO) {
            roteiro.setStatus(StatusRoteiro.EM_ANDAMENTO);
        }

        regras.recalcularTotaisRoteiro(roteiro, parametroService.buscarOuCriarPadrao());
        return roteiroRepository.save(roteiro);
    }

    /**
     * RF05 — registra a saída em um ponto, o que aciona o cálculo do
     * tempo parado (RN02) e a atualização do total do roteiro (RN03).
     * Se for o último ponto (maior ordem), o roteiro é concluído.
     */
    @Transactional
    public Roteiro registrarSaida(String roteiroId, String pontoRoteiroId) {
        Roteiro roteiro = buscarOuFalhar(roteiroId);
        PontoRoteiro ponto = localizarPonto(roteiro, pontoRoteiroId);

        if (ponto.getDataHoraChegada() == null) {
            throw new RegraDeNegocioException(
                    "Não é possível registrar saída sem uma chegada registrada neste ponto.");
        }
        ponto.setDataHoraSaida(OffsetDateTime.now());

        int maiorOrdem = roteiro.getPontos().stream()
                .max(Comparator.comparingInt(PontoRoteiro::getOrdem))
                .map(PontoRoteiro::getOrdem)
                .orElse(0);
        if (ponto.getOrdem() == maiorOrdem) {
            roteiro.setStatus(StatusRoteiro.CONCLUIDO);
        }

        regras.recalcularTotaisRoteiro(roteiro, parametroService.buscarOuCriarPadrao());
        return roteiroRepository.save(roteiro);
    }

    /** RF07/RF08 — consulta roteiros de um período (usada por histórico e dashboard). */
    @Transactional(readOnly = true)
    public List<Roteiro> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return roteiroRepository.buscarPorPeriodo(inicio, fim);
    }

    @Transactional(readOnly = true)
    public Roteiro buscarOuFalhar(String id) {
        return roteiroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Roteiro não encontrado: " + id));
    }

    private PontoRoteiro localizarPonto(Roteiro roteiro, String pontoRoteiroId) {
        return roteiro.getPontos().stream()
                .filter(p -> p.getId().equals(pontoRoteiroId))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Ponto " + pontoRoteiroId + " não pertence ao roteiro " + roteiro.getId()));
    }
}
