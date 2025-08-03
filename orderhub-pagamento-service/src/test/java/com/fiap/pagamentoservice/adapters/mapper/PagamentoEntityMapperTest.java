package com.fiap.pagamentoservice.adapters.mapper;

import br.com.orderhub.core.domain.entities.Pagamento;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PagamentoEntityMapperTest {
    @Test
    void testEntityToDomain(){
        PagamentoEntity pagamentoEntity = new PagamentoEntity(
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                "EM_ABERTO"
        );

        Pagamento pagamentoDomain = PagamentoEntityMapper.entityToDomain(pagamentoEntity);

        assertEquals("Adamastor", pagamentoDomain.getNomeCliente());
    }

    @Test
    void testDomainToEntity(){
        Pagamento pagamendoDomain = new Pagamento(
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                StatusPagamento.EM_ABERTO
        );

        PagamentoEntity pagamentoEntity = PagamentoEntityMapper.domainToEntity(pagamendoDomain);

        assertEquals("Adamastor", pagamentoEntity.getNomeCliente());
    }
}
