package com.fiap.pagamentoservice.adapters.dto;

import br.com.orderhub.core.domain.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarStatusPedidoDTO {
    private Long idPedido;
    private StatusPagamento statusPagamento;
}
