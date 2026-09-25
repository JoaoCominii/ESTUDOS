package com.rotaclara.service;

import com.rotaclara.domain.Parametro;
import com.rotaclara.repository.ParametroRepository;
import com.rotaclara.web.dto.ParametroDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParametroService {

    private final ParametroRepository repository;

    public ParametroService(ParametroRepository repository) {
        this.repository = repository;
    }

    /**
     * Os parâmetros são um singleton (ver {@link Parametro#ID_UNICO}).
     * Caso ainda não tenham sido cadastrados (ex.: banco recém-criado),
     * cria um registro com valores padrão razoáveis, para que o
     * cálculo de custo (RN07) nunca falhe por falta de configuração.
     */
    @Transactional
    public Parametro buscarOuCriarPadrao() {
        return repository.findById(Parametro.ID_UNICO)
                .orElseGet(() -> repository.save(new Parametro(6.19, 0.42, 8, 30)));
    }

    @Transactional
    public Parametro atualizar(ParametroDto.Request request) {
        Parametro parametro = buscarOuCriarPadrao();
        parametro.setValorCombustivelPorLitro(request.valorCombustivelPorLitro());
        parametro.setCustoPorKm(request.custoPorKm());
        parametro.setJornadaPadraoHoras(request.jornadaPadraoHoras());
        parametro.setLimiteAlertaParadaMinutos(request.limiteAlertaParadaMinutos());
        return repository.save(parametro);
    }
}
