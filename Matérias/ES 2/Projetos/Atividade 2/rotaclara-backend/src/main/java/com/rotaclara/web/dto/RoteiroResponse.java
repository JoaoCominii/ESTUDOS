package com.rotaclara.web.dto;

import com.rotaclara.domain.Roteiro;
import com.rotaclara.domain.Parametro;
import com.rotaclara.service.RegrasNegocioService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record RoteiroResponse(
        String id,
        LocalDate data,
        String motoristaId,
        String motoristaNome,
        double distanciaTotalKm,
        int tempoTotalParadoMinutos,
        double custoEstimado,
        String status,
        List<PontoRoteiroResponse> pontos
) {
    public static RoteiroResponse de(Roteiro r, Parametro parametro, RegrasNegocioService regras) {
        List<PontoRoteiroResponse> pontos = r.getPontosOrdenados().stream()
                .map(p -> PontoRoteiroResponse.de(p, parametro, regras))
                .toList();
        return new RoteiroResponse(
                r.getId(),
                r.getData(),
                r.getMotorista().getId(),
                r.getMotorista().getNome(),
                r.getDistanciaTotalKm(),
                r.getTempoTotalParadoMinutos(),
                r.getCustoEstimado(),
                r.getStatus().name(),
                pontos
        );
    }

    public record PontoRoteiroResponse(
            String id,
            String pontoId,
            String endereco,
            double latitude,
            double longitude,
            int ordem,
            OffsetDateTime dataHoraChegada,
            OffsetDateTime dataHoraSaida,
            Integer tempoParadoMinutos,
            boolean emAlerta
    ) {
        static PontoRoteiroResponse de(
                com.rotaclara.domain.PontoRoteiro p,
                Parametro parametro,
                RegrasNegocioService regras
        ) {
            return new PontoRoteiroResponse(
                    p.getId(),
                    p.getPonto().getId(),
                    p.getPonto().getEndereco(),
                    p.getPonto().getLatitude(),
                    p.getPonto().getLongitude(),
                    p.getOrdem(),
                    p.getDataHoraChegada(),
                    p.getDataHoraSaida(),
                    p.getTempoParadoMinutos(),
                    regras.pontoEmAlerta(p, parametro)
            );
        }
    }
}
