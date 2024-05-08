package com.commerceapp.reporting.instancia;

import java.util.HashMap;
import java.util.Map;

public class ReportReciboParameter {

	String PedidoVenta;
	String Vendedor;

	String NombreCliente;
	String DocumentoCliente;

	String TotalAPagar;
	String rutaImagen;
	String Caja;

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getCaja() {
		return Caja;
	}

	public void setCaja(String caja) {
		Caja = caja;
	}

	public String getTotalAPagar() {
		return TotalAPagar;
	}

	public void setTotalAPagar(String totalAPagar) {
		TotalAPagar = totalAPagar;
	}

	public String getNombreCliente() {
		return NombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		NombreCliente = nombreCliente;
	}

	public String getDocumentoCliente() {
		return DocumentoCliente;
	}

	public void setDocumentoCliente(String documentoCliente) {
		DocumentoCliente = documentoCliente;
	}

	public String getVendedor() {
		return Vendedor;
	}

	public void setVendedor(String vendedor) {
		Vendedor = vendedor;
	}

	public String getPedidoVenta() {
		return PedidoVenta;
	}

	public void setPedidoVenta(String pedidoVenta) {
		PedidoVenta = pedidoVenta;
	}

	public Map<String, Object> toMap() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();

		map.put("PedidoVenta", PedidoVenta);
		map.put("Vendedor", Vendedor);
		map.put("NombreCliente", NombreCliente);
		map.put("DocumentoCliente", DocumentoCliente);
		map.put("TotalAPagar", TotalAPagar);
		map.put("rutaImagen", rutaImagen);
		map.put("Caja", Caja);
		return map;
	}

}
