package com.um.model;

public class Carrito {
	
	private int carritoId;
	private int productoId;
	private int cantidad;
	private String precio;	
	
	public Carrito() {
		carritoId = 0;
		productoId = 0;
		cantidad = 0;
		precio = "0";
	}

	public int getCarritoId() {
		return carritoId;
	}

	public void setCarritoId(int carritoId) {
		this.carritoId = carritoId;
	}

	public int getProductoId() {
		return productoId;
	}

	public void setProductoId(int productoId) {
		this.productoId = productoId;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}
	
	
}
