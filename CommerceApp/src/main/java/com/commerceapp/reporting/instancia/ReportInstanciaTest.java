package com.commerceapp.reporting.instancia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.commerceapp.Main;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.cPresentacion;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.reporting.Report;
import com.commerceapp.reporting.ReportGeneratePDF;
import com.commerceapp.util.Utilidades;

import javafx.application.HostServices;

public class ReportInstanciaTest {

	public class Reportes {

		public static final String rptPresentacionDatos = "rptPresentacionDatos";
		public static final String rptHojaDiagnostico = "rptHojaDiagnostico";

	}

	public static void main(String[] args) throws IOException {
		ReportInstanciaData reportData = createReportData();
		ReportInstanciaParameters reportParameters = createReportParameters();
		Report report = new Report();
		report.setTemplatePath("/jrxml/instancia.jrxml");
		report.setOutDirectory("C:/Users/monda/Documents");
		report.setOutFileName("Instancia.pdf");
		report.configure();
		ReportGeneratePDF reportExporter = new ReportGeneratePDF();
		List<ReportInstanciaData> reportDataCollection = new ArrayList<>();
		reportDataCollection.add(reportData);
		File pdfFile = reportExporter.generate(report, reportDataCollection, reportParameters);

		HostServices hostServices = Main.getHostService();
		hostServices.showDocument(pdfFile.toURI().toString());

	}

	public static void metodoPrueba() {
		ReportInstanciaData reportData = createReportData();
		ReportInstanciaParameters reportParameters = createReportParameters();
		Report report = new Report();
		report.setTemplatePath("/jrxml/instancia.jrxml");
		report.setOutDirectory("C:/Users/monda/Documents");
		report.setOutFileName("Instancia.pdf");
		report.configure();
		ReportGeneratePDF reportExporter = new ReportGeneratePDF();
		List<ReportInstanciaData> reportDataCollection = new ArrayList<>();
		reportDataCollection.add(reportData);
		File pdfFile = reportExporter.generate(report, reportDataCollection, reportParameters);

		HostServices hostServices = Main.getHostService(); // Este metodo getHostServices() lo tiene Application, habría
		// que llegar al Applicaion
		hostServices.showDocument(pdfFile.toURI().toString());
	}

	private static ReportInstanciaData createReportData() {
		ReportInstanciaData reportData = new ReportInstanciaData();

		
		
		
		return reportData;
	}

	private static ReportInstanciaParameters createReportParameters() {
		ReportInstanciaParameters reportParameters = new ReportInstanciaParameters();

		reportParameters.setPartxtTitulo(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "txtTitulo", ""));

		reportParameters.setTxtSubtitulo(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "txtSubtitulo", ""));
		reportParameters.setLblFechaSolicitud(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFechaSolicitud", ""));

		if (MGeneral.mlform.Datos.get_TipoPersona().equalsIgnoreCase(kLegalizacion.kTipoPersonaFisica.toString())) {
			reportParameters.setParlblNombreORazon(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblNombreYApellidos", ""));
		} else {
			reportParameters.setParlblNombreORazon(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblDenominacion", ""));
		}

		reportParameters.setParlblNIFCIF(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblNIFCIF", ""));

		reportParameters.setLblDomicilio(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblDomicilio", ""));
		reportParameters.setParlblFirma(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFirma", ""));
		reportParameters.setParlblCiudad(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblCiudad", ""));
		reportParameters.setParlblCodigoPostal(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblCodigoPostal", ""));

		reportParameters.setParlblTelefono(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTelefono", ""));
		reportParameters.setParlblFax(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFax", ""));
		reportParameters.setParlblProvincia(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblProvincia", ""));
		reportParameters.setParlblTexto(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTexto", ""));
		reportParameters.setPartxtSubtitulo2(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "txtSubtitulo2", ""));

		reportParameters.setParlblTipoRegistroPublico(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTipoRegistroPublico", ""));
		reportParameters.setParlblTomo(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTomo", ""));
		reportParameters.setParlblFolio(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFolio", ""));
		reportParameters.setParlblHojaRegistral(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblHojaRegistral", ""));
		reportParameters.setParlblDatosRegistrales(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblDatosRegistrales", ""));
		reportParameters.setParlblOtros(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblOtros", ""));
		
		
		String cNombre;
		cNombre = MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario();
		if (!MGeneral.mlform.Presentacion.getApellido1().isEmpty()) {
			cNombre = cNombre + " " + MGeneral.mlform.Presentacion.getApellido1();
		}
		if (!MGeneral.mlform.Presentacion.getApellido2().isEmpty()) {
			cNombre = cNombre + " " + MGeneral.mlform.Presentacion.getApellido2();
		}

		MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Provincias");
		String provincia = maestro.obtenerDescripcion(MGeneral.mlform.Presentacion.getProvinciaCodigo());
		reportParameters.setPartxtNombreORazon(cNombre);
		reportParameters.setPartxtNIFCIF(MGeneral.mlform.Presentacion.getNifCif());

		LocalDate localDate = MGeneral.mlform.dameFecha(MGeneral.mlform.Presentacion.getFechaSolicitud());// For
																											// reference
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedString = localDate.format(formatter);
		reportParameters.setPartxtFechaSolicitud(formattedString);

		reportParameters.setPartxtDomicilio(MGeneral.mlform.Presentacion.getDomicilio());
		reportParameters.setPartxtCiudad(MGeneral.mlform.Presentacion.getCiudad());
		reportParameters.setPartxtCodigoPostal(MGeneral.mlform.Presentacion.getCodigoPostal());
		reportParameters.setPartxtProvincia(provincia);
		reportParameters.setPartxtTelefono(MGeneral.mlform.Presentacion.getTelefono());
		reportParameters.setPartxtFax(MGeneral.mlform.Presentacion.getFax());
		
		reportParameters.setPartxtFolio(MGeneral.mlform.Presentacion.getDatosRegistralesFolio());
		reportParameters.setPartxtTomo(MGeneral.mlform.Presentacion.getDatosRegistralesTomo());
		reportParameters.setPartxtTipoRegistroPublico(MGeneral.mlform.Presentacion.getTipoRegistroPublico());
		reportParameters.setPartxtHojaRegistral(MGeneral.mlform.Presentacion.getDatosRegistralesHoja());
		reportParameters.setPartxtOtros(MGeneral.mlform.Presentacion.getDatosRegistralesOtros());
		return reportParameters;
	}
}
