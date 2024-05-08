package com.commerceapp.reporting.instancia;

import java.util.HashMap;
import java.util.Map;

public class ReportReciboParameter {

	String PedidoVenta;
	String Vendedor;

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
		return map;
	}

}
