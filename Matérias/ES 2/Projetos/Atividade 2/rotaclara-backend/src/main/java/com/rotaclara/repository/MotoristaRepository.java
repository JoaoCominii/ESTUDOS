package com.rotaclara.repository;

import com.rotaclara.domain.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotoristaRepository extends JpaRepository<Motorista, String> {
    Optional<Motorista> findByDocumento(String documento);
}
