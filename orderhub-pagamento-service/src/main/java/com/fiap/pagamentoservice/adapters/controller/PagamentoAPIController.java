package com.fiap.pagamentoservice.adapters.controller;

import br.com.orderhub.core.controller.PagamentoController;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import br.com.orderhub.core.dto.pagamentos.CriarPagamentoDTO;
import br.com.orderhub.core.dto.pagamentos.PagamentoDTO;
import br.com.orderhub.core.exceptions.OrdemPagamentoNaoEncontradaException;
import com.fiap.pagamentoservice.adapters.dto.FecharPagamentoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
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
    public ResponseEntity<PagamentoDTO> gerarPagamento(@RequestBody CriarPagamentoDTO criarPagamentoDTO) throws Exception {
        PagamentoDTO pagamentoDTO = pagamentoController.gerarOrdemPagamento(criarPagamentoDTO);
        return ResponseEntity.ok(pagamentoDTO);
    }

    @PostMapping("/fechar")
    public ResponseEntity<PagamentoDTO> fecharPagamento(@RequestBody FecharPagamentoDTO fecharPagamentoDTO){
        try{
            StatusPagamento statusPagamento = switch (fecharPagamentoDTO.novoStatusPagamento()) {
                case "EM_ABERTO" -> StatusPagamento.EM_ABERTO;
                case "FECHADO_COM_SUCESSO" -> StatusPagamento.FECHADO_COM_SUCESSO;
                case "FECHADO_FALHA_PAGAMENTO" -> StatusPagamento.FECHADO_FALHA_PAGAMENTO;
                default -> throw new IllegalArgumentException("Status de pagamento invalido");
            };

            return ResponseEntity.ok(pagamentoController.fecharOrdemPagamento(fecharPagamentoDTO.idOrdemPagamento(), statusPagamento));
        } catch (OrdemPagamentoNaoEncontradaException exception){
            System.out.println(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
