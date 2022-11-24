package com.um.model;

public class OrdenProducto {
	
	private int idOrden;
	private int idProducto;
	private int cantidad;
	private String nota;
	
	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public int getIdOrden() {
		return idOrden;
	}
	
	public void setIdOrden(int idOrden) {
		this.idOrden = idOrden;
	}
	
	
}
