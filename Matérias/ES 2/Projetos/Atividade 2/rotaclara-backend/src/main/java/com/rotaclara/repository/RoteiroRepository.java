package com.rotaclara.repository;

import com.rotaclara.domain.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoteiroRepository extends JpaRepository<Roteiro, String> {

    /** RN05 — cada roteiro pertence a um único motorista e a uma única data. */
    Optional<Roteiro> findByMotorista_IdAndData(String motoristaId, LocalDate data);

    /**
     * Usada pelo dashboard (RF08) e pelo histórico (RF07). O JOIN FETCH
     * evita N+1 ao carregar motorista e pontos junto — importante para
     * o requisito de resposta &lt; 3s em consultas de até 12 meses
     * (RNF03).
     */
    @Query(
            "select distinct r from Roteiro r "
                    + "join fetch r.motorista "
                    + "left join fetch r.pontos p "
                    + "left join fetch p.ponto "
                    + "where r.data between :inicio and :fim "
                    + "order by r.data desc"
    )
    List<Roteiro> buscarPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    List<Roteiro> findByMotorista_IdAndDataBetween(String motoristaId, LocalDate inicio, LocalDate fim);
}
