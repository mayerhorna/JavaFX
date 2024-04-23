package com.commerceapp.reporting.instancia;

public class ReportDiagnosticoData {
	private String fichero;
	private String huella;
	private String verificacionhuella;
	private String visualizacion;

	public String getVerificacionhuella() {
		return verificacionhuella;
	}

	public void setVerificacionhuella(String verificacionhuella) {
		this.verificacionhuella = verificacionhuella;
	}

	public String getFichero() {
		return fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	public String getHuella() {
		return huella;
	}

	public void setHuella(String huella) {
		this.huella = huella;
	}

	public String getVisualizacion() {
		return visualizacion;
	}

	public void setVisualizacion(String visualizacion) {
		this.visualizacion = visualizacion;
	}

}
