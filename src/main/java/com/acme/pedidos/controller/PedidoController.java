package com.acme.pedidos.controller;

import com.acme.pedidos.model.EnviarPedidoRequestWrapper;
import com.acme.pedidos.model.EnviarPedidoResponse;
import com.acme.pedidos.model.EnviarPedidoResponseWrapper;
import com.acme.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API de abastecimiento ACME - Tienda Carrera 70")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping(value = "/enviar",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Enviar pedido",
        description = "Recibe un pedido en formato JSON, lo transforma a SOAP/XML, lo envía al sistema de envíos ACME y retorna la respuesta en JSON.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "enviarPedido": {
                        "numPedido": "75630275",
                        "cantidadPedido": "1",
                        "codigoEAN": "00110000765191002104587",
                        "nombreProducto": "Armario INVAL",
                        "numDocumento": "1113987400",
                        "direccion": "CR 72B 45 12 APT 301"
                      }
                    }
                    """)
            )
        ),
			responses = {
					@ApiResponse(responseCode = "200", description = "Pedido enviado exitosamente", 
					content = @Content(schema = @Schema(implementation = EnviarPedidoResponseWrapper.class))),
					@ApiResponse(responseCode = "400", description = "Datos de pedido inválidos", 
					content = @Content(schema = @Schema(implementation = Map.class))),
					@ApiResponse(responseCode = "500", description = "Error interno o fallo en servicio externo", 
					content = @Content(schema = @Schema(implementation = Map.class))) })
    public ResponseEntity<EnviarPedidoResponseWrapper> enviarPedido(
            @RequestBody EnviarPedidoRequestWrapper requestWrapper) {

        log.info("Pedido recibido: {}", requestWrapper);

        if (requestWrapper == null || requestWrapper.getEnviarPedido() == null) {
            throw new IllegalArgumentException("El cuerpo JSON debe contener el nodo enviarPedido");
        }

        EnviarPedidoResponse response = pedidoService.enviarPedido(requestWrapper.getEnviarPedido());

        EnviarPedidoResponseWrapper responseWrapper = EnviarPedidoResponseWrapper.builder()
                .enviarPedidoRespuesta(response)
                .build();

        log.info("Respuesta enviada: {}", responseWrapper);
        return ResponseEntity.ok(responseWrapper);
    }
}
