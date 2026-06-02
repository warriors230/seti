package com.acme.pedidos.soap.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoAcmeResponse", propOrder = {"envioPedidoResponse"})
@XmlRootElement(name = "EnvioPedidoAcmeResponse", namespace = SoapSchemas.NAMESPACE)
@Data
public class EnvioPedidoAcmeResponse {

    @XmlElement(name = "EnvioPedidoResponse")
    private EnvioPedidoResponse envioPedidoResponse;
}
