package com.acme.pedidos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Wrapper del request de pedido")
public class EnviarPedidoRequestWrapper {

    @JsonProperty("enviarPedido")
    @Schema(description = "Datos del pedido")
    private EnviarPedidoRequest enviarPedido;
}
