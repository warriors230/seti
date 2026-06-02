package com.acme.pedidos.soap.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvioPedidoRequest", propOrder = {"pedido", "cantidad", "ean", "producto", "cedula", "direccion"})
@Data
public class EnvioPedidoRequest {

    @XmlElement(name = "pedido", namespace = SoapSchemas.NAMESPACE)
    private String pedido;

    @XmlElement(name = "Cantidad", namespace = SoapSchemas.NAMESPACE)
    private String cantidad;

    @XmlElement(name = "EAN", namespace = SoapSchemas.NAMESPACE)
    private String ean;

    @XmlElement(name = "Producto", namespace = SoapSchemas.NAMESPACE)
    private String producto;

    @XmlElement(name = "Cedula", namespace = SoapSchemas.NAMESPACE)
    private String cedula;

    @XmlElement(name = "Direccion", namespace = SoapSchemas.NAMESPACE)
    private String direccion;
}
