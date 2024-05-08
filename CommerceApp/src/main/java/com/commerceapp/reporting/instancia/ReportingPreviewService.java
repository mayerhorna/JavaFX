package com.commerceapp.reporting.instancia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.commerceapp.Main;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumLiterales;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.cPresentacion;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.reporting.Report;
import com.commerceapp.reporting.ReportGeneratePDF;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportingPreviewService {

	public class Reportes {

		public static final String rptPresentacionDatos = "rptPresentacionDatos";
		public static final String rptHojaDiagnostico = "rptHojaDiagnostico";

	}

	public static boolean generarReporteReciboVenta() {
		try {

			List<ReportReciboData> reportDataCollection = createReportReciboData();
			ReportReciboParameter reportParameters = createReportReciboParameters();
			Report report = new Report();
			report.setTemplatePath("/jrxml/facturaPedido.jrxml");
			report.setOutDirectory(MGeneral.Configuracion.getPathAuxiliar());
			report.setOutFileName(kLegalizacion.kNombreFicheroReciboVenta);
			report.configure();
			ReportGeneratePDF reportExporter = new ReportGeneratePDF();

			File pdfFile = reportExporter.generate(report, reportDataCollection, reportParameters);
			Main.getHostService().showDocument(pdfFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private static ReportReciboParameter createReportReciboParameters() {
		ReportReciboParameter reportParameters = new ReportReciboParameter();
		reportParameters.setPedidoVenta(MGeneral.mlform.objDtosRecibo.getNroventa());
		reportParameters.setVendedor(MGeneral.mlform.objDtosRecibo.getVendedor());
		reportParameters.setNombreCliente(MGeneral.mlform.objDtosRecibo.getNombreCliente());
		reportParameters.setDocumentoCliente(MGeneral.mlform.objDtosRecibo.getDocumentoCliente());
		reportParameters.setTotalAPagar(MGeneral.mlform.objDtosRecibo.getTotalapagar());
		reportParameters.setCaja("Caja 001");
		reportParameters.setRutaImagen("src/main/resources/imagenes/LogoECommerce.png");
		return reportParameters;
	}

	private static List<ReportReciboData> createReportReciboData() {
		List<ReportReciboData> reportDataCollection = MGeneral.mlform.objDtosRecibo.objListaProduc;

		return reportDataCollection;
	}

}