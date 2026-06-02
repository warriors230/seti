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
@Schema(description = "Respuesta del envío del pedido")
public class EnviarPedidoResponse {

    @JsonProperty("codigoEnvio")
    @Schema(description = "Código de envío generado", example = "80375472")
    private String codigoEnvio;

    @JsonProperty("estado")
    @Schema(description = "Estado del envío", example = "Entregado exitosamente al cliente")
    private String estado;
}
