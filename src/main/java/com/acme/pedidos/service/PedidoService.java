package com.acme.pedidos.service;

import com.acme.pedidos.model.EnviarPedidoRequest;
import com.acme.pedidos.model.EnviarPedidoResponse;

/**
 * Interfaz para el servicio de pedidos ACME.
 */
public interface PedidoService {
    
    /**
     * Envía un pedido al sistema de envíos ACME, transformando la solicitud
     * de JSON a SOAP XML y la respuesta de SOAP XML a JSON.
     *
     * @param request datos del pedido
     * @return respuesta del servicio de envíos
     */
    EnviarPedidoResponse enviarPedido(EnviarPedidoRequest request);
}
