package com.rotaclara.repository;

import com.rotaclara.domain.PontoRoteiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoRoteiroRepository extends JpaRepository<PontoRoteiro, String> {
    List<PontoRoteiro> findByRoteiro_IdOrderByOrdemAsc(String roteiroId);
}
