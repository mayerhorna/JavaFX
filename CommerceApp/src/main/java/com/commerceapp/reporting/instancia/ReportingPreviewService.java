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

	List<String> lstErroresPrimarios = new ArrayList<>();

	// Lista errores secundarios
	List<String> lstErroresSecundarios = new ArrayList<>();

	public class Reportes {

		public static final String rptPresentacionDatos = "rptPresentacionDatos";
		public static final String rptHojaDiagnostico = "rptHojaDiagnostico";

	}

	public static boolean generarReportePresentacionDatos() {
		try {
			List<ReportInstanciaData> reportDataCollection = createReportData();
			ReportInstanciaParameters reportParameters = createReportParameters();
			Report report = new Report();
			report.setTemplatePath("/jrxml/instancia.jrxml");
			report.setOutDirectory(MGeneral.mlform.Datos.get_PathDatos());
			report.setOutFileName(kLegalizacion.kNombreFicheroInstancia);
			report.configure();
			ReportGeneratePDF reportExporter = new ReportGeneratePDF();

			File pdfFile = reportExporter.generate(report, reportDataCollection, reportParameters);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static boolean generarReporteHojaDiagnostico() {
		try {
			List<ReportDiagnosticoData> reportDataCollection = createDiagnosticoReportData();
			ReportDiagnosticoParameters reportParameters = createReportDiagnosticoParameters();
			Report report = new Report();
			report.setTemplatePath("/jrxml/rptHojaDiagnostico.jrxml");
			report.setOutDirectory(MGeneral.mlform.Datos.get_PathDatos());
			report.setOutFileName("Diagnostico.pdf");
			report.configure();
			ReportGeneratePDF reportExporter = new ReportGeneratePDF();

			File pdfFile = reportExporter.generateDiagnostico(report, reportDataCollection, reportParameters);
			Utilidades.ProccessStarURL(pdfFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private static List<ReportDiagnosticoData> createDiagnosticoReportData() {
		List<ReportDiagnosticoData> reportDataCollection = new ArrayList<>();
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
			ReportDiagnosticoData reportData = new ReportDiagnosticoData();

			reportData.setFichero(oFicheroLibro.getNombreFichero());
			reportData.setHuella(oFicheroLibro.getHuellaManual());
			if (oFicheroLibro.getHuellaManual().equalsIgnoreCase(oFicheroLibro.getHuellaCalculada())) {
				reportData.setVerificacionhuella(MGeneral.Idioma.obtenerLiteral(EnumLiterales.Correcta).toString());
			} else {
				reportData.setVerificacionhuella(MGeneral.Idioma.obtenerLiteral(EnumLiterales.Erronea).toString());
			}

			reportData.setVisualizacion(String.valueOf(oFicheroLibro.isVisualizacionCorrecta()
					? MGeneral.Idioma.obtenerLiteral(EnumLiterales.Correcta).toString()
					: MGeneral.Idioma.obtenerLiteral(EnumLiterales.Erronea).toString()));

			reportDataCollection.add(reportData);
		}
		return reportDataCollection;
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

	private static ReportDiagnosticoParameters createReportDiagnosticoParameters() {
		ReportDiagnosticoParameters reportParameters = new ReportDiagnosticoParameters();

		ValidationResult validationResult = validar();

		List<String> erroresPrimarios = validationResult.getLstErroresPrimarios();

		List<String> erroresSecundarios = validationResult.getLstErroresSecundarios();

		String erroresPrimariosString = String.join("\r\n", erroresPrimarios);
		String erroresSecundariosString = String.join("\r\n", erroresSecundarios);

		reportParameters.setParlblTitulo(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "lblTitulo", ""));
		reportParameters.setLblIdentificadorEntrada(String.format(
				MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES, Reportes.rptHojaDiagnostico,
						ElementosIdiomaC.TEXT_CONTROLES, "lblIdentificadorEntrada", ""),
				MGeneral.mlform.Datos.get_IdentificadorEntrada()));
		reportParameters.setDataSet1Implicitos(erroresPrimariosString);
		reportParameters.setParlblErroresPrimarios(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "lblErroresPrimarios", ""));
		reportParameters.setParlblErroresImplicitos(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "lblErroresImplicitos", ""));
		reportParameters.setDataSet2Implicitos(erroresSecundariosString);

		reportParameters.setParcolNombreFichero(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "colNombreFichero", ""));
		reportParameters.setParcolHuella(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "colHuella", ""));
		reportParameters.setParcolVerificacionHuella(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "colVerificacionHuella", ""));
		reportParameters.setParcolVisualizacion(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptHojaDiagnostico, ElementosIdiomaC.TEXT_CONTROLES, "colVisualizacion", ""));
		reportParameters
				.setParlblInformacion(MGeneral.Idioma.obtenerMensaje(EnumMensajes.FormatoEsLegalia2, "", "", ""));
		reportParameters.setPartxtErroresImplicitos(String.valueOf(erroresSecundarios.size()));
		reportParameters.setPartxtErroresPrimarios(String.valueOf(erroresPrimarios.size()));

		return reportParameters;
	}

	private static ReportInstanciaParameters createReportParameters() {
		ReportInstanciaParameters reportParameters = new ReportInstanciaParameters();

		reportParameters.setRutaImagen(ReportingPreviewService.class.getResource("/imagenes/Logo_REG.jpg").toString());

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
		reportParameters.setParlblTotalLibros(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "lblTotalLibros", ""));
		reportParameters.setPartxtLOPD(MGeneral.Idioma.obtenerValor(ObjetosIdioma.REPORTES,
				Reportes.rptPresentacionDatos, ElementosIdiomaC.TEXT_CONTROLES, "txtLOPD", ""));

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
		reportParameters.setPartxtOtros(MGeneral.mlform.Presentacion.getDatosRegistralesOtros());
		reportParameters.setPartxtTotalLibros(String.valueOf(MGeneral.mlform.Libros.size()));
	
		return reportParameters;
	}

	private static ValidationResult validar() {
		ValidationResult result = new ValidationResult();
		kLegalizacion.enumResultadoValidacion resultado = MGeneral.mlform.valida();

		if (resultado != kLegalizacion.enumResultadoValidacion.Valida) {
			for (int i = 0; i < MGeneral.mlform.MensajesReglas.length; i++) {
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsError) {
					result.getLstErroresPrimarios().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsAviso) {
					result.getLstErroresSecundarios().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
			}
		}
		return result;
	}

	public static class ValidationResult {
		private List<String> lstErroresPrimarios;
		private List<String> lstErroresSecundarios;

		public ValidationResult() {
			this.lstErroresPrimarios = new ArrayList<>();
			this.lstErroresSecundarios = new ArrayList<>();
		}

		public List<String> getLstErroresPrimarios() {
			return lstErroresPrimarios;
		}

		public List<String> getLstErroresSecundarios() {
			return lstErroresSecundarios;
		}
	}

}