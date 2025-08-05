package com.fiap.pagamentoservice.adapters.gateway;


import br.com.orderhub.core.domain.entities.Pagamento;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoEntity;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoRepositoryJpaGatewayImplTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoRepositoryJpaGatewayImpl gateway;

    @Test
    void deveGerarOrdemPagamentoComSucesso() throws Exception {
        Pagamento pagamento = new Pagamento(1L,"Cliente", "email@email.com", 100.0, StatusPagamento.EM_ABERTO);
        PagamentoEntity entity = new PagamentoEntity(
                1L,
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                "EM_ABERTO"
        );
        when(pagamentoRepository.save(any(PagamentoEntity.class))).thenReturn(entity);

        Pagamento resultado = gateway.gerarOrdemPagamento(pagamento);

        assertNotNull(resultado);
        assertEquals(pagamento.getStatus(), resultado.getStatus());
    }

    @Test
    void deveFecharOrdemPagamentoComSucesso() {
        Long id = 1L;
        PagamentoEntity entity = new PagamentoEntity(
                1L,
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                "EM_ABERTO"
        );
        entity.setStatusPagamento(StatusPagamento.EM_ABERTO.toString());

        when(pagamentoRepository.findById(id)).thenReturn(Optional.of(entity));
        when(pagamentoRepository.save(any())).thenReturn(entity);

        Pagamento resultado = gateway.fecharOrdemPagamento(id, StatusPagamento.FECHADO_COM_SUCESSO);

        assertNotNull(resultado);
        assertEquals(StatusPagamento.FECHADO_COM_SUCESSO, resultado.getStatus());
    }

    @Test
    void deveRetornarNull_quandoFecharPagamentoInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Pagamento resultado = gateway.fecharOrdemPagamento(99L, StatusPagamento.FECHADO_COM_SUCESSO);

        assertNull(resultado);
    }

    @Test
    void deveLancarExcecao_quandoPagamentoJaFechado() {
        PagamentoEntity entity = new PagamentoEntity(
                1L,
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                "FECHADO_COM_SUCESSO"
        );

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> gateway.fecharOrdemPagamento(1L, StatusPagamento.FECHADO_COM_SUCESSO));
    }

    @Test
    void deveBuscarPagamentoPorIdComSucesso() {
        Long id = 1L;
        PagamentoEntity entity = new PagamentoEntity(
                1L,
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                "EM_ABERTO"
        );
        when(pagamentoRepository.findById(id)).thenReturn(Optional.of(entity));
        when(pagamentoRepository.save(entity)).thenReturn(entity);

        Pagamento resultado = gateway.buscarOrderPagamentoPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveRetornarNull_quandoBuscarPagamentoPorIdInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Pagamento resultado = gateway.buscarOrderPagamentoPorId(99L);

        assertNull(resultado);
    }
}