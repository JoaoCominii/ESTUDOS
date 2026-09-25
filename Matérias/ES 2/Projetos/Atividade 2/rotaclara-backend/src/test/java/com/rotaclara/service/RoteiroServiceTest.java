package com.rotaclara.service;

import com.rotaclara.domain.Motorista;
import com.rotaclara.domain.Parametro;
import com.rotaclara.domain.Ponto;
import com.rotaclara.domain.Roteiro;
import com.rotaclara.exception.RegraDeNegocioException;
import com.rotaclara.repository.MotoristaRepository;
import com.rotaclara.repository.PontoRepository;
import com.rotaclara.repository.PontoRoteiroRepository;
import com.rotaclara.repository.RoteiroRepository;
import com.rotaclara.web.dto.RoteiroCriarRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoteiroServiceTest {

    @Mock private RoteiroRepository roteiroRepository;
    @Mock private MotoristaRepository motoristaRepository;
    @Mock private PontoRepository pontoRepository;
    @Mock private PontoRoteiroRepository pontoRoteiroRepository;
    @Mock private ParametroService parametroService;

    private RoteiroService service;

    private Motorista motorista;
    private Ponto pontoPartida;
    private Ponto pontoDois;

    @BeforeEach
    void setUp() {
        service = new RoteiroService(
                roteiroRepository, motoristaRepository, pontoRepository,
                pontoRoteiroRepository, new RegrasNegocioService(), parametroService
        );
        motorista = new Motorista("Carlos Mendes", "31999990001", "111", "Moto", 35);
        pontoPartida = new Ponto("Seg. Família", -19.92, -43.93);
        pontoDois = new Ponto("Rua Peru, 55", -19.94, -43.93);
    }

    @Test
    void rn05_naoPermiteDoisRoteirosParaOMesmoMotoristaNaMesmaData() {
        LocalDate data = LocalDate.of(2026, 9, 23);
        when(motoristaRepository.findById("mot-1")).thenReturn(Optional.of(motorista));
        when(roteiroRepository.findByMotorista_IdAndData("mot-1", data))
                .thenReturn(Optional.of(new Roteiro(data, motorista, 10)));

        var request = new RoteiroCriarRequest(
                data, "mot-1", 18.4,
                List.of(new RoteiroCriarRequest.PontoRoteiroItemRequest("pt-1", 1))
        );

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("RN05");
    }

    @Test
    void rn06_ordemDosPontosDeveSerSequencialComecandoEm1() {
        LocalDate data = LocalDate.of(2026, 9, 23);
        when(motoristaRepository.findById("mot-1")).thenReturn(Optional.of(motorista));
        when(roteiroRepository.findByMotorista_IdAndData("mot-1", data)).thenReturn(Optional.empty());

        // ordens 1 e 3, pulando o 2 — inválido
        var request = new RoteiroCriarRequest(
                data, "mot-1", 18.4,
                List.of(
                        new RoteiroCriarRequest.PontoRoteiroItemRequest("pt-1", 1),
                        new RoteiroCriarRequest.PontoRoteiroItemRequest("pt-2", 3)
                )
        );

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("RN06");
    }

    @Test
    void criarRoteiroValidoCalculaTotaisEChamaRepositorio() {
        LocalDate data = LocalDate.of(2026, 9, 23);
        when(motoristaRepository.findById("mot-1")).thenReturn(Optional.of(motorista));
        when(roteiroRepository.findByMotorista_IdAndData("mot-1", data)).thenReturn(Optional.empty());
        when(pontoRepository.findById("pt-1")).thenReturn(Optional.of(pontoPartida));
        when(pontoRepository.findById("pt-2")).thenReturn(Optional.of(pontoDois));
        when(parametroService.buscarOuCriarPadrao()).thenReturn(new Parametro(6.19, 0.42, 8, 30));
        when(roteiroRepository.save(any(Roteiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new RoteiroCriarRequest(
                data, "mot-1", 18.4,
                List.of(
                        new RoteiroCriarRequest.PontoRoteiroItemRequest("pt-1", 1),
                        new RoteiroCriarRequest.PontoRoteiroItemRequest("pt-2", 2)
                )
        );

        Roteiro roteiro = service.criar(request);

        assertThat(roteiro.getPontos()).hasSize(2);
        assertThat(roteiro.getCustoEstimado()).isEqualTo(7.73); // 18.4 * 0.42
        verify(roteiroRepository).save(any(Roteiro.class));
    }
}
