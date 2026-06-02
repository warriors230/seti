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

    @XmlElement(name = "pedido")
    private String pedido;

    @XmlElement(name = "Cantidad")
    private String cantidad;

    @XmlElement(name = "EAN")
    private String ean;

    @XmlElement(name = "Producto")
    private String producto;

    @XmlElement(name = "Cedula")
    private String cedula;

    @XmlElement(name = "Direccion")
    private String direccion;
}
