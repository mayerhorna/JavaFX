package com.commerceapp.domain;

import java.util.ArrayList;

import com.commerceapp.reporting.instancia.ReportReciboData;

public class DatosReciboVenta {
	String nroventa;
	String Vendedor;
	String DocumentoCliente;
	String NombreCliente;
	String totalapagar;

	public String getTotalapagar() {
		return totalapagar;
	}

	public void setTotalapagar(String totalapagar) {
		this.totalapagar = totalapagar;
	}

	public ArrayList<ReportReciboData> objListaProduc = new ArrayList<ReportReciboData>();

	public String getDocumentoCliente() {
		return DocumentoCliente;
	}

	public void setDocumentoCliente(String documentoCliente) {
		DocumentoCliente = documentoCliente;
	}

	public String getNombreCliente() {
		return NombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		NombreCliente = nombreCliente;
	}

	public String getNroventa() {
		return nroventa;
	}

	public String getVendedor() {
		return Vendedor;
	}

	public void setVendedor(String vendedor) {
		Vendedor = vendedor;
	}

	public void setNroventa(String nroventa) {
		this.nroventa = nroventa;
	}

}
