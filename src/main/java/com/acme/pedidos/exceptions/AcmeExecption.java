package com.acme.pedidos.exceptions;

public class AcmeExecption extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AcmeExecption(String mensaje, Throwable cause) {
		super(mensaje,cause);
	}
	
	

}
