package com.fiap.pagamentoservice.adapters.gateway;

import br.com.orderhub.core.domain.entities.Pagamento;
import br.com.orderhub.core.domain.enums.StatusPagamento;
import br.com.orderhub.core.interfaces.IPagamentoGateway;
import com.fiap.pagamentoservice.adapters.mapper.PagamentoEntityMapper;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoEntity;
import com.fiap.pagamentoservice.adapters.persistence.PagamentoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PagamentoRepositoryJpaGatewayImpl implements IPagamentoGateway {
    private final PagamentoRepository pagamentoRepository;

    public PagamentoRepositoryJpaGatewayImpl(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento gerarOrdemPagamento(Pagamento pagamentoDTO) throws Exception {
        PagamentoEntity pagamentoEntity = PagamentoEntityMapper.domainToEntity(pagamentoDTO);

        return PagamentoEntityMapper.entityToDomain(pagamentoRepository.save(pagamentoEntity));
    }

    @Override
    public Pagamento fecharOrdemPagamento(Long idOrdemPagamento, StatusPagamento statusPagamento) {
        Optional<PagamentoEntity> pagamentoOptional = pagamentoRepository.findById(idOrdemPagamento);

        if (pagamentoOptional.isEmpty()) {
            return null;
        }
        PagamentoEntity pagamentoEntity = pagamentoOptional.get();

        if (pagamentoEntity.getStatusPagamento().equals(StatusPagamento.EM_ABERTO) && !statusPagamento.toString().contains("FECHADO")) {
            throw new IllegalArgumentException("Ordens de pagamento abertas só podem ser fechadas");
        }

        if (pagamentoEntity.getStatusPagamento().contains("FECHADO")) {
            throw new IllegalArgumentException("Orderm de pagamento já fechada!");
        }

        pagamentoEntity.setStatusPagamento(statusPagamento.toString());

        return PagamentoEntityMapper.entityToDomain(pagamentoRepository.save(pagamentoEntity));
    }

    @Override
    public Pagamento buscarOrderPagamentoPorId(Long id) {
        Optional<PagamentoEntity> pagamentoOptional = pagamentoRepository.findById(id);

        if (pagamentoOptional.isEmpty()) {
            return null;
        }

        PagamentoEntity pagamentoEntity = pagamentoOptional.get();

        return PagamentoEntityMapper.entityToDomain(this.pagamentoRepository.save(pagamentoEntity));
    }
}
