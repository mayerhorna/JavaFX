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
			report.setTemplatePath("/jrxml/reciboVenta.jrxml");
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

	private static List<ReportReciboData> createReportReciboData() {
		return null;

	}

	private static ReportReciboParameter createReportReciboParameters() {
		ReportReciboParameter reportParameters = new ReportReciboParameter();
		reportParameters.setPedidoVenta(MGeneral.mlform.objDtosRecibo.getNroventa());
		reportParameters.setVendedor(MGeneral.mlform.objDtosRecibo.getVendedor());
		return reportParameters;
	}

	private static List<ReportInstanciaData> createReportData() {
		List<ReportInstanciaData> reportDataCollection = new ArrayList<>();
		ArrayList<cFicheroLibro> listaFicheros = new ArrayList<>();
		ArrayList<String> tipos = new ArrayList<>();
		try {
			String[] indices = new String[MGeneral.mlform.Libros.size()];
			String[] fechas = new String[MGeneral.mlform.Libros.size()];
			int i = 0;
			int j = 0;
			for (cLibro libro : MGeneral.mlform.Libros) {
				if (!Formato.ValorNulo(MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado())) {
					fechas[i] = MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado();
				} else {
					fechas[i] = "";
				}
				int k = 0;
				for (cFicheroLibro fichero : libro.Ficheros) {
					listaFicheros.add(fichero);
					int indice = kLegalizacion.dameIndiceDeTipoLibro(MGeneral.mlform.Libros.get(i).getTipoLibro());
					tipos.add(kLegalizacion.Tiposlibros.get(indice).getDescripcion());
					if (fechas[i] != null && k == 0) {
						indices[i] = String.valueOf(j);
					}
					k++;
					j++;
				}
				i++;
			}
		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
		for (cFicheroLibro oFicheroLibro : listaFicheros) {
			ReportInstanciaData reportData = new ReportInstanciaData();
			reportData.setSubparlblLibroDe(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblLibroDe", ""));
			reportData.setSubparFechaApertura(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFechaApertura", ""));
			reportData.setSubparFechaCierre(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblFechaCierre", ""));
			reportData.setParlblTextoHuella(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTextoHuella", ""));
			reportData.setSubparlblNumero(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
					Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblNumero", ""));
			reportData.setTipoLibro(oFicheroLibro.getTipoLibroSegunNombre().toString());
			reportData.setNumero(String.valueOf(oFicheroLibro.getNumero()));
			reportData.setFechaDeApertura(LegalizacionService.dameFecha(oFicheroLibro.getFechaApertura())
					.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			reportData.setFechaDeCierre(LegalizacionService.dameFecha(oFicheroLibro.getFechaCierre())
					.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			reportData.setHuellaDigital(oFicheroLibro.getHuella());

			reportDataCollection.add(reportData);
		}
		return reportDataCollection;
	}

	
}