package com.fiap.pagamentoservice.adapters.configuration;

import br.com.orderhub.core.controller.PagamentoController;
import br.com.orderhub.core.interfaces.IPagamentoGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class PagamentoControllerConfigTest {
    @Test
    void pagamentoControllerConfigTest() {
        IPagamentoGateway gateway = mock(IPagamentoGateway.class);
        PagamentoControllerConfig config = new PagamentoControllerConfig();
        PagamentoController controller = config.pagamentoController(gateway);
        assertNotNull(controller);
    }
}
