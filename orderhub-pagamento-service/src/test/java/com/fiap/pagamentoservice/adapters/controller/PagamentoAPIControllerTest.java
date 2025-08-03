package com.fiap.pagamentoservice.adapters.controller;

import br.com.orderhub.core.controller.PagamentoController;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import br.com.orderhub.core.dto.pagamentos.CriarPagamentoDTO;
import br.com.orderhub.core.dto.pagamentos.PagamentoDTO;
import br.com.orderhub.core.exceptions.OrdemPagamentoNaoEncontradaException;
import com.fiap.pagamentoservice.adapters.dto.FecharPagamentoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagamentoAPIControllerTest {
    @Mock
    private PagamentoController pagamentoController;

    @InjectMocks
    private PagamentoAPIController pagamentoAPIController;

    @Test
    void deveRetornarPagamentoDTO_quandoBuscarPorIdExistente() {
        Long id = 1L;
        PagamentoDTO pagamentoDTO = new PagamentoDTO(
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                StatusPagamento.EM_ABERTO
        );
        when(pagamentoController.buscarPagamentoPorId(id)).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = pagamentoAPIController.buscarOrdemPagamentoPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagamentoDTO, response.getBody());
    }

    @Test
    void deveRetornarNotFound_quandoBuscarPorIdInexistente() {
        Long id = 99L;
        when(pagamentoController.buscarPagamentoPorId(id)).thenThrow(new OrdemPagamentoNaoEncontradaException("Não encontrado"));

        ResponseEntity<PagamentoDTO> response = pagamentoAPIController.buscarOrdemPagamentoPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveGerarPagamentoComSucesso() throws Exception {
        CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(
                "Adamastor",
                "email@email.com",
                3999.0,
                StatusPagamento.EM_ABERTO
        );
        PagamentoDTO pagamentoDTO = new PagamentoDTO(
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                StatusPagamento.EM_ABERTO
        );
        when(pagamentoController.gerarOrdemPagamento(criarPagamentoDTO)).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = pagamentoAPIController.gerarPagamento(criarPagamentoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagamentoDTO, response.getBody());
    }

    @Test
    void deveFecharPagamentoComSucesso() {
        FecharPagamentoDTO fecharDTO = new FecharPagamentoDTO(1L, "FECHADO_COM_SUCESSO");
        PagamentoDTO pagamentoDTO = new PagamentoDTO(
                1L,
                "Adamastor",
                "email@email.com",
                3999.0,
                StatusPagamento.EM_ABERTO
        );
        when(pagamentoController.fecharOrdemPagamento(1L, StatusPagamento.FECHADO_COM_SUCESSO)).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = pagamentoAPIController.fecharPagamento(fecharDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagamentoDTO, response.getBody());
    }

    @Test
    void deveRetornarNotFound_quandoFecharPagamentoInexistente() {
        FecharPagamentoDTO fecharDTO = new FecharPagamentoDTO(99L, "FECHADO_COM_SUCESSO");
        when(pagamentoController.fecharOrdemPagamento(99L, StatusPagamento.FECHADO_COM_SUCESSO))
                .thenThrow(new OrdemPagamentoNaoEncontradaException("Não encontrado"));

        ResponseEntity<PagamentoDTO> response = pagamentoAPIController.fecharPagamento(fecharDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveLancarExcecao_quandoStatusPagamentoInvalido() {
        FecharPagamentoDTO fecharDTO = new FecharPagamentoDTO(1L, "INVALIDO");

        assertThrows(IllegalArgumentException.class, () -> pagamentoAPIController.fecharPagamento(fecharDTO));
    }
}
