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
@Schema(description = "Datos del pedido a enviar")
public class EnviarPedidoRequest {

    @JsonProperty("numPedido")
    @Schema(description = "Número del pedido", example = "75630275")
    private String numPedido;

    @JsonProperty("cantidadPedido")
    @Schema(description = "Cantidad del pedido", example = "1")
    private String cantidadPedido;

    @JsonProperty("codigoEAN")
    @Schema(description = "Código EAN del producto", example = "00110000765191002104587")
    private String codigoEAN;

    @JsonProperty("nombreProducto")
    @Schema(description = "Nombre del producto", example = "Armario INVAL")
    private String nombreProducto;

    @JsonProperty("numDocumento")
    @Schema(description = "Número de documento del cliente", example = "1113987400")
    private String numDocumento;

    @JsonProperty("direccion")
    @Schema(description = "Dirección de entrega", example = "CR 72B 45 12 APT 301")
    private String direccion;
}
