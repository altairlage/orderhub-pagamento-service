package com.fiap.pagamentoservice.adapters.configuration;

import br.com.orderhub.core.controller.PagamentoController;
import br.com.orderhub.core.interfaces.IPagamentoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoControllerConfig {

    @Bean
    public PagamentoController pagamentoController(IPagamentoGateway pagamentoGateway){
        return new PagamentoController(pagamentoGateway);
    }
}
