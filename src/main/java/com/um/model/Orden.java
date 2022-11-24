package com.um.model;

public class Orden {
	
	private int ordenId;
	private int mesa;
	private String precio;
	private int carritoId;
	private String productos;
	private String status;
	
	public Orden() {
		ordenId = 0;
		mesa 	= 0;
		precio	= "0";
		carritoId = 0;
		productos = "0";
		status	= "En proceso";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductos() {
		return productos;
	}

	public void setProductos(String productos) {
		this.productos = productos;
	}

	public int getOrdenId() {
		return ordenId;
	}

	public void setOrdenId(int ordenId) {
		this.ordenId = ordenId;
	}

	public int getMesa() {
		return mesa;
	}

	public void setMesa(int mesa) {
		this.mesa = mesa;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public int getCarritoId() {
		return carritoId;
	}

	public void setCarritoId(int carritoId) {
		this.carritoId = carritoId;
	}
	
}
