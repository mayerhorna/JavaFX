package com.commerceapp.reporting.instancia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportDiagnosticoParameters {

	private String parlblTitulo;
	private String parlblIdentificadorEntrada;
	private String DataSet1Implicitos;
	private String parlblErroresPrimarios;
	private String parlblErroresImplicitos;
	private String DataSet2Implicitos;

	private String parcolNombreFichero;
	private String parcolHuella;
	private String parcolVerificacionHuella;
	private String parcolVisualizacion;
	private String parlblInformacion;
	private String partxtErroresPrimarios;
	private String partxtErroresImplicitos;


	public String getPartxtErroresPrimarios() {
		return partxtErroresPrimarios;
	}

	public void setPartxtErroresPrimarios(String partxtErroresPrimarios) {
		this.partxtErroresPrimarios = partxtErroresPrimarios;
	}

	public String getPartxtErroresImplicitos() {
		return partxtErroresImplicitos;
	}

	public void setPartxtErroresImplicitos(String partxtErroresImplicitos) {
		this.partxtErroresImplicitos = partxtErroresImplicitos;
	}

	public String getParlblInformacion() {
		return parlblInformacion;
	}

	public void setParlblInformacion(String parlblInformacion) {
		this.parlblInformacion = parlblInformacion;
	}

	public String getParcolNombreFichero() {
		return parcolNombreFichero;
	}

	public void setParcolNombreFichero(String parcolNombreFichero) {
		this.parcolNombreFichero = parcolNombreFichero;
	}

	public String getParcolHuella() {
		return parcolHuella;
	}

	public void setParcolHuella(String parcolHuella) {
		this.parcolHuella = parcolHuella;
	}

	public String getParcolVerificacionHuella() {
		return parcolVerificacionHuella;
	}

	public void setParcolVerificacionHuella(String parcolVerificacionHuella) {
		this.parcolVerificacionHuella = parcolVerificacionHuella;
	}

	public String getParcolVisualizacion() {
		return parcolVisualizacion;
	}

	public void setParcolVisualizacion(String parcolVisualizacion) {
		this.parcolVisualizacion = parcolVisualizacion;
	}

	public String getParlblErroresPrimarios() {
		return parlblErroresPrimarios;
	}

	public void setParlblErroresPrimarios(String parlblErroresPrimarios) {
		this.parlblErroresPrimarios = parlblErroresPrimarios;
	}

	public String getParlblErroresImplicitos() {
		return parlblErroresImplicitos;
	}

	public void setParlblErroresImplicitos(String parlblErroresImplicitos) {
		this.parlblErroresImplicitos = parlblErroresImplicitos;
	}

	public String getDataSet2Implicitos() {
		return DataSet2Implicitos;
	}

	public void setDataSet2Implicitos(String dataSet2Implicitos) {
		DataSet2Implicitos = dataSet2Implicitos;
	}

	public String getParlblIdentificadorEntrada() {
		return parlblIdentificadorEntrada;
	}

	public void setParlblIdentificadorEntrada(String parlblIdentificadorEntrada) {
		this.parlblIdentificadorEntrada = parlblIdentificadorEntrada;
	}

	public String getDataSet1Implicitos() {
		return DataSet1Implicitos;
	}

	public void setDataSet1Implicitos(String dataSet1Implicitos) {
		DataSet1Implicitos = dataSet1Implicitos;
	}

	public String getParlblTitulo() {
		return parlblTitulo;
	}

	public void setParlblTitulo(String parlblTitulo) {
		this.parlblTitulo = parlblTitulo;
	}

	public String getLblIdentificadorEntrada() {
		return parlblIdentificadorEntrada;
	}

	public void setLblIdentificadorEntrada(String lblIdentificadorEntrada) {
		this.parlblIdentificadorEntrada = lblIdentificadorEntrada;
	}

	public Map<String, Object> toMap() {

		Map<String, Object> map = new HashMap<>();

		map.put("parlblTitulo", parlblTitulo);
		map.put("parlblIdentificadorEntrada", parlblIdentificadorEntrada);
		map.put("DataSet1Implicitos", DataSet1Implicitos);
		map.put("parlblErroresPrimarios", parlblErroresPrimarios);
		map.put("parlblErroresImplicitos", parlblErroresImplicitos);
		map.put("DataSet2Implicitos", DataSet2Implicitos);
		map.put("parcolNombreFichero", parcolNombreFichero);
		map.put("parcolHuella", parcolHuella);
		map.put("parcolVerificacionHuella", parcolVerificacionHuella);
		map.put("parcolVisualizacion", parcolVisualizacion);
		map.put("parlblInformacion", parlblInformacion);
		map.put("partxtErroresPrimarios", partxtErroresPrimarios);
		map.put("partxtErroresImplicitos", partxtErroresImplicitos);
	
		return map;
	}

}
