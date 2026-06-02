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
@Schema(description = "Wrapper de la respuesta del pedido")
public class EnviarPedidoResponseWrapper {

    @JsonProperty("enviarPedidoRespuesta")
    @Schema(description = "Datos de la respuesta")
    private EnviarPedidoResponse enviarPedidoRespuesta;
}
