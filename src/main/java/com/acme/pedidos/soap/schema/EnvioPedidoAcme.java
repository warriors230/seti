package com.acme.pedidos.soap.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoAcme", propOrder = {"envioPedidoRequest"})
@XmlRootElement(name = "EnvioPedidoAcme", namespace = SoapSchemas.NAMESPACE)
@Data
public class EnvioPedidoAcme {

    @XmlElement(name = "EnvioPedidoRequest", namespace = SoapSchemas.NAMESPACE)
    private EnvioPedidoRequest envioPedidoRequest;
}
