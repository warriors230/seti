package com.acme.pedidos;

import com.acme.pedidos.model.EnviarPedidoRequest;
import com.acme.pedidos.model.EnviarPedidoResponse;
import com.acme.pedidos.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private WebClient webClient;

    @Test
    void contextLoads() {
        assertNotNull(pedidoService);
    }

    @Test
    void testBuildRequest() {
        EnviarPedidoRequest request = EnviarPedidoRequest.builder()
                .numPedido("75630275")
                .cantidadPedido("1")
                .codigoEAN("00110000765191002104587")
                .nombreProducto("Armario INVAL")
                .numDocumento("1113987400")
                .direccion("CR 72B 45 12 APT 301")
                .build();

        assertNotNull(request);
        assertEquals("75630275", request.getNumPedido());
        assertEquals("1", request.getCantidadPedido());
        assertEquals("00110000765191002104587", request.getCodigoEAN());
        assertEquals("Armario INVAL", request.getNombreProducto());
        assertEquals("1113987400", request.getNumDocumento());
        assertEquals("CR 72B 45 12 APT 301", request.getDireccion());
    }

    @Test
    void testResponseModel() {
        EnviarPedidoResponse response = EnviarPedidoResponse.builder()
                .codigoEnvio("80375472")
                .estado("Entregado exitosamente al cliente")
                .build();

        assertNotNull(response);
        assertEquals("80375472", response.getCodigoEnvio());
        assertEquals("Entregado exitosamente al cliente", response.getEstado());
    }
}
