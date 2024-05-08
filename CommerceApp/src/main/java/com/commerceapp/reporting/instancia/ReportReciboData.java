package com.commerceapp.reporting.instancia;

public class ReportReciboData {
	private String numeracion;
	private String producto;
	private String precio;
	private String cantidad;

	public ReportReciboData(String producto, String precio, String numeracion, String cantidad) {

		this.producto = producto;
		this.precio = precio;
		this.cantidad = cantidad;
		this.numeracion = numeracion;

	}

	public String getNumeracion() {
		return numeracion;
	}

	public void setNumeracion(String numeracion) {
		this.numeracion = numeracion;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

}
