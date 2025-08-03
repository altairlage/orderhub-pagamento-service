package com.fiap.pagamentoservice.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
    Optional<PagamentoEntity> findById(Long id);
    Optional<PagamentoEntity> findByNomeCliente(String nomeCliente);
    Optional<PagamentoEntity> findByEmailCliente(String emailCliente);
}
