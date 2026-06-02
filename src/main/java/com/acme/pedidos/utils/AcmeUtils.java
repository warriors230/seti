package com.acme.pedidos.utils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class AcmeUtils {
	
	 public static String extraerValoresXlms(Document doc, String tagName) {
	        NodeList nodes = doc.getElementsByTagName(tagName);
	        if (nodes.getLength() > 0 && nodes.item(0).getFirstChild() != null) {
	            return nodes.item(0).getFirstChild().getNodeValue();
	        }
	        return "";
	    }
	 

	 /** Escapa caracteres especiales XML para evitar injection. */
	 public static String limpiarCadena(String value) {
	        if (value == null) return "";
	        return value.replace("&", "&amp;")
	                    .replace("<", "&lt;")
	                    .replace(">", "&gt;")
	                    .replace("\"", "&quot;")
	                    .replace("'", "&apos;");
	    }
}
