package com.fiap.pagamento.service.controller;

import br.com.orderhub.core.controller.PagamentoController;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import br.com.orderhub.core.dto.clientes.ClienteDTO;
import br.com.orderhub.core.dto.pagamentos.PagamentoDTO;
import br.com.orderhub.core.exceptions.OrdemPagamentoNaoEncontradaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/pagamentos")
public class PagamentoAPIController {
    private final PagamentoController pagamentoController;

    public PagamentoAPIController(PagamentoController pagamentoController) {
        this.pagamentoController = pagamentoController;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarOrdemPagamentoPorId(@PathVariable Long id){
        try{
            return ResponseEntity.ok(
                    pagamentoController.buscarPagamentoPorId(id)
            );
        } catch (OrdemPagamentoNaoEncontradaException exception){
            System.out.println(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/gerar")
    public ResponseEntity<PagamentoDTO> gerarPagamento(@RequestBody ClienteDTO clienteDTO){
        return ResponseEntity.ok(pagamentoController.gerarOrdemPagamento(clienteDTO));
    }

    @PostMapping("/fechar")
    public ResponseEntity<PagamentoDTO> fecharPagamento(@RequestBody Long id, @RequestBody StatusPagamento novoStatus){
        try{
            return ResponseEntity.ok(pagamentoController.fecharOrdemPagamento(id, novoStatus));
        } catch (OrdemPagamentoNaoEncontradaException exception){
            System.out.println(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
