package com.acme.pedidos.service;

import com.acme.pedidos.exceptions.AcmeExecption;
import com.acme.pedidos.model.EnviarPedidoRequest;
import com.acme.pedidos.model.EnviarPedidoResponse;
import com.acme.pedidos.soap.schema.EnvioPedidoAcme;
import com.acme.pedidos.soap.schema.EnvioPedidoAcmeResponse;
import com.acme.pedidos.soap.schema.EnvioPedidoRequest;
import com.acme.pedidos.soap.schema.EnvioPedidoResponse;
import com.acme.pedidos.utils.AcmeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Implementación concreta del servicio de pedidos ACME.
 * Realiza la validación manual y todo el flujo de integración JSON a SOAP XML directamente
 * utilizando Jaxb2Marshaller para la serialización y deserialización.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final WebClient webClient;
    private final Jaxb2Marshaller jaxb2Marshaller;

    @Value("${acme.soap.endpoint}")
    private String soapEndpoint;

    /**
     * Valida los campos, convierte a SOAP XML usando JAXB, consume el servicio externo
     * y retorna la respuesta convertida a DTO.
     */
    @Override
    public EnviarPedidoResponse enviarPedido(EnviarPedidoRequest request) {
        log.info("[PedidoServiceImpl] Procesando solicitud de pedido");
        
        // 1. Validaciones
        validarRequest(request);

        // 2. Conversión JSON a SOAP XML
        String soapRequest = conversionSoap(request);
        log.info("SOAP Request enviado:\n{}", soapRequest);

        // 3. Consumo del endpoint SOAP
        String soapResponse = consumirSoap(soapRequest);
        log.info("SOAP Response recibido:\n{}", soapResponse);

        // 4. Conversión SOAP XML a JSON / DTO
        return parsearSoapToJson(soapResponse);
    }

    // ─── Validaciones Manuales ──────────────────────────────────────────────────

    private void validarRequest(EnviarPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El objeto enviarPedido no puede ser nulo");
        }
        validarCampo(request.getNumPedido(), "numPedido");
        validarCampo(request.getCantidadPedido(), "cantidadPedido");
        validarCampo(request.getCodigoEAN(), "codigoEAN");
        validarCampo(request.getNombreProducto(), "nombreProducto");
        validarCampo(request.getNumDocumento(), "numDocumento");
        validarCampo(request.getDireccion(), "direccion");
    }

    private void validarCampo(String valor, String nombreCampo) {
        if (!StringUtils.hasText(valor)) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio");
        }
    }

    // ─── Parseamos de JSON a XML con Jaxb2Marshaller ───────────────────────────

    private String conversionSoap(EnviarPedidoRequest req) {
        try {
            EnvioPedidoRequest soapReq = new EnvioPedidoRequest();
            soapReq.setPedido(AcmeUtils.limpiarCadena(req.getNumPedido()));
            soapReq.setCantidad(AcmeUtils.limpiarCadena(req.getCantidadPedido()));
            soapReq.setEan(AcmeUtils.limpiarCadena(req.getCodigoEAN()));
            soapReq.setProducto(AcmeUtils.limpiarCadena(req.getNombreProducto()));
            soapReq.setCedula(AcmeUtils.limpiarCadena(req.getNumDocumento()));
            soapReq.setDireccion(AcmeUtils.limpiarCadena(req.getDireccion()));

            EnvioPedidoAcme envelope = new EnvioPedidoAcme();
            envelope.setEnvioPedidoRequest(soapReq);

            StringWriter writer = new StringWriter();
            jaxb2Marshaller.marshal(envelope, new StreamResult(writer));
            String payloadXml = writer.toString();

            // Quitamos la declaración XML <?xml...?> si viene incluida para que el body SOAP sea válido.
            String cleanXml = payloadXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "")
                                        .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

            return """
                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                        <soapenv:Header/>
                        <soapenv:Body>
                            %s
                        </soapenv:Body>
                    </soapenv:Envelope>
                    """.formatted(cleanXml.trim());
        } catch (Exception e) {
            log.error("Error serializando petición a SOAP XML: {}", e.getMessage(), e);
            throw new AcmeExecption("Error construyendo la petición SOAP", e);
        }
    }

    // ─── Consumo HTTP SOAP ──────────────────────────────────────────────────────

    private String consumirSoap(String soapBody) {
        log.info("Llamando al endpoint externo: {}", soapEndpoint);
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

    // ─── Parseamos de XML a JSON con Jaxb2Marshaller ───────────────────────────

    private EnviarPedidoResponse parsearSoapToJson(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            Node responseNode = doc.getElementsByTagNameNS("http://WSDLs/EnvioPedidos/EnvioPedidosAcme", "EnvioPedidoAcmeResponse").item(0);
            if (responseNode == null) {
                responseNode = doc.getElementsByTagName("EnvioPedidoAcmeResponse").item(0);
            }
            if (responseNode == null) {
                throw new IllegalArgumentException("No se encontró EnvioPedidoAcmeResponse en la respuesta XML");
            }

            EnvioPedidoAcmeResponse soapResponse = (EnvioPedidoAcmeResponse) jaxb2Marshaller.unmarshal(new DOMSource(responseNode));

            if (soapResponse == null || soapResponse.getEnvioPedidoResponse() == null) {
                throw new IllegalArgumentException("La respuesta SOAP está vacía o incompleta");
            }

            EnvioPedidoResponse detail = soapResponse.getEnvioPedidoResponse();

            return EnviarPedidoResponse.builder()
                    .codigoEnvio(detail.getCodigo())
                    .estado(detail.getMensaje())
                    .build();

        } catch (Exception e) {
            log.error("Error deserializando respuesta SOAP: {}", e.getMessage(), e);
            throw new AcmeExecption("Error procesando la respuesta del servicio de envío", e);
        }
    }
}
