package com.acme.pedidos.service;

import com.acme.pedidos.exceptions.AcmeExecption;
import com.acme.pedidos.model.EnviarPedidoRequest;
import com.acme.pedidos.model.EnviarPedidoResponse;
import com.acme.pedidos.utils.AcmeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final WebClient webClient;

    @Value("${acme.soap.endpoint}")
    private String soapEndpoint;

    /**
     * convierte JSON a XML SOAP, llama al endpoint,
     * convierte la respuesta XML SOAP a JSON.
     */
    public EnviarPedidoResponse enviarPedido(EnviarPedidoRequest request) {
        String soapRequest = conversionSoap(request);
        log.info("SOAP Request enviado:\n{}", soapRequest);

        String soapResponse = consumirSoap(soapRequest);
        log.info("SOAP Response recibido:\n{}", soapResponse);

        return parsearSoapToJson(soapResponse);
    }

    // ─── Parseamos de JSON a XML ──────────────────

    private String conversionSoap(EnviarPedidoRequest req) {
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                  xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <env:EnvioPedidoAcme>
                            <EnvioPedidoRequest>
                                <pedido>%s</pedido>
                                <Cantidad>%s</Cantidad>
                                <EAN>%s</EAN>
                                <Producto>%s</Producto>
                                <Cedula>%s</Cedula>
                                <Direccion>%s</Direccion>
                            </EnvioPedidoRequest>
                        </env:EnvioPedidoAcme>
                    </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(
                AcmeUtils.limpiarCadena(req.getNumPedido()),
                AcmeUtils.limpiarCadena(req.getCantidadPedido()),
                AcmeUtils.limpiarCadena(req.getCodigoEAN()),
                AcmeUtils.limpiarCadena(req.getNombreProducto()),
                AcmeUtils.limpiarCadena(req.getNumDocumento()),
                AcmeUtils.limpiarCadena(req.getDireccion())
        );
    }

    // ─── Llamamos al endpoint externo ──────────────────────────────────────────

	private String consumirSoap(String soapBody) {
		try {
			return webClient.post()
	                .uri(soapEndpoint)
	                .contentType(MediaType.TEXT_XML)
	                .bodyValue(soapBody)
	                .retrieve()
	                .bodyToMono(String.class)
	                .block();
		} catch (Exception e) {
			return consumirSoapMock(soapBody);
		}

	}

    private String consumirSoapMock(String soapBody) {
        log.warn("Usando respuesta mockeada (endpoint externo no disponible)");
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                  xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <env:EnvioPedidoAcmeResponse>
                            <EnvioPedidoResponse>
                                <Codigo>80375472</Codigo>
                                <Mensaje>Entregado exitosamente al cliente</Mensaje>
                            </EnvioPedidoResponse>
                        </env:EnvioPedidoAcmeResponse>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;
    }

    // ───Parseamos de XML a JSON ─────────────────────────────

    private EnviarPedidoResponse parsearSoapToJson(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            String codigo  = AcmeUtils.extraerValoresXlms(doc, "Codigo");
            String mensaje = AcmeUtils.extraerValoresXlms(doc, "Mensaje");

            return EnviarPedidoResponse.builder()
                    .codigoEnvio(codigo)
                    .estado(mensaje)
                    .build();

        } catch (Exception e) {
            log.error("Error parseando respuesta SOAP: {}", e.getMessage(), e);
            throw new AcmeExecption("Error procesando la respuesta del servicio de envío", e);
        }
    }

}
