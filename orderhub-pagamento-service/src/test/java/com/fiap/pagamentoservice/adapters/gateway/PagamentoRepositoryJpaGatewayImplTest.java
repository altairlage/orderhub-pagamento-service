package com.fiap.pagamentoservice.adapters.gateway;


import br.com.orderhub.core.domain.entities.Pagamento;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoEntity;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoRepositoryJpaGatewayImplTest {

    @InjectMocks
    private PagamentoRepositoryJpaGatewayImpl pagamentoGateway;

    @Mock
    private PagamentoRepository pagamentoRepository;

    private Pagamento pagamentoDomain;
    private PagamentoEntity pagamentoEntity;

    @BeforeEach
    void setup() {
        pagamentoDomain = new Pagamento(
                1L,
                1L,
                "Jorge",
                "jorge@email.com",
                1500.0,
                StatusPagamento.EM_ABERTO
        );

        pagamentoEntity = new PagamentoEntity(
                1L,
                1L,
                "Jorge",
                "jorge@email.com",
                1500.0,
                "EM_ABERTO"
        );
    }

    @Test
    void deveGerarOrdemPagamentoComSucesso() throws Exception {
        
        when(pagamentoRepository.save(any())).thenReturn(pagamentoEntity);

        
        Pagamento result = pagamentoGateway.gerarOrdemPagamento(pagamentoDomain);

        // Assert
        assertNotNull(result);
        assertEquals(StatusPagamento.EM_ABERTO, result.getStatus());
        verify(pagamentoRepository).save(any());
    }

    @Test
    void deveLancarExcecaoAoFecharPagamentoJaFechado() {
        
        pagamentoEntity.setStatusPagamento("FECHADO_COM_SUCESSO");
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoEntity));

        
        assertThrows(IllegalArgumentException.class, () ->
                pagamentoGateway.fecharOrdemPagamento(1L, StatusPagamento.FECHADO_COM_SUCESSO));
    }

    @Test
    void deveRetornarPagamentoPorIdComSucesso() {
        
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoEntity));

        
        Pagamento result = pagamentoGateway.buscarOrderPagamentoPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void deveRetornarNullQuandoPagamentoNaoEncontrado() throws Exception {
        
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.empty());

        
        Pagamento result = pagamentoGateway.buscarOrderPagamentoPorId(1L);

        assertNull(result);
    }

    @Test
    void deveRetornarNull_quandoFecharPagamentoInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Pagamento resultado = pagamentoGateway.fecharOrdemPagamento(99L, StatusPagamento.FECHADO_COM_SUCESSO);

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

        assertThrows(IllegalArgumentException.class, () -> pagamentoGateway.fecharOrdemPagamento(1L, StatusPagamento.FECHADO_COM_SUCESSO));
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

        Pagamento resultado = pagamentoGateway.buscarOrderPagamentoPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveRetornarNull_quandoBuscarPagamentoPorIdInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Pagamento resultado = pagamentoGateway.buscarOrderPagamentoPorId(99L);

        assertNull(resultado);
    }
}