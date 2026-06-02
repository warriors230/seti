package com.acme.pedidos.soap.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoResponse", propOrder = {"codigo", "mensaje"})
@Data
public class EnvioPedidoResponse {

    @XmlElement(name = "Codigo", namespace = SoapSchemas.NAMESPACE)
    private String codigo;

    @XmlElement(name = "Mensaje", namespace = SoapSchemas.NAMESPACE)
    private String mensaje;
}
