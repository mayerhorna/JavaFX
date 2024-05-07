package com.commerceapp.service;

import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.FileReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;

import java.text.DateFormat;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Date;

import java.util.Formatter;

import java.util.List;

import java.util.Optional;

import java.util.Scanner;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;

import com.commerceapp.controller.MenuPrincipalController;
import com.commerceapp.domain.ConfiguracionC;
import com.commerceapp.domain.DatosReciboVenta;
import com.commerceapp.domain.Errores;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.MHuella;
import com.commerceapp.domain.Progreso;
import com.commerceapp.domain.ZipUtil;
import com.commerceapp.domain.cEncriptacion;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.cAdjuntosPdf;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.cPresentacion;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesEstructura;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesImplicitasCuestionario;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesPrimariasCuestionario;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesPrimariasLibro;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesPrimariasLibros;
import com.commerceapp.domain.legalizacion.kLegalizacion.enumFormato;
import com.commerceapp.maestros.FormatoFichero;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.maestros.MaestroModoEncriptacion;
import com.commerceapp.maestros.TipoLibro;
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.DatePicker;

import javafx.scene.control.ProgressBar;

import javafx.stage.Modality;

import javafx.stage.Stage;

import javafx.stage.StageStyle;

public class LegalizacionService {

	private static final Logger logger = Logger.getLogger(LegalizacionService.class.getName());

	public static final String K_COMPILACION = "LEGALIA2";
	public DatosReciboVenta objDtosRecibo = new DatosReciboVenta();

	public enum EnumModo {

		Normal(1), Recepcion(2), Dll(3), SoloLectura(4), SoloReenviar(5);

		private final int valor;

		EnumModo(int valor) {

			this.valor = valor;

		}

		public int getValor() {

			return valor;

		}

	}

	/**
	 * 
	 * Resultado que se puede obtener al realizar la generación del ZIP
	 * 
	 */

	public enum EnumResultadoZip {

		Correcto(1), NoValida(2), Excepcion(3);

		private final int valor;

		EnumResultadoZip(int valor) {

			this.valor = valor;

		}

		public int getValor() {

			return valor;

		}

	}

	private EnumModo _Modo;

	private String _PathDatos; // Path completo al directorio de la legalización

	private String _NombreDirectorioDatos; // Nombre del directorio de la legalización

	private int _NumeroLibros; // Numero de libros de la legalización

	private static boolean _MostrarMensajes; // Indica si se deben mostrar o no mensajes

	public Errores ErrorOcurrido; // Suclase para el control de errores

	public cDatos Datos; // Subclase con los datos generales (nombre, descripción, ejercicio, ...)

	public cPresentacion Presentacion; // Subclase con los datos de presentación (Registro, Cif, ....)

	private int _NumeroTotalFicherosPresentados; // Número total de ficheros que incluye la legalización

	public ArrayList<cLibro> Libros; // =new ArrayList<>() Array de subclases Libros() de tipo cLibro:

	// libros de la legalización

	public EnumValidacionesEstructura[] ValidacionesEstructuraNoCumple; // Array de validaciones de estructura que no

	// cumple

	public EnumValidacionesPrimariasCuestionario[] ValidacionesPrimariasCuestionarioNoCumple; // Array de validaciones

	// primarias del

	// cuestionario que no

	// cumple

	public EnumValidacionesImplicitasCuestionario[] ValidacionesImplicitasCuestionarioNoCumple; // Array de validaciones

	// implícitas del

	// cuestionario que no

	// cumple

	public EnumValidacionesPrimariasLibros[] ValidacionesPrimariasLibrosNoCumple; // Array de validaciones primarias de

	// libros que no cumple

	private boolean _ValidaCargaDeLibros; // Booleano que indica si se puede realizar o no la carga de los libros de la

	// legalización

	public MensajesReglasC[] MensajesReglas; // Array con los mensajes de reglas correspondientes a las validaciones que

	// no cumple

	public cAdjuntosPdf Adjuntos;

	public Progreso vProgreso = null;

	private boolean _UltimoFicheroSeHaEncriptado;

//Getter y Setters

	public EnumModo getModo() {

		return _Modo;

	}

	public void setModo(EnumModo value) {

		_Modo = value;

	}

// Propiedad PathDatos

	public String getPathDatos() {

		return _PathDatos;

	}

// Propiedad NombreDirectorioDatos

	public String getNombreDirectorioDatos() {

		return _NombreDirectorioDatos;

	}

// Propiedad NumeroLibros

	public int getNumeroLibros() {

		return _NumeroLibros;

	}

// Propiedad NumeroFicheros

	public int getNumeroFicheros() {

		int numFicheros = 0;

		try {

			if (Libros == null)

				return 0;

			for (int i = 0; i < _NumeroLibros; i++) {

				numFicheros += Libros.get(i).getNumeroFicheros();

			}

			return numFicheros;

		} catch (Exception ex) {

			// Manejo de excepciones si es necesario

			return 0;

		}

	}

// Propiedad NumeroTotalFicherosPresentados

	public int getNumeroTotalFicherosPresentados() {

		return _NumeroTotalFicherosPresentados;

	}

	public void setNumeroTotalFicherosPresentados(int value) {

		_NumeroTotalFicherosPresentados = value;

	}

	public String getPathFicheroDatos() {

		return Paths.get(_PathDatos, kLegalizacion.kNombreFicheroDatos).toString();

	}

	public String getPathFicheroNombres() {

		return Paths.get(_PathDatos, kLegalizacion.kNombreFicheroNombres).toString();

	}

	public String getPathFicheroDesc() {

		return Paths.get(_PathDatos, kLegalizacion.kNombreFicheroDesc).toString();

	}

	public String getPathFicheroInstancia() {

		return Paths.get(_PathDatos, kLegalizacion.kNombreFicheroInstancia).toString();

	}

	public String getPathFicheroZip() {

		return Paths.get(_PathDatos, Datos.get_NombreZip()).toString();

	}

	public String getPathFicheroNE() {

		return Paths.get(_PathDatos, Datos.get_eDocNombreFicheroNE()).toString();

	}

	/**
	 * 
	 * Recorre todos los ficheros relacionados con la legalización que deben ser
	 * 
	 * incluidos en el ZIP calculando su tamaño total en bytes
	 * 
	 */

	public long getBytes() {

		int i, j;

		long contbytes = 0;

		File fi;

		if (Libros == null)

			return 0;

		// Tamaño del fichero de datos

		fi = new File(getPathFicheroDatos());

		if (fi.exists()) {

			contbytes += fi.length();

		}

		// Tamaño del fichero de nombres

		fi = new File(getPathFicheroNombres());

		if (fi.exists()) {

			contbytes += fi.length();

		}

		// Tamaño del fichero de descripción

		fi = new File(getPathFicheroDesc());

		if (fi.exists()) {

			contbytes += fi.length();

		}

		// Tamaño de los ficheros de los libros

		for (i = 0; i < _NumeroLibros; i++) {

			for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

				contbytes += Libros.get(i).Ficheros[j].getBytes();

			}

		}

		return contbytes;

	}

	/**
	 * 
	 * Obtención del valor asignado en el constructor que indica si se muestran o no
	 * 
	 * mensajes
	 * 
	 */

	public boolean isMostrarMensajes() {

		return _MostrarMensajes;

	}

	/**
	 * 
	 * Según los datos contenidos en el array ValidacionesEstructuraNoCumple,
	 * 
	 * calcula si cumple la validación de estructura
	 * 
	 */

	public boolean isValidaEstructura() {

		return ValidacionesEstructuraNoCumple == null;

	}

	/**
	 * 
	 * Según los datos contenidos en el array
	 * 
	 * ValidacionesPrimariasCuestionarioNoCumple, calcula si cumple las validaciones
	 * 
	 * primarias del cuestionario
	 * 
	 */

	public boolean isValidaPrimariasCuestionario() {

		return ValidacionesPrimariasCuestionarioNoCumple == null;

	}

	/**
	 * 
	 * Según los datos contenidos en el array
	 * 
	 * ValidacionesImplicitasCuestionarioNoCumple, calcula si cumple las
	 * 
	 * validaciones implícitas del cuestionario
	 * 
	 */

	public boolean isValidaImplicitasCuestionario() {

		return ValidacionesImplicitasCuestionarioNoCumple == null;

	}

	public boolean isValidaPrimariasLibros() {

		return ValidacionesPrimariasLibrosNoCumple == null;

	}

	/**
	 * 
	 * Recorre todos los libros viendo si cada uno cumple las validaciones primarias
	 * 
	 * de un libro
	 * 
	 */

	public boolean isValidaPrimariasTodosLosLibros() {

		boolean validatodos = true;

		for (int i = 0; i < _NumeroLibros; i++) {

			if (Libros.get(i).getValidacionesPrimariasNoCumple() != null) {

				validatodos = false;

				break;

			}

		}

		return validatodos;

	}

	/**
	 * 
	 * Recorre todos los libros, y de cada libro todos sus ficheros, viendo si cada
	 * 
	 * uno cumple las validaciones primarias de un fichero
	 * 
	 */

	public boolean isValidaPrimariasTodosLosFicheros() {

		boolean validatodos = true;

		for (int i = 0; i < _NumeroLibros; i++) {

			for (int j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

				if (Libros.get(i).Ficheros[j].getValidacionesPrimariasNoCumple() != null) {

					validatodos = false;

					break;

				}

			}

		}

		return validatodos;

	}

	/**
	 * 
	 * Obtener si valida o no la carga de libros
	 * 
	 */

	public boolean isValidaCargaDeLibros() {

		return _ValidaCargaDeLibros;

	}

	public String getPathFicheroEnviado() {

		return Paths.get(_PathDatos, Datos.get_eDocNombreFicheroEnviado()).toString();

	}

	public String getPathFicheroAcuseEntrada() {

		return Datos.get_eDocNombreFicheroAcuseEntrada();

	}

	// Obtiene el menor ejercicio existente en las fechas de apertura de todos los

	// ficheros de la legalización

	public int getMenorEjercicio() {

		int menor = 0;

		LocalDate mfa = getMenorFechaApertura(-1, -1);

		if (mfa != LocalDate.MAX)

			menor = mfa.getYear();

		return menor;

	}

	/**
	 * 
	 * Obtiene la menor fecha de apertura de todos los ficheros de la legalización
	 * 
	 */

	public LocalDate getMenorFechaApertura(int indicelibroquitar, int indiceficheroquitar) {

		int i, j;

		LocalDate fecha;

		LocalDate menorfa = LocalDate.MAX;

		for (i = 0; i < _NumeroLibros; i++) {

			if (Libros.get(i).comprobarFechaApertura()) {

				for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					if (i == indicelibroquitar && j == indiceficheroquitar) {

					} else {

						if (!Formato.ValorNulo(Libros.get(i).Ficheros[j].getFechaApertura())) {

							fecha = LegalizacionService.dameFecha(Libros.get(i).Ficheros[j].getFechaApertura());

							if (fecha != LocalDate.of(1, 1, 1)) {

								if (fecha.isBefore(menorfa))

									menorfa = fecha;

							}

						}

					}

				}

			}

		}

		return menorfa;

	}

	public LocalDate getMayorFechaCierre(int indicelibroquitar, int indiceficheroquitar) {

		int i, j;

		LocalDate fecha;

		LocalDate mayorfc = LocalDate.of(1, 1, 1);

		for (i = 0; i < _NumeroLibros; i++) {

			for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

				if (i == indicelibroquitar && j == indiceficheroquitar) {

				} else {

					if (!Formato.ValorNulo(Libros.get(i).Ficheros[j].getFechaCierre())) {

						fecha = LegalizacionService.dameFecha(Libros.get(i).Ficheros[j].getFechaCierre());

						if (fecha != LocalDate.of(1, 1, 1)) {

							if (fecha.isAfter(mayorfc))

								mayorfc = fecha;

						}

					}

				}

			}

		}

		return mayorfc;

	}

	/*
	 * 
	 * Obtiene el nombre del fichero ZIP por defecto:prefijo+
	 * 
	 * 
	 * 
	 * código Registro (formateado a 5) + Cif de la sociedad
	 * 
	 */

	/*
	 * 
	 * public String getNombreZipPorDefecto() { String cad = String.format("%05d",
	 * 
	 * Presentacion.getRegistroMercantilDestinoCodigo()); return "LL" + cad +
	 * 
	 * Presentacion.getNifCif(); }
	 * 
	 */

	public String getNombreZipPorDefecto() {

		String cad = "00000" + Presentacion.getRegistroMercantilDestinoCodigo();

		cad = cad.substring(cad.length() - 5);

		return "LL" + cad + Presentacion.getNifCif();

	}

	/**
	 * 
	 * Devuelve una cadena con detalle sobre la legalización: formato ...
	 * 
	 */

	public String getDetalleLegalizacion() {

		String cad = "";

		switch (MGeneral.mlform.Datos.getFormato()) {

		case Legalia:

			cad = MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.FormatoEsLegalia, null, null, null)

					+ System.lineSeparator();

			if (MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				cad += MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.SeAplicanReglasNuevasaLegalia, null, null,

						null);

			} else {

				cad += MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.NoSeAplicanReglasNuevasaLegalia, null, null,

						null);

			}

			break;

		case Legalia2:

			cad = MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.FormatoEsLegalia2, null, null, null);

			break;

		}

		return cad;

	}

	/**
	 * 
	 * Obtención si la última operación de importar o modificar un fichero, se ha
	 * 
	 * realizado encriptándolo
	 * 
	 */

	public boolean isUltimoFicheroSeHaEncriptado() {

		return _UltimoFicheroSeHaEncriptado;

	}

	public String getPathFicheroeDocInicial() {

		if (Formato.ValorNulo(Datos.get_NombreZip())) {

			return "";

		}

		return Paths.get(_PathDatos,

				Paths.get(Datos.get_NombreZip()).getFileName().toString().replaceFirst("[.][^.]+$", "") + ".Xml")

				.toString();

	}

//Constructor

	public LegalizacionService() {

		inicializa("");

	}

	public LegalizacionService(boolean mostrarLosMensajes, EnumModo queModo) {

		_MostrarMensajes = mostrarLosMensajes;

		if (queModo == null) {

			queModo = EnumModo.Normal;

		}

		_Modo = queModo;

		inicializa("");

	}

//Inicializa

	public void inicializa(String pathDatosLegalizacion) {

		_PathDatos = "";

		_NombreDirectorioDatos = "";

		Datos = new cDatos();

		_NumeroLibros = 0;

		_NumeroTotalFicherosPresentados = 0;

		ErrorOcurrido = new Errores();

		Presentacion = new cPresentacion();

		Libros = new ArrayList<cLibro>();

		inicializaValidaciones();

		_PathDatos = pathDatosLegalizacion;

		_NombreDirectorioDatos = new File(pathDatosLegalizacion).getName();

		Adjuntos = new cAdjuntosPdf(_PathDatos);

		// Sección condicional

		if (!MGeneral.ModoBatch && (_Modo == EnumModo.Normal || _Modo == EnumModo.Recepcion

				|| _Modo == EnumModo.SoloLectura || _Modo == EnumModo.SoloReenviar)) {

			if ("LEGALIA2".equals(K_COMPILACION)) {

			}

		}

	}

	public void iniciarProgressBar(ProgressBar pb) {

		if (K_COMPILACION.equals("LEGALIA2")) {

			vProgreso = new Progreso(null, pb, null, null);

		}

	}

	private void inicializaValidaciones() {

		int i, j;

		_ValidaCargaDeLibros = true;

		ValidacionesEstructuraNoCumple = null;

		ValidacionesPrimariasCuestionarioNoCumple = null;

		ValidacionesPrimariasLibrosNoCumple = null;

		ValidacionesImplicitasCuestionarioNoCumple = null;

		for (i = 0; i < _NumeroLibros; i++) {

			Libros.get(i).ValidacionesPrimariasNoCumple = null;

			for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

				Libros.get(i).Ficheros[j].ValidacionesPrimariasNoCumple = null;

			}

		}

		MensajesReglas = null;

	}

//Metodos

	// Region Cargar y guardar

	public boolean carga(String pathDatosLegalizacion) {

		boolean cargaExitosa = false;

		try {

			inicializa(pathDatosLegalizacion);

			File directorioDatos = new File(pathDatosLegalizacion);

			if (!directorioDatos.exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.DirectorioInexistente, _PathDatos,

						null);

				return cargaExitosa;

			}

			if (!validarEstructura())

				return cargaExitosa;

			if (!cargaDatos())

				return cargaExitosa;

			if (!cargaPresentacion())

				return cargaExitosa;

			if (!cargaLibros())

				return cargaExitosa;

			// En Modo Recepción se calculan las huellas

			// ya que se comparan con las que se introducen manualmente

			if (_Modo == EnumModo.Recepcion && !calculaHuellas())

				return cargaExitosa;

			cargaExitosa = true;

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return cargaExitosa;

	}

	public boolean cargaPresentacion() {

		boolean cargaExitosa = false;

		int fileNum = 0;

		FileInputStream fis = null;

		// BufferedReader para leer de manera eficiente desde el FileReader

		InputStreamReader is = null;

		BufferedReader bufferedReader = null;

		try {

			if (!new File(getPathFicheroDatos()).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente,

						getPathFicheroDatos(), null);

				return cargaExitosa;

			}

			// fileNum = new File(getPathFicheroDatos()).exists() ? new

			// FileReader(getPathFicheroDatos()) : 0;

			fis = new FileInputStream(getPathFicheroDatos());

			is = new InputStreamReader(fis, "ISO-8859-1");

			bufferedReader = new BufferedReader(is);

			// bufferedReader = new BufferedReader(new FileReader(new

			// File(getPathFicheroDatos()), StandardCharsets.UTF_8));

			String linea;

			String codigocampo;

			String valorcampo;

			while ((linea = bufferedReader.readLine()) != null) {

				linea = linea.trim(); // v1.1.8 quitar espacios en blanco

				if (linea.length() > 0) {

					codigocampo = linea.substring(0, 3);

					valorcampo = linea.substring(3);

					switch (codigocampo) {

					case kLegalizacion.kRegistroMercantilDestinoDescripcionCodigoCampo:

						valorcampo = valorcampo.replace("/", "-");

						Presentacion.setRegistroMercantilDestinoDescripcion(valorcampo);

						break;

					case kLegalizacion.kRegistroMercantilDestinoCodigoCodigoCampo:

						Presentacion.setRegistroMercantilDestinoCodigo(valorcampo);

						break;

					case kLegalizacion.kFechaSolicitudCodigoCampo:

						if (Formato.ValorNulo(valorcampo)) {

							aniadeValidacionPrimariaCuestionarioNoCumplida(

									EnumValidacionesPrimariasCuestionario.FechadelaSolicitudFalta);

						} else {

							if (!comprobarFecha(valorcampo, new LocalDate[1])) {

								aniadeValidacionPrimariaCuestionarioNoCumplida(

										EnumValidacionesPrimariasCuestionario.FechaSolicitudNoValida);

							}

						}

						Presentacion.setFechaSolicitud(valorcampo);

						break;

					case kLegalizacion.kNombreSociedadoEmpresarioCodigoCampo:

						Presentacion.setNombreSociedadoEmpresario(valorcampo);

						break;

					case kLegalizacion.kApellido1CodigoCampo:

						Presentacion.setApellido1(valorcampo);

						break;

					case kLegalizacion.kApellido2CodigoCampo:

						Presentacion.setApellido2(valorcampo);

						break;

					case kLegalizacion.kNifCifCodigoCampo:

						Presentacion.setNifCif(valorcampo);

						break;

					case kLegalizacion.kDomicilioCodigoCampo:

						Presentacion.setDomicilio(valorcampo);

						break;

					case kLegalizacion.kCiudadCodigoCampo:

						Presentacion.setCiudad(valorcampo);

						break;

					case kLegalizacion.kCodigoPostalCodigoCampo:

						Presentacion.setCodigoPostal(valorcampo);

						break;

					case kLegalizacion.kProvinciaCodigoCodigoCampo:

						Presentacion.setProvinciaCodigo(valorcampo);

						break;

					case kLegalizacion.kFaxCodigoCampo:

						Presentacion.setFax(valorcampo);

						break;

					case kLegalizacion.kTelefonoCodigoCampo:

						Presentacion.setTelefono(valorcampo);

						break;

					case kLegalizacion.kDatosRegistralesTomoCodigoCampo:

						Presentacion.setDatosRegistralesTomo(valorcampo);

						break;

					case kLegalizacion.kDatosRegistralesLibroCodigoCampo:

						Presentacion.setDatosRegistralesLibro(valorcampo);

						break;

					case kLegalizacion.kDatosRegistralesFolioCodigoCampo:

						Presentacion.setDatosRegistralesFolio(valorcampo);

						break;

					case kLegalizacion.kDatosRegistralesHojaCodigoCampo:

						Presentacion.setDatosRegistralesHoja(valorcampo);

						break;

					case kLegalizacion.kDatosRegistralesOtrosCodigoCampo:

						Presentacion.setDatosRegistralesOtros(valorcampo);

						break;

					case kLegalizacion.kTipoRegistroPublicoCodigoCampo:

						Presentacion.setTipoRegistroPublico(valorcampo);

						break;

					case kLegalizacion.kPresentanteNombreCodigoCampo:

						Presentacion.getPresentante().set_Nombre(valorcampo);

						break;

					case kLegalizacion.kPresentanteApellido1CodigoCampo:

						Presentacion.getPresentante().set_Apellido1(valorcampo);

						break;

					case kLegalizacion.kPresentanteApellido2CodigoCampo:

						Presentacion.getPresentante().set_Apellido2(valorcampo);

						break;

					case kLegalizacion.kPresentanteNifCodigoCampo:

						Presentacion.getPresentante().set_Nif(valorcampo);

						break;

					case kLegalizacion.kPresentanteDomicilioCodigoCampo:

						Presentacion.getPresentante().set_Domicilio(valorcampo);

						break;

					case kLegalizacion.kPresentanteCiudadCodigoCampo:

						Presentacion.getPresentante().set_Ciudad(valorcampo);

						break;

					case kLegalizacion.kPresentanteCodigoPostalCodigoCampo:

						Presentacion.getPresentante().set_CodigoPostal(valorcampo);

						break;

					case kLegalizacion.kPresentanteProvinciaCodigoCodigoCampo:

						Presentacion.getPresentante().set_ProvinciaCodigo(valorcampo);

						break;

					case kLegalizacion.kPresentanteFaxCodigoCampo:

						Presentacion.getPresentante().set_Fax(valorcampo);

						break;

					case kLegalizacion.kPresentanteTelefonoCodigoCampo:

						Presentacion.getPresentante().set_Telefono(valorcampo);

						break;

					case kLegalizacion.kPresentanteEmailCodigoCampo:

						Presentacion.getPresentante().set_Email(valorcampo);

						break;

					case kLegalizacion.kPresentanteSolicitaRetencionCodigoCampo:

						Presentacion.getPresentante().set_SolicitaRetencion(valorcampo);

						break;

					}

				}

			}

			if (Formato.ValorNulo(Presentacion.getRegistroMercantilDestinoCodigo())) {

				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Registros");

				Presentacion.setRegistroMercantilDestinoCodigo(

						maestro.obtenerCodigoDeDescripcion(Presentacion.getRegistroMercantilDestinoDescripcion()));

			}

			// Si no se ha leído de DESC.TXT el campo TipoPersona, se calcula

			if (Formato.ValorNulo(Datos.get_TipoPersona())) {

				if (Formato.ValorNulo(Presentacion.getApellido1()) == false

						|| Formato.ValorNulo(Presentacion.getApellido2()) == false) {

					// Si tiene alguno de los dos apellidos

					Datos.set_TipoPersona(kLegalizacion.kTipoPersonaFisica);

				} else {

					Datos.set_TipoPersona(kLegalizacion.KTipoPersonaDefecto);

					if (Formato.EsNifDePersona(Presentacion.getNifCif()) == true) {

						Datos.set_TipoPersona(kLegalizacion.kTipoPersonaFisica);

					}

				}

			}

			cargaExitosa = true;

		} catch (IOException | NumberFormatException ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		} finally {

			try {

				if (bufferedReader != null) {

					fis.close();

					is.close();

					bufferedReader.close();

				}

			} catch (IOException ignored) {

			}

		}

		return cargaExitosa;

	}

	private boolean cargaLibros() throws IOException {

		boolean cargaLibros = false;

		int fileDatos;

		int fileNombres;

		BufferedReader datos = null;

		BufferedReader nombres = null;

		String linea;

		String codigoCampo;

		String valorCampo;

		String lineaNombres;

		int contadorFicheros = 0;

		String codigoSubcampo;

		String valorSubcampo = "";

		String numeroSecuencialFicheroLeido;

		String numeroSecuencialFicheroActual = "";

		kLegalizacion.enumTipoLibro tipoLibroActual = null;

		kLegalizacion.enumTipoLibro tipoLibroLeido = null;

		String nombreFicheroLeido = "";

		cFicheroLibro ficheroLibro = null;

		boolean leerLibros = false;

		cLibro libro;

		String mensajeRegla = "";

		try {

			Libros = new ArrayList<>();

			_NumeroLibros = 0;

			if (!validarCargaLibros()) {

				return false;

			}

			if (!new java.io.File(_PathDatos).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.DirectorioInexistente, _PathDatos,

						null);

				return false;

			}

			if (!new java.io.File(getPathFicheroDatos()).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente,

						getPathFicheroDatos(), null);

				return false;

			}

			if (!new java.io.File(getPathFicheroNombres()).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente,

						getPathFicheroNombres(), null);

				return false;

			}

			datos = new BufferedReader(new FileReader(getPathFicheroDatos()));

			fileDatos = datos.hashCode();

			nombres = new BufferedReader(new FileReader(getPathFicheroNombres()));

			fileNombres = nombres.hashCode();

			boolean LeerLibros = false;

			kLegalizacion.enumTipoLibro tipolibroactual = null;

			while ((linea = datos.readLine()) != null) {

				if (!linea.trim().isEmpty()) {

					// Separar la línea leída en código de campo y valor

					String codigocampo = linea.substring(0, 3);

					String valorcampo = linea.substring(3);

					// Hasta no leer el código de campo 501 no se activa la lectura de libros ya que

					// debe ser previo a los códigos de campo de los libros

					if (codigocampo.equals(kLegalizacion.kNumeroTotalLibrosPresentadosCodigoCampo)) { // 501

						LeerLibros = true;

						_NumeroTotalFicherosPresentados = Integer.parseInt(valorcampo);

					}

					// Si el codigo de campo está comprendido entre los que hay que leer

					if (LeerLibros && codigocampo.compareTo(kLegalizacion.kPrimerFicheroCodigoCampo) >= 0

							&& codigocampo.compareTo(kLegalizacion.kUltimoFicheroCodigoCampo) <= 0) {

						// Obtener el código de subcampo y valor del subcampo

						codigoSubcampo = valorcampo.substring(0, 2);

						valorSubcampo = valorcampo.substring(2);

						numeroSecuencialFicheroLeido = codigocampo;

						if (!numeroSecuencialFicheroLeido.equals(numeroSecuencialFicheroActual)) {

							// Se trata de un fichero nuevo

							if (!String.valueOf(numeroSecuencialFicheroActual).isEmpty()) {

								// Añadir el fichero anterior

								Libros.get(_NumeroLibros - 1).aniadeFichero(ficheroLibro);

							}

							numeroSecuencialFicheroActual = numeroSecuencialFicheroLeido;

							contadorFicheros++;

							if (!numeroSecuencialFicheroLeido.equals((String.format("%03d", contadorFicheros)))) {

								// No se cumple la secuencia de ficheros

								ErrorOcurrido.generaError(_MostrarMensajes,

										IdiomaC.EnumMensajes.EstructuraDatosTxtIncorrecta, null, null);

								nombres.close();

								datos.close();

								return false;

							}

							if (!codigoSubcampo.equals(kLegalizacion.kDescripcionLibroCodigoSubcampo)) {

								// Cuando comienza un fichero se obliga a que exista el 01 con el tipo de libro

								ErrorOcurrido.generaError(_MostrarMensajes,

										IdiomaC.EnumMensajes.EstructuraDatosTxtIncorrecta, null, null);

								nombres.close();

								datos.close();

								return false;

							}

							// Lectura del nombre del fichero correspondiente de NOMBRES.TXT

							String nombreficheroleido = "";

							if (fileNombres != 0) {

								String lineanombres = nombres.readLine().trim(); // v1.1.5 quitar espacios en blanco

								if (!Formato.ValorNulo(lineanombres)) {

									nombreficheroleido = lineanombres;

								}

							}

							if (nombreficheroleido.isEmpty()) {

								ErrorOcurrido.generaError(_MostrarMensajes,

										IdiomaC.EnumMensajes.EstructuraDatosTxtIncorrecta, mensajeRegla, null);

								nombres.close();

								datos.close();

								return false;

							}

							tipoLibroLeido = cLibro.obtenerTipoLibroSegunNombreFichero(nombreficheroleido);

							if (tipoLibroLeido == null) {

								// tipolibroleido puede ser null si no corresponde con ninguno de los admitidos

								ErrorOcurrido.generaError(_MostrarMensajes,

										IdiomaC.EnumMensajes.EstructuraDatosTxtIncorrecta, null, null);

								nombres.close();

								datos.close();

								return false;

							}

							// Cada vez que cambia el nº secuencial (se lee un 01 se va a añadir un fichero)

							ficheroLibro = new cFicheroLibro();

							ficheroLibro.setNombreFichero(nombreficheroleido);

							if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

								ficheroLibro.setPathFichero(_PathDatos + "\\" + nombreficheroleido);

							} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

								ficheroLibro.setPathFichero(_PathDatos + "/" + nombreficheroleido);

							}

						}

						// tipoLibroLeido se vuellve nulo

						if (tipoLibroLeido != tipolibroactual) {

							// Ha cambiado el tipo de libro

							tipolibroactual = tipoLibroLeido;

							libro = new cLibro();

							libro.setTipoLibro(tipoLibroLeido);

							aniadeLibro(libro);

						} else {

							// Es del mismo tipo de libro

						}

						// Según el código de subcampo se asocia el valor leído a la propiedad

						// correspondiente

						switch (codigoSubcampo) {

						case kLegalizacion.kDescripcionLibroCodigoSubcampo:

							ficheroLibro.setDescripcion(valorSubcampo);

							break;

						case kLegalizacion.kNumeroLibroCodigoSubcampo:

							if (valorSubcampo.matches("\\d+"))

								ficheroLibro.setNumero(Integer.parseInt(valorSubcampo));

							break;

						case kLegalizacion.kFechaAperturaCodigoSubcampo:

							ficheroLibro.setFechaApertura(valorSubcampo);

							break;

						case kLegalizacion.kFechaCierreCodigoSubcampo:

							ficheroLibro.setFechaCierre(valorSubcampo);

							break;

						case kLegalizacion.kFechaCierreUltimoLegalizadoCodigoSubcampo:

							// Si es el primer fichero del libro, se asigna la fecha cierre UltimoLegalizado

							if (Libros.get(_NumeroLibros - 1).getNumeroFicheros() == 0) {

								Libros.get(_NumeroLibros - 1).setFechaCierreUltimoLegalizado(valorSubcampo);

							}

							break;

						case kLegalizacion.kHuellaCodigoSubcampo:

							ficheroLibro.setHuella(valorSubcampo);

							break;

						}

					}

				} else {

					break;

				}

			}

			if (!String.valueOf(numeroSecuencialFicheroActual).isEmpty()) {

				// Añadir último fichero

				Libros.get(_NumeroLibros - 1).aniadeFichero(ficheroLibro);

			}

			cargaLibros = true;

		} catch (IOException ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		} finally {

			nombres.close();

			datos.close();

		}

		return cargaLibros;

	}

	public boolean cargaDatos() {

		boolean cargaExitosa = false;

		try {

			Datos = new cDatos();

			Datos.set_PathDatos(_PathDatos);

			if (!new File(getPathFicheroDesc()).exists()) {

				// Si el fichero DESC no existe

				Datos.set_Descripcion(_NombreDirectorioDatos);

				// Si en el directorio existe un fichero ZIP se toma como versión Legalia2

				// String[] ficherosZip = new File(_PathDatos).list((dir, name) ->

				// name.endsWith(".ZIP"));

				/*
				 * 
				 * if (ficherosZip != null && ficherosZip.length >= 1) {
				 * 
				 * Datos.setFormato(kLegalizacion.enumFormato.Legalia2); } else {
				 * 
				 * Datos.setFormato(kLegalizacion.enumFormato.Legalia); if
				 * 
				 * (MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {
				 * 
				 * Datos.setFormato(kLegalizacion.enumFormato.Legalia2); } }
				 * 
				 */

				cargaExitosa = true;

				return cargaExitosa;

			}

			// Si existe DESC.TXT, se va a obtener de él si se trata de versión Legalia o

			// Legalia2

			if (!cargaFicheroDesc()) {

				return cargaExitosa;

			}

			if (MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				Datos.setFormato(kLegalizacion.enumFormato.Legalia2);

			}

			cargaExitosa = true;

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return cargaExitosa;

	}

	public boolean cargaDatosLegalias(String pathDatosLegalizacion) {

		try {

			inicializa(pathDatosLegalizacion);

			if (!validarEstructura()) {

				return false; // Falta DATOS.TXT o NOMBRES.TXT

			}

			return cargaDatos();

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

			return false;

		}

	}

	public boolean cargaFicheroDesc() {

		boolean cargaExitosa = false;

		// int fileNum = 0;

		BufferedReader bufferedReader = null;

		String linea;

		int contLineas = 0;

		try {

			if (!new File(getPathFicheroDesc()).exists())

				return cargaExitosa;

			// fileNum = new File(getPathFicheroDesc()).exists() ? new

			// FileReader(getPathFicheroDesc()) : 0;

			// bufferedReader = new BufferedReader(new FileReader(getPathFicheroDesc()));

			bufferedReader = new BufferedReader(new FileReader(new File(getPathFicheroDesc()), StandardCharsets.UTF_8));

			while ((linea = bufferedReader.readLine()) != null) {

				linea = linea.trim(); // v1.1.8 quitar espacios en blanco

				if (linea.length() > 0) {

					contLineas++;

					if (contLineas == 1) {

						Datos.set_Descripcion(linea);

					} else {

						int posicionIgual = linea.indexOf("=");

						if (posicionIgual > 0) {

							String codigoCampo = linea.substring(0, posicionIgual);

							String valorCampo = linea.substring(posicionIgual + 1);

							switch (codigoCampo) {

							case kLegalizacion.kCodigoDatoFormato:

								if (isNumeric(valorCampo)) {

									try {

										// Datos.setFormato(kLegalizacion.enumFormato.valueOf(valorCampo));

									} catch (IllegalArgumentException e) {

										Datos.setFormato(kLegalizacion.enumFormato.Legalia2);

									}

								}

								break;

							case kLegalizacion.kCodigoDatoEjercicio:

								try {

									Datos.set_Ejercicio(Integer.parseInt(valorCampo));

								} catch (NumberFormatException ignored) {

								}

								break;

							case kLegalizacion.kCodigoDatoEnviado:

								Datos.set_Enviado(valorCampo);

								break;

							case kLegalizacion.kCodigoDatoNombreZip:

								try {

									Datos.set_NombreZip(valorCampo);

								} catch (Exception ignored) {

									Datos.set_NombreZip("");

								}

								break;

							case kLegalizacion.kCodigoDatoTipoPersona:

								try {

									Datos.set_TipoPersona(valorCampo);

								} catch (Exception ignored) {

									Datos.set_TipoPersona("");

								}

								break;

							case kLegalizacion.kVersionLegalia2Generacion:

								try {

									Datos.set_VersionLegalia2Generacion(valorCampo);

								} catch (Exception ignored) {

									Datos.set_VersionLegalia2Generacion("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocNumeroDocumento:

								try {

									Datos.set_eDocNumeroDocumento(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocNumeroDocumento("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocEntradaTipo:

								try {

									Datos.set_eDocEntradaTipo(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocEntradaTipo("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocEntradaSubsanada:

								try {

									Datos.set_eDocEntradaSubsanada(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocEntradaSubsanada("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocIdTramite:

								try {

									Datos.set_eDocIdTramite(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocIdTramite("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocNombreFicheroEnviado:

								try {

									Datos.set_eDocNombreFicheroEnviado(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocNombreFicheroEnviado("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocNombreFicheroAcuseEntrada:

								try {

									Datos.set_eDocNombreFicheroAcuseEntrada(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocNombreFicheroAcuseEntrada("");

								}

								break;

							case kLegalizacion.kCodigoDatoeDocNombreFicheroNE:

								try {

									Datos.set_eDocNombreFicheroNE(valorCampo);

								} catch (Exception ignored) {

									Datos.set_eDocNombreFicheroNE("");

								}

								break;

							case kLegalizacion.kCodigoDatoEnvioReintentable:

								try {

									Datos.set_EnvioReintentable(valorCampo);

								} catch (Exception ignored) {

									Datos.set_EnvioReintentable("");

								}

								break;

							case kLegalizacion.kCodigoDatoCodAccesoNif:

								try {

									Datos.set_CodAccesoNif(valorCampo);

								} catch (Exception ignored) {

									Datos.set_CodAccesoNif("");

								}

								break;

							case kLegalizacion.kCodigoDatoPresentanteNombreConfirmado:

								try {

									Datos.set_PresentanteNombreConfirmado(valorCampo);

								} catch (Exception ignored) {

									Datos.set_PresentanteNombreConfirmado("");

								}

								break;

							case kLegalizacion.kCodigoDatoPresentanteApellidosConfirmados:

								try {

									Datos.set_PresentanteApellidosConfirmados(valorCampo);

								} catch (Exception ignored) {

									Datos.set_PresentanteApellidosConfirmados("");

								}

								break;

							case kLegalizacion.kCodigoDatoPresentanteCorreoElectronicoConfirmado:

								try {

									Datos.set_PresentanteCorreoElectronicoConfirmado(valorCampo);

								} catch (Exception ignored) {

									Datos.set_PresentanteCorreoElectronicoConfirmado("");

								}

								break;

							default:

								// Manejar el caso por defecto o agregar más casos según sea necesario

								break;

							}

						}

					}

				}

			}

			// Si el fichero DESC.TXT solo tiene una línea se trata del formato de datos

			// Legalia

			if (contLineas == 1) {

				Datos.setFormato(kLegalizacion.enumFormato.Legalia);

			}

			cargaExitosa = true;

		} catch (IOException | IllegalArgumentException ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		} finally {

			try {

				if (bufferedReader != null) {

					bufferedReader.close();

				}

			} catch (IOException ignored) {

			}

		}

		return cargaExitosa;

	}

	public EnumResultadoZip generarZip(String nombreZip) {

		try {

			int i, j;

			int x = 0;

			ZipUtil z = new ZipUtil();

			boolean generaZip;

			String zipFic = "";

			StringBuilder caderror = new StringBuilder();

			// kLegalizacion.enumResultadoValidacion resultadoValida = valida();

			kLegalizacion.enumResultadoValidacion resultadoValida = kLegalizacion.enumResultadoValidacion.Valida;

			// Solo se genera Zip cuando Valida aunque sea con avisos

			if (resultadoValida == kLegalizacion.enumResultadoValidacion.NoValida) {

				return EnumResultadoZip.NoValida;

			}

			// Borrar todos los ficheros zip existentes

			for (String fichero : new File(_PathDatos).list()) {

				if (fichero.toLowerCase().endsWith(".zip")) {

					String ruta = "";

					if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

						ruta = _PathDatos + "\\" + fichero;

					} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

						ruta = _PathDatos + "/" + fichero;

					}

					if (!Ficheros.FicheroBorra(ruta)) {

						throw new Exception(

								IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.ErrorAlBorrarFichero, "", "", ""));

					}

				}

			}

			if (nombreZip != null) {

				nombreZip = getNombreZipPorDefecto() + ".ZIP";

			}

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				zipFic = _PathDatos + "\\" + nombreZip;

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				zipFic = _PathDatos + "/" + nombreZip;

			}

			Datos.set_NombreZip(nombreZip);// Para que el fichero DESC vaya ya con el nombre del zip

			Datos.set_Enviado("");

			// Se generan las huellas y se guarda la legalización

			/*
			 * 
			 * if (!generarHuellas()) { return EnumResultadoZip.Excepcion; }
			 * 
			 */

			String[] fileNames = new String[getNumeroFicheros() + 3];

			for (i = 0; i < _NumeroLibros; i++) {

				for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					fileNames[x] = Libros.get(i).Ficheros[j].getPathFichero();

					x++;

				}

			}

			fileNames[x] = getPathFicheroDatos();

			fileNames[x + 1] = getPathFicheroDesc();

			fileNames[x + 2] = getPathFicheroNombres();

			generaZip = ZipUtil.creaZip(fileNames, zipFic, caderror, null);

			if (!generaZip) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, "", "");

				return EnumResultadoZip.Excepcion;

			}

			// No se limita el tamaño máximo del zip a la hora de generar, sólo a la de

			// enviar

			Ficheros.FicheroSoloLectura(zipFic, true);

			return EnumResultadoZip.Correcto;

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, "", "");

			return EnumResultadoZip.Excepcion;

		}

	}

	public boolean enviarZipPorPortal() {

		boolean enviadoZipPorPortal = false;

		try {

			String pathZip;

			String pathFormateado;

			String url;

			if (Formato.ValorNulo(Datos.get_NombreZip())) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente, "ZIP", "");

				return enviadoZipPorPortal;

			}

			pathZip = new File(_PathDatos, Datos.get_NombreZip()).getPath();

			if (!new File(pathZip).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente, pathZip, "");

				return enviadoZipPorPortal;

			}

			if (MGeneral.Configuracion.getBytesMaximosZip() > 0) {

				File fi2 = new File(pathZip);

				long bytesZip = fi2.length();

				if (bytesZip > MGeneral.Configuracion.getBytesMaximosZip()) {

					double numero;

					String cadMaximos;

					String cadZip;

					numero = MGeneral.Configuracion.getBytesMaximosZip() / (1024 * 1024);

					cadMaximos = MGeneral.Configuracion.getBytesMaximosZip() + " bytes";

					cadMaximos += " (" + String.format("%,2f", numero) + "MB)";

					numero = bytesZip / (1024 * 1024);

					cadZip = bytesZip + " bytes";

					cadZip += " (" + String.format("%,2f", numero) + "MB)";

					ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.BytesMaximosZipExcedidos,

							cadMaximos, cadZip);

					return enviadoZipPorPortal;

				}

			}

			pathFormateado = Ficheros.CodificaPathLocal(pathZip);

			pathFormateado += MGeneral.mlform.Adjuntos.cadenaEnvio();

			// url = MGeneral.Configuracion.getUrlPortal() +

			// kLegalizacion.kVariablesUrlPortal + pathFormateado;

			// url = "https://sede.registradores.org/";

			/*
			 * 
			 * Runtime.getRuntime() .exec(new String[] { "cmd", "/c", "start", "\"\"",
			 * 
			 * kLegalizacion.kNavegadorPortal, url });
			 * 
			 */

			Utilidades.ProccessStarURL(MGeneral.Configuracion.getUrlPortal());

			LocalDate ahora = LocalDate.now();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

			String fechaFormateada = ahora.format(formatter);

			Datos.set_Enviado(fechaFormateada);

			if (!guardaDatosGenerales()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.NoSeHaPodidoGuardar, "", "");

				return false;

			}

			enviadoZipPorPortal = true;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");

		}

		return enviadoZipPorPortal;

	}

	public boolean generarHuellas() {

		try {

			int contador = 0;

			StringBuilder cadError = new StringBuilder();

			boolean hayError = false;

			/*
			 * 
			 * if (vProgreso != null) { vProgreso.siguienteProceso(getNumeroFicheros(),
			 * 
			 * MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.GenerandoHuellas).
			 * 
			 * toString()); }
			 * 
			 */

			for (int i = 0; i < getNumeroLibros(); i++) {

				for (int j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					if (Files.exists(Paths.get(Libros.get(i).Ficheros[j].getPathFichero()))) {

						// Siempre que se generan las huellas se hace el SHA256

						Libros.get(i).Ficheros[j].setHuella(MHuella

								.obtenerHuellaFicheroSHA256(Libros.get(i).Ficheros[j].getPathFichero(), cadError));

						if (Libros.get(i).Ficheros[j].getHuella().length() == 0) {

							ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, cadError, null);

							return false;

						}

					} else {

						ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente,

								Libros.get(i).Ficheros[j].getPathFichero(), null);

						return false;

					}

					contador++;

					/*
					 * 
					 * if (vProgreso != null) { vProgreso.mostrarProgreso(contador); }
					 * 
					 */

				}

			}

			boolean resultadoGuarda = guarda();

			if (resultadoGuarda) {

				return !hayError;

			} else {

				return false;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		} finally {

			/*
			 * 
			 * if (vProgreso != null) { vProgreso.procesoTerminado(); }
			 * 
			 */

		}

		return false;

	}

	public boolean calculaHuellas() {

		try {

			boolean hayError = false;

			for (int i = 0; i < _NumeroLibros; i++) {

				for (int j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					if (new File(Libros.get(i).Ficheros[j].getPathFichero()).exists()) {

						StringBuilder cadError = new StringBuilder();

						if (Datos.getFormato() == kLegalizacion.enumFormato.Legalia) {

							Libros.get(i).Ficheros[j].setHuellaCalculada(MHuella

									.obtenerHuellaFicheroMD5(Libros.get(i).Ficheros[j].getPathFichero(), cadError));

						} else {

							Libros.get(i).Ficheros[j].setHuellaCalculada(MHuella

									.obtenerHuellaFicheroSHA256(Libros.get(i).Ficheros[j].getPathFichero(), cadError));

						}

						if (Libros.get(i).Ficheros[j].getHuellaCalculada().length() == 0) {

							ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, cadError, null);

							hayError = true;

						}

					} else {

						ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.FicheroInexistente,

								Libros.get(i).Ficheros[j].getPathFichero(), null);

						hayError = true;

					}

				}

			}

			return !hayError;

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

			return false;

		}

	}

	public boolean guarda() {

		boolean guardado = false;

		String bloquedatospresentacion;

		StringBuilder bloquedatoslibros = new StringBuilder();

		String bloquedatoslibro;

		StringBuilder bloquenombres = new StringBuilder();

		String linea;

		int contadorLibros = 0;

		int contadorFicheros = 0;

		int i, j;

		String codigocampo;

		String codigosubcampo;

		String valorsubcampo;

		try {

			if (_Modo == EnumModo.Recepcion) {

				return false; //

			}

			if (_Modo == EnumModo.Dll) {

				return false; //

			}

			if (_Modo == EnumModo.SoloLectura) {

				return false; //

			}

			if (_Modo == EnumModo.SoloReenviar) {

				return false; //

			}

			if (!new File(_PathDatos).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.DirectorioInexistente, _PathDatos, "");

				return false;

			}

			bloquedatospresentacion = Presentacion.bloquePresentacion();

			// Se actualizan el nº ficheros leido con el real

			_NumeroTotalFicherosPresentados = getNumeroFicheros();

			linea = kLegalizacion.kNumeroTotalLibrosPresentadosCodigoCampo + getNumeroFicheros() + "\n";

			bloquedatoslibros.append(linea);

			for (i = 0; i < _NumeroLibros; i++) {

				if (!renombraFicherosLibro(i))

					return false;

				for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					bloquedatoslibro = "";

					contadorFicheros++;

					codigocampo = String.format("%03d", contadorFicheros);

					codigosubcampo = kLegalizacion.kDescripcionLibroCodigoSubcampo;

					valorsubcampo = Libros.get(i).Ficheros[j].getDescripcion();

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					codigosubcampo = kLegalizacion.kNumeroLibroCodigoSubcampo;

					valorsubcampo = String.valueOf(Libros.get(i).Ficheros[j].getNumero());

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					codigosubcampo = kLegalizacion.kFechaAperturaCodigoSubcampo;

					valorsubcampo = Libros.get(i).Ficheros[j].getFechaApertura();

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					codigosubcampo = kLegalizacion.kFechaCierreCodigoSubcampo;

					valorsubcampo = Libros.get(i).Ficheros[j].getFechaCierre();

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					codigosubcampo = kLegalizacion.kFechaCierreUltimoLegalizadoCodigoSubcampo;

					valorsubcampo = Libros.get(i).getFechaCierreUltimoLegalizado();

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					codigosubcampo = kLegalizacion.kHuellaCodigoSubcampo;

					valorsubcampo = Libros.get(i).Ficheros[j].getHuella();

					linea = codigocampo + codigosubcampo + valorsubcampo + "\n";

					bloquedatoslibro += linea;

					bloquedatoslibros.append(bloquedatoslibro);

					bloquenombres.append(Libros.get(i).Ficheros[j].getNombreFichero()).append("\n");

				}

			}

			if (!aplicaSoloLectura(false)) {

				return false;

			}

			if (!guardaFichero(getPathFicheroDatos(), bloquedatospresentacion + bloquedatoslibros.toString())) {

				return false;

			}

			if (!guardaFichero(getPathFicheroNombres(), bloquenombres.toString())) {

				return false;

			}

			if (!guardaDatosGenerales()) {

				return false;

			}

			guardado = true;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");

			ex.printStackTrace();

		} finally {

			aplicaSoloLectura(true);

			// guardado = true;

		}

		return guardado;

	}

	public boolean guardaDatosGenerales() {

		boolean resultado = false;

		String bloqueDatos = "";

		try {

			if (_Modo == EnumModo.Recepcion) {

				return false;

			}

			if (_Modo == EnumModo.Dll) {

				return false; //

			}

			if (_Modo == EnumModo.SoloLectura) {

				return false; //

			}

			// SoloReenviar sí que guarda los datos generales

			if (!new File(_PathDatos).exists()) {

				ErrorOcurrido.generaError(_MostrarMensajes, EnumMensajes.DirectorioInexistente, _PathDatos, "");

				return false;

			}

			String linea = Datos.get_Descripcion() + "\n";

			bloqueDatos += linea;

			// Versión de Legalia2 actual

			Datos.set_VersionLegalia2Generacion(MGeneral.Configuracion.getVersion());

			linea = kLegalizacion.kVersionLegalia2Generacion + "=" + Datos.get_VersionLegalia2Generacion() + "\n";

			bloqueDatos += linea;

			// Si se guarda es siempre con el formato Legalia2

			Datos.setFormato(kLegalizacion.enumFormato.Legalia2);

			linea = kLegalizacion.kCodigoDatoFormato + "=" + Datos.getFormato() + "\n";

			bloqueDatos += linea;

			// El menor de los ejercicios indicados

			Datos.set_Ejercicio(getMenorEjercicio());

			linea = kLegalizacion.kCodigoDatoEjercicio + "=" + Datos.get_Ejercicio() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoEnviado + "=" + Datos.get_Enviado() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoNombreZip + "=" + Datos.get_NombreZip() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoTipoPersona + "=" + Datos.get_TipoPersona() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocNumeroDocumento + "=" + Datos.get_eDocNumeroDocumento() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocEntradaTipo + "=" + Datos.get_eDocEntradaTipo() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocEntradaSubsanada + "=" + Datos.get_eDocEntradaSubsanada() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocIdTramite + "=" + Datos.get_eDocIdTramite() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocNombreFicheroEnviado + "=" + Datos.get_eDocNombreFicheroEnviado()

					+ "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocNombreFicheroAcuseEntrada + "="

					+ Datos.get_eDocNombreFicheroAcuseEntrada() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoeDocNombreFicheroNE + "=" + Datos.get_eDocNombreFicheroNE() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoEnvioReintentable + "=" + Datos.get_EnvioReintentable() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoCodAccesoNif + "=" + Datos.get_CodAccesoNif() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoPresentanteNombreConfirmado + "=" + Datos.get_PresentanteNombreConfirmado()

					+ "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoPresentanteApellidosConfirmados + "="

					+ Datos.get_PresentanteApellidosConfirmados() + "\n";

			bloqueDatos += linea;

			linea = kLegalizacion.kCodigoDatoPresentanteCorreoElectronicoConfirmado + "="

					+ Datos.get_PresentanteCorreoElectronicoConfirmado() + "\n";

			bloqueDatos += linea;

			String pathFicheroDesc = getPathFicheroDesc();

			if (Ficheros.FicheroExiste(pathFicheroDesc) == Ficheros.ExistFicheroEnum.Existe) {

				if (!Ficheros.FicheroSoloLectura(pathFicheroDesc, false))

					return false;

			}

			if (!guardaFichero(pathFicheroDesc, bloqueDatos))

				return false;

			if (!Ficheros.FicheroSoloLectura(pathFicheroDesc, true))

				return false;

			resultado = true;

//manejar los nulls

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, EnumMensajes.Excepcion, ex.getMessage(), "");

		}

		return resultado;

	}

	private boolean aplicaSoloLectura(boolean soloLectura) {

		boolean resultado;

		resultado = Ficheros.DirectorioSoloLectura(_PathDatos, soloLectura, true);

		if (!resultado) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.ErrorEnSoloLectura, null, null);

		}

		return resultado;

	}

	public void aniadeLibro(cLibro queLibro) {

		_NumeroLibros += 1;

		Libros.add(queLibro);

	}

	public boolean quitaLibro(int indiceLibro) {

		boolean quitado = false;

		if (indiceLibro < 0 || indiceLibro > _NumeroLibros - 1) {

			return quitado;

		}

		Libros.remove(indiceLibro);

		_NumeroLibros -= 1;

		quitado = true;

		return quitado;

	}

	public boolean quitaTodosLosLibros() {

		boolean quitados = false;

		for (int i = 0; i < _NumeroLibros; i++) {

			quitaLibro(i);

		}

		_NumeroLibros = 0;

		quitados = true;

		return quitados;

	}

	/**
	 * 
	 * Elimina de array de ficheros del libro de índice indice libro, el fichero de
	 * 
	 * índice indicefichero
	 * 
	 * 
	 * 
	 * @param indiceLibro
	 * 
	 * @param indiceFichero
	 * 
	 * @return
	 * 
	 */

	public boolean quitaFichero(int indiceLibro, int indiceFichero) {

		boolean quitado = false;

		if (indiceLibro < 0 || indiceLibro > _NumeroLibros - 1) {

			return quitado;

		}

		if (indiceFichero < 0 || indiceFichero > Libros.get(indiceLibro).getNumeroFicheros() - 1) {

			return quitado;

		}

		if (!Libros.get(indiceLibro).quitaFichero(indiceFichero)) {

			return quitado;

		}

		if (Libros.get(indiceLibro).getNumeroFicheros() == 0) {

			quitado = quitaLibro(indiceLibro);

		} else {

			quitado = true;

		}

		return quitado;

	}

	public boolean guardaFichero(String pathFichero, String bloqueDatos) {

		boolean resultado = false;

		BufferedWriter bufferedWriter = null;

		try {

			File file = new File(pathFichero);

			FileOutputStream fileOutputStream = new FileOutputStream(file);

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "ISO-8859-1");

			bufferedWriter = new BufferedWriter(outputStreamWriter);

			bufferedWriter.write(bloqueDatos);

			bufferedWriter.close();

			resultado = true;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, EnumMensajes.EscrituraFicheroFallida, pathFichero, null);

		} finally {

			try {

				if (bufferedWriter != null) {

					bufferedWriter.close();

				}

			} catch (Exception ex) {

			}

		}

		return resultado;

	}

	public boolean importaFichero(String pathFicheroOrigen, kLegalizacion.enumTipoLibro queTipoDeLibro,

			String descripcion, int queNumero, String fechaCierreUltimoLegalizado,

			kLegalizacion.enumExtensionFichero queExtensionFichero, String queFechaApertura, String queFechaCierre,

			boolean queEsEncriptado) {

		boolean importado = false;

		int indiceLibro = 0;

		String nombreFichero;

		String pathFicheroDestino = "";

		boolean esNuevoLibro;

		String nombreFicheroOrigen;

		boolean hayQueEncriptar = false;

		try {

			_UltimoFicheroSeHaEncriptado = false;

			if (!Files.exists(Paths.get(pathFicheroOrigen))) {

				return importado;

			}

			if (queTipoDeLibro == null) {// kLegalizacion.enumTipoLibro.NO_DEFINIDO

				return importado;

			}

			if (queExtensionFichero == null) {// kLegalizacion.enumExtensionFichero.NO_DEFINIDO

				return importado;

			}

			for (indiceLibro = 0; indiceLibro < _NumeroLibros; indiceLibro++) {

				if (Libros.get(indiceLibro).getTipoLibro() == queTipoDeLibro) {

					break;

				}

			}

			if (indiceLibro == _NumeroLibros) {

// Se añade un nuevo libro

				esNuevoLibro = true;

				if (queNumero <= 0) {

					return importado;

				}

				if (queNumero > 1 && Formato.ValorNulo(fechaCierreUltimoLegalizado)) {

					return importado;

				}

			} else {

// Libro ya existente

				esNuevoLibro = false;

				queNumero = Libros.get(indiceLibro).Ficheros[0].getNumero(); // Se calcula el Numero a partir del

				// Numero del primer fichero del libro

				queNumero += Libros.get(indiceLibro).getNumeroFicheros();

			}

			nombreFicheroOrigen = Paths.get(pathFicheroOrigen).getFileName().toString();

// Ver si hay que encriptar el fichero. Por si acaso se comprueba y si se recibe que hay que encriptar y ya es encriptado, no se hace nada

			hayQueEncriptar = false;

			if (queEsEncriptado) {

				int indiceExtension = kLegalizacion.dameIndiceDeExtensionFichero(queExtensionFichero);

				/*
				 * 
				 * if (!kLegalizacion.ExtensionesFicheros[indiceExtension].isEsDeEncriptacion())
				 * 
				 * { hayQueEncriptar = true; }
				 * 
				 */

			}

			if (hayQueEncriptar) {

				nombreFichero = dameNombreFichero(queTipoDeLibro, queNumero, queExtensionFichero, "");

				if (nombreFichero.isEmpty()) {

					return importado;

				}

				/*
				 * 
				 * if (!EncriptaFichero(pathFicheroOrigen, nombreFichero, pathFicheroDestino,
				 * 
				 * nombreFichero)) { return importado; }
				 * 
				 */

			} else {

				nombreFichero = dameNombreFichero(queTipoDeLibro, queNumero, queExtensionFichero, nombreFicheroOrigen);

				if (nombreFichero.isEmpty()) {

					return importado;

				}

				pathFicheroDestino = Paths.get(_PathDatos, nombreFichero).toString();

			}

			if (pathFicheroOrigen.equals(pathFicheroDestino)) {

				return importado;

			}

			if (!hayQueEncriptar) { // Si se ha encriptado el fichero destino ya se ha creado en la carpeta de la

				// legalización

// Antes de añadir el libro o fichero, se copia el fichero: por si da error en la copia

				if (Files.exists(Paths.get(pathFicheroDestino))) {

					File aux = new File(pathFicheroDestino);

					aux.delete();

				}

				if (hayQueCopiarFicheroConProgreso(pathFicheroOrigen)) {

					Files.copy(Paths.get(pathFicheroOrigen), Paths.get(pathFicheroDestino),

							StandardCopyOption.REPLACE_EXISTING);

				} else {

					Files.copy(Paths.get(pathFicheroOrigen), Paths.get(pathFicheroDestino));

				}

			}

			if (esNuevoLibro) {

				cLibro libro = new cLibro();

				libro.setTipoLibro(queTipoDeLibro);

				libro.setFechaCierreUltimoLegalizado(fechaCierreUltimoLegalizado);

				aniadeLibro(libro);

			}

			cFicheroLibro ficheroLibro = new cFicheroLibro();

			if (Formato.ValorNulo(descripcion)) {

				int indiceTipoLibro = kLegalizacion.dameIndiceDeTipoLibro(queTipoDeLibro);

				descripcion = kLegalizacion.Tiposlibros.get(indiceTipoLibro).getDescripcion();

			}

			ficheroLibro.setDescripcion(descripcion);

			ficheroLibro.setPathFichero(pathFicheroDestino);

			ficheroLibro.setNombreFichero(nombreFichero);

			ficheroLibro.setNumero(queNumero);

			ficheroLibro.setFechaApertura(queFechaApertura);

			ficheroLibro.setFechaCierre(queFechaCierre);

			Libros.get(indiceLibro).aniadeFichero(ficheroLibro);

			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return importado;

	}

	public boolean modificaFichero(int indiceLibro, int indiceFichero, String pathFicheroImportar,

			kLegalizacion.enumTipoLibro queTipoDeLibro, String descripcion, int queNumero,

			String fechaCierreUltimoLegalizado, kLegalizacion.enumExtensionFichero queExtensionFichero,

			String queFechaApertura, String queFechaCierre, boolean queEsEncriptado) {

		boolean modificado = false;

		final String kPrefijoAux = "_aux_";

		String pathFicheroDestino;

		StringBuilder nombreFichero = new StringBuilder();

		StringBuilder pathFicheroDestinoAuxiliar = new StringBuilder();

		boolean hayCambioDeFichero = false;

		boolean hayQueEncriptar = false;

		boolean hayQueDesencriptar = false;

		String extension;

		try {

			_UltimoFicheroSeHaEncriptado = false;

			if (indiceLibro >= _NumeroLibros) {

				return modificado;

			}

			if (indiceFichero >= Libros.get(indiceLibro).getNumeroFicheros()) {

				return modificado;

			}

			if (!Files.exists(Paths.get(pathFicheroImportar))) {

				return modificado;

			}

			if (queTipoDeLibro == null) {// kLegalizacion.enumTipoLibro.NO_DEFINIDO

				return modificado;

			}

			if (queExtensionFichero == null) {// kLegalizacion.enumExtensionFichero.NO_DEFINIDO

				return modificado;

			}

// No se permite cambiar el tipo de libro

			if (Libros.get(indiceLibro).getTipoLibro() != queTipoDeLibro) {

				return modificado;

			}

// Si se cambia el fichero, se crea el nuevo con un nombre auxiliar y luego se borra el inicial y se renombra el auxiliar

// Esto se hace antes que otras posibles operaciones de renombrar los fichero por cambio de número .....

// El cambio del tipo de extensión del fichero sólo se puede hacer eligiendo otro fichero

// Control si antes estaba encriptado/desencriptado y se cambia

			if (!pathFicheroImportar.equals(Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero())) {

				hayCambioDeFichero = true;

				if (queEsEncriptado) {

					int indiceExtension = kLegalizacion.dameIndiceDeExtensionFichero(queExtensionFichero);

					/*
					 * 
					 * if (!kLegalizacion.ExtensionesFicheros[indiceExtension].isEsDeEncriptacion())
					 * 
					 * { hayQueEncriptar = true; }
					 * 
					 */

				}

			} else {

// Comprobar si está encriptado/desencriptado y se cambia

				if (queEsEncriptado != Libros.get(indiceLibro).Ficheros[indiceFichero].isEsEncriptado()) {

					hayCambioDeFichero = true;

					hayQueEncriptar = queEsEncriptado;

					hayQueDesencriptar = !queEsEncriptado;

				}

			}

			if (hayCambioDeFichero) {

				String nombreFicheroOriginal = Paths.get(pathFicheroImportar).getFileName().toString();

				if (hayQueEncriptar) {

					String aux = dameNombreFichero(queTipoDeLibro,

							Libros.get(indiceLibro).Ficheros[indiceFichero].getNumero(), queExtensionFichero, "");

					if (aux.isEmpty()) {

						return modificado;

					}

					nombreFichero.append(kPrefijoAux + aux);

					/*
					 * 
					 * if (!EncriptaFichero(pathFicheroImportar, nombreFichero,
					 * 
					 * pathFicheroDestinoAuxiliar, nombreFichero)) { return modificado; }
					 * 
					 */

					nombreFicheroOriginal = nombreFichero.substring(kPrefijoAux.length());

				} else if (hayQueDesencriptar) {

					nombreFichero.setLength(0);

					if (!desEncriptaFichero(pathFicheroImportar, pathFicheroDestinoAuxiliar, kPrefijoAux,

							nombreFichero)) {

						return modificado;

					}

					nombreFicheroOriginal = nombreFichero.substring(kPrefijoAux.length());

					// extension = Paths.get(nombreFicheroOriginal).getExtension();

					/*
					 * 
					 * if (extension.startsWith(".")) { extension = extension.substring(1); }
					 * 
					 */

					// queExtensionFichero =

					// kLegalizacion.DameCodigoDeExtensionFicheroSegunExtension(extension);

					if (queExtensionFichero.ordinal() == 0) {

						return modificado; // En principio no puede ser ya que sólo se desencriptan los encriptado con

						// Legalia 2

					}

				} else { // Copia del fichero origen como auxiliar

					nombreFichero.append(dameNombreFichero(queTipoDeLibro,

							Libros.get(indiceLibro).Ficheros[indiceFichero].getNumero(), queExtensionFichero,

							nombreFicheroOriginal));

// Nota: si el numero de fichero ha cambiado, realmente el nombre se calcula sobre el número actual, luego se renombran si procede

					pathFicheroDestinoAuxiliar.append(Paths.get(_PathDatos, kPrefijoAux + nombreFichero).toString());

// Primero se copia el fichero origen, como fichero destino auxiliar. Si la copia se cancela o da error, el fichero actual todavía existe y con su nombre correcto

					if (hayQueCopiarFicheroConProgreso(pathFicheroImportar)) {

						Files.copy(Paths.get(pathFicheroImportar), Paths.get(pathFicheroDestinoAuxiliar.toString()),

								StandardCopyOption.REPLACE_EXISTING);

					} else {

						Files.copy(Paths.get(pathFicheroImportar), Paths.get(pathFicheroDestinoAuxiliar.toString()));

					}

				}

// Se borra el fichero actual

				if (Files.exists(Paths.get(Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero()))) {

					if (!Ficheros.FicheroBorra(Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero())) {

						Ficheros.FicheroBorra(pathFicheroDestinoAuxiliar.toString());

						throw new Exception(MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.ErrorAlBorrarFichero,

								Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero(), null, null));

					}

				}

// Hay que calcular de nuevo el nombre de fichero, siempre, no sólo al cambiar de extensión

// Ya que se podría sustituir un .ECB.AES128 por por ejemplo un sólo .AES128 y viceversa

				nombreFichero.setLength(0);

				nombreFichero.append(

						dameNombreFichero(queTipoDeLibro, Libros.get(indiceLibro).Ficheros[indiceFichero].getNumero(),

								queExtensionFichero, nombreFicheroOriginal));

				Libros.get(indiceLibro).Ficheros[indiceFichero].setNombreFichero(nombreFichero.toString());

				pathFicheroDestino = Paths.get(_PathDatos, nombreFichero.toString()).toString();

				Libros.get(indiceLibro).Ficheros[indiceFichero].setPathFichero(pathFicheroDestino);

// Control de que el fichero destino no exista ya en la legalización

// Este caso se puede dar si: encriptan un fichero de la legalización en el mismo directorio de la legalización

// Luego sustituyen el fichero de la legalización (no encriptado) por el encriptado

				if (Files.exists(Paths.get(pathFicheroDestino))) {

					if (!Ficheros.FicheroBorra(pathFicheroDestino)) {

						throw new Exception(MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.ErrorAlBorrarFichero,

								Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero(), null, null));

					}

				}

// Se renombra el fichero auxiliar como fichero actual. Muy improbable que ocurriese un error aquí

				Files.move(Paths.get(pathFicheroDestinoAuxiliar.toString()),

						Paths.get(_PathDatos, nombreFichero.toString()), StandardCopyOption.REPLACE_EXISTING);

				if (hayQueEncriptar || hayQueDesencriptar) {

// Se cambia el fichero de salida de la encriptación ya que se ha encriptado con un nombre auxiliar y luego renombrado

					MGeneral.Encriptacion.setFicheroSalida(pathFicheroDestino);

				}

			}

			if (indiceFichero == 0) {

				Libros.get(indiceLibro).setFechaCierreUltimoLegalizado(fechaCierreUltimoLegalizado);

			}

			if (indiceFichero == 0) {

// Si es el primer fichero y se cambia el número hay que renombrar los ficheros

// En el formulario sólo se deja cambiar el nº del primer fichero

				if (Libros.get(indiceLibro).Ficheros[indiceFichero].getNumero() != queNumero) {

					Libros.get(indiceLibro).Ficheros[indiceFichero].setNumero(queNumero);

					renombraFicherosLibro(indiceLibro);

					if (hayQueEncriptar || hayQueDesencriptar) {

// Si se había cambiado el número, se cambia el fichero de salida de la encriptación por el definitivo

						MGeneral.Encriptacion

								.setFicheroSalida(Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero());

					}

				}

			}

			if (Formato.ValorNulo(descripcion)) {

				int indiceTipoLibro = kLegalizacion.dameIndiceDeTipoLibro(queTipoDeLibro);

				// descripcion = kLegalizacion.Tiposlibros[indiceTipoLibro].getDescripcion();

			}

			Libros.get(indiceLibro).Ficheros[indiceFichero].setDescripcion(descripcion);

			Libros.get(indiceLibro).Ficheros[indiceFichero].setFechaApertura(queFechaApertura);

			Libros.get(indiceLibro).Ficheros[indiceFichero].setFechaCierre(queFechaCierre);

			return true;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		borrarArchivosTemporales(_PathDatos);

		return modificado;

	}

	public static void borrarArchivosTemporales(String directorio) {

		// Crear un objeto File que represente el directorio

		File dir = new File(directorio);

		// Verificar si el directorio existe

		if (dir.exists() && dir.isDirectory()) {

			// Obtener la lista de archivos en el directorio

			File[] archivos = dir.listFiles();

			// Iterar sobre los archivos y borrar los que comienzan con "tmp_"

			for (File archivo : archivos) {

				if (archivo.isFile() && archivo.getName().startsWith("tmp_")) {

					boolean exito = archivo.delete();

					if (exito) {

						System.out.println("Se ha borrado el archivo: " + archivo.getName());

					} else {

						System.out.println("No se pudo borrar el archivo: " + archivo.getName());

					}

				}

			}

		} else {

			System.out.println("El directorio no existe o no es válido.");

		}

	}

	public boolean desEncriptaFichero(String pathFicheroOrigen, StringBuilder pathFicheroSalida,

			String prefijoNombreFicheroSalida, StringBuilder nombreFicheroSalidaFinal) {

		try {

			boolean retorno = false;

			pathFicheroSalida.setLength(0);

			;

			nombreFicheroSalidaFinal.setLength(0);

			// La siguiente línea se sustituirá por tu lógica de compilación para LEGALIA2

			// Por el momento, simplemente retorna falso

			if (!K_COMPILACION.equals("LEGALIA2")) {

				return false;

			}

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Encriptacion.fxml"));

			Parent abrirEncriptacion = loader.load();

			Stage stage = new Stage();

			Scene scene = new Scene(abrirEncriptacion);

			// quitando el maximizar y minimizar

			stage.initModality(Modality.APPLICATION_MODAL);

			// bloquea la interacción con otras ventanas de la aplicación

			stage.initStyle(StageStyle.UTILITY);

			// quitando iconos

			stage.getIcons().clear();

			stage.setScene(scene);

			MGeneral.Idioma.cargarIdiomaControles(stage, null);

			stage.showAndWait();

			if (!MGeneral.RetornoFormulario) {

				return false;

			}

			pathFicheroSalida.append(MGeneral.Encriptacion.getFicheroSalida());

			nombreFicheroSalidaFinal.append(new File(pathFicheroSalida.toString()).getName());

			return true;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");

			return false;

		}

	}

	public int dameIndiceLibroDeTipoLibro(kLegalizacion.enumTipoLibro queTipoDeLibro) {

		for (int indiceLibro = 0; indiceLibro < _NumeroLibros; indiceLibro++) {

			if (Libros.get(indiceLibro).getTipoLibro() == queTipoDeLibro) {

				return indiceLibro;

			}

		}

		return -1;

	}

	private String dameNombreFichero(kLegalizacion.enumTipoLibro queTipoDeLibro, int queNumero,

			kLegalizacion.enumExtensionFichero queExtensionFichero, String nombreFicheroOriginal) {

		String nombreFichero = "";

		try {

			if (queTipoDeLibro == kLegalizacion.enumTipoLibro.OTROS) {

				// Se usa el número directamente si el tipo de libro es OTROS

				nombreFichero = Integer.toString(queNumero);

			} else {

				// Completar a ceros por la izquierda hasta una longitud de 3 caracteres

				nombreFichero = String.format("%03d", queNumero);

			}

			int indiceTipoLibro = kLegalizacion.dameIndiceDeTipoLibro(queTipoDeLibro);

			nombreFichero = kLegalizacion.Tiposlibros.get(indiceTipoLibro).getNombreFichero() + "_" + nombreFichero;

			boolean conservarExtensionesOriginales = false;

			if (!nombreFicheroOriginal.isEmpty()) {

				// Si la extensión del fichero es de encriptación, se mantienen las extensiones

				// originales

				// Obtener la extensión del archivo

				String extensionFichero = getFileExtension(nombreFicheroOriginal);

				if (extensionFichero.startsWith(".")) {

					extensionFichero = extensionFichero.substring(1);

				}

				int indiceExtension = kLegalizacion.dameIndiceDeExtensionFicheroSegunExtension(extensionFichero);

				if (indiceExtension > -1

						&& kLegalizacion.ExtensionesFicheros.get(indiceExtension).isEsDeEncriptacion()) {

					conservarExtensionesOriginales = true;

				}

			}

			if (conservarExtensionesOriginales) {

				nombreFichero += dameExtensionesOriginalesFichero(nombreFicheroOriginal);

			} else {

				int indiceExtension = kLegalizacion.dameIndiceDeExtensionFichero(queExtensionFichero);

				String extensionFichero = kLegalizacion.ExtensionesFicheros.get(indiceExtension).getExtension();

				nombreFichero += "." + extensionFichero;

			}

			return nombreFichero;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return nombreFichero;

	}

	private static String dameExtensionesOriginalesFichero(String nombreFicheroOriginal) {

		String cadResultado = "";

		try {

			if (nombreFicheroOriginal == null || nombreFicheroOriginal.isEmpty()) {

				return cadResultado;

			}

			// Se calcula la primera extensión del fichero: deberá ser una extensión de

			// algoritmo encriptación

			String nombreficheroactual = nombreFicheroOriginal;

			String extension = getFileExtension(nombreficheroactual).toUpperCase();

			if (extension.equals(""))

				return cadResultado;

			String extensionsinpunto = extension.substring(1);

			int indiceextensionalgoritmo = kLegalizacion.dameIndiceDeExtensionFicheroSegunExtension(extensionsinpunto);

			if (indiceextensionalgoritmo == -1)

				return cadResultado;

			// Control de si se trata de una extensión correspondiente a un formato de

			// encriptación

			if (!kLegalizacion.ExtensionesFicheros.get(indiceextensionalgoritmo).isEsDeEncriptacion())

				return cadResultado;

			cadResultado = extension;

			// Se calcula la segunda extensión del fichero: puede ser una extensión de modo

			// de encriptación, o de los formatos de ficheros admitidos

			nombreficheroactual = removeExtension(nombreficheroactual);

			extension = getFileExtension(nombreficheroactual).toUpperCase();

			if (extension.equals(""))

				return cadResultado;

			extensionsinpunto = extension.substring(1);

			// Si se trata de una extensión de un modo de encriptación se incluye en el

			// resultado y se obtiene la siguiente extensión

			if (kLegalizacion.extensionEsDeModoEncriptacion(extensionsinpunto)) {

				cadResultado = extension + cadResultado;

				nombreficheroactual = removeExtension(nombreficheroactual);

				extension = getFileExtension(nombreficheroactual).toUpperCase();

				if (extension.equals(""))

					return cadResultado;

				extensionsinpunto = extension.substring(1);

			}

			// Mirar si se trata de una extensión de uno de los formatos admitidos (sin

			// tener en cuenta los de encriptación)

			int indiceextensionfichero = kLegalizacion.dameIndiceDeExtensionFicheroSegunExtension(extensionsinpunto);

			if (indiceextensionfichero == -1)

				return cadResultado;

			// No se permite que sea una de encriptación

			if (kLegalizacion.ExtensionesFicheros.get(indiceextensionfichero).isEsDeEncriptacion())

				return cadResultado;

			cadResultado = extension + cadResultado;

			return cadResultado;

		} catch (Exception ex) {

			ex.printStackTrace();

			// ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion,

			// ex.getMessage());

			return cadResultado;

		}

	}

	private static String removeExtension(String fileName) {

		int dotIndex = fileName.lastIndexOf('.');

		if (dotIndex == -1 || dotIndex == 0 || dotIndex == fileName.length() - 1) {

			return fileName;

		} else {

			return fileName.substring(0, dotIndex);

		}

	}

	private static String getFileExtension(String filePath) {

		int dotIndex = filePath.lastIndexOf('.');

		if (dotIndex == -1 || dotIndex == 0 || dotIndex == filePath.length() - 1) {

			// Si no se encuentra un punto en la ruta del archivo o está al inicio o al

			// final del nombre,

			// o el nombre del archivo comienza o termina con un punto, no hay extensión

			// válida.

			return "";

		} else {

			// Devuelve la extensión del archivo

			return filePath.substring(dotIndex);

		}

	}

	private boolean renombraFicherosLibro(int queIndiceLibro) {

		boolean hayError = false;

		try {

			if (queIndiceLibro > (_NumeroLibros - 1)) {

				return false;

			}

			if (Libros.get(queIndiceLibro).getNumeroFicheros() == 0) {

				return true;

			}

			int numeroInicial = Libros.get(queIndiceLibro).Ficheros[0].getNumero();

			// Asignar el número definitivo y ver si hay que renombrar o no

			int primerIndiceRenombrar = -1;

			for (int j = 0; j < Libros.get(queIndiceLibro).getNumeroFicheros(); j++) {

				Libros.get(queIndiceLibro).Ficheros[j].setNumero(numeroInicial + j);

				String nombreFichero = dameNombreFichero(Libros.get(queIndiceLibro).getTipoLibro(),

						Libros.get(queIndiceLibro).Ficheros[j].getNumero(),

						Libros.get(queIndiceLibro).Ficheros[j].getTipoExtensionFichero(),

						Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero());

				if (!nombreFichero.isEmpty()) {

					if (!Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero().equals(nombreFichero)) {

						if (primerIndiceRenombrar == -1) {

							primerIndiceRenombrar = j;

						}

					}

				}

			}

			if (primerIndiceRenombrar == -1) {

				return true; // No hay que renombrar

			}

			// Primero se van a renombrar con un nombre auxiliar para que no haya problemas

			// de duplicidad de nombres

			for (int j = primerIndiceRenombrar; j < Libros.get(queIndiceLibro).getNumeroFicheros(); j++) {

				File aux = new File(Libros.get(queIndiceLibro).Ficheros[j].getPathFichero());

				if (aux.exists()) {

					String nombreFichero = dameNombreFichero(Libros.get(queIndiceLibro).getTipoLibro(),

							Libros.get(queIndiceLibro).Ficheros[j].getNumero(),

							Libros.get(queIndiceLibro).Ficheros[j].getTipoExtensionFichero(),

							Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero());

					if (!nombreFichero.isEmpty()) {

						String nombreTemporal = "tmp_" + Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero();

						try {

							File archivoOriginal = new File(Libros.get(queIndiceLibro).Ficheros[j].getPathFichero());

							File nuevoArchivo = null;

							// Creamos un objeto File con la ruta del nuevo nombre

							if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

								nuevoArchivo = new File(_PathDatos + "\\" + nombreTemporal);

							} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

								nuevoArchivo = new File(_PathDatos + "/" + nombreTemporal);

							}

							// Renombramos el archivo

							boolean exito = archivoOriginal.renameTo(nuevoArchivo);

							if (exito) {

								System.out.println("El archivo se ha renombrado correctamente.");

							} else {

								System.out.println("No se pudo renombrar el archivo.");

							}

						} catch (Exception ex) {

							// Si da alguna excepción nos salimos del bucle

							ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.RenombrarFicheroFallido,

									Libros.get(queIndiceLibro).Ficheros[j].getPathFichero(), nombreTemporal);

							ex.printStackTrace();

							hayError = true;

							break;

						}

						if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

							Libros.get(queIndiceLibro).Ficheros[j].setPathFichero(_PathDatos + "\\" + nombreTemporal);

						} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

							Libros.get(queIndiceLibro).Ficheros[j].setPathFichero(_PathDatos + "/" + nombreTemporal);

						}

						Libros.get(queIndiceLibro).Ficheros[j].setNombreFichero(nombreTemporal);

					}

				}

			}

			// Renombrar con el nombre definitivo

			if (!hayError) {

				for (int j = primerIndiceRenombrar; j < Libros.get(queIndiceLibro).getNumeroFicheros(); j++) {

					File file = new File(Libros.get(queIndiceLibro).Ficheros[j].getPathFichero());

					if (file.exists()) {

						String nombreFichero = dameNombreFichero(Libros.get(queIndiceLibro).getTipoLibro(),

								Libros.get(queIndiceLibro).Ficheros[j].getNumero(),

								Libros.get(queIndiceLibro).Ficheros[j].getTipoExtensionFichero(),

								Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero());

						if (!nombreFichero.isEmpty()) {

							if (!Libros.get(queIndiceLibro).Ficheros[j].getNombreFichero().substring(0, 4)

									.equals("tmp_")) {

								// El bucle anterior ha dado excepción al renombrar y se ha detenido el proceso

								hayError = true;

								break;

							}

							// No se produce excepción para que acabe el bucle pero se activa el error

							try {

								File a = new File(Libros.get(queIndiceLibro).Ficheros[j].getPathFichero());

								File b = null;

								// Creamos un objeto File con la ruta del nuevo nombre

								if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

									b = new File(_PathDatos + "\\" + nombreFichero);

								} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

									b = new File(_PathDatos + "/" + nombreFichero);

								}

								boolean exito = a.renameTo(b);

								if (exito) {

									System.out.println("El archivo se ha renombrado correctamente.");

								} else {

									System.out.println("No se pudo renombrar el archivo.");

								}

								// Renombramos el archivo

							} catch (Exception ex) {

								ErrorOcurrido.generaError(_MostrarMensajes,

										IdiomaC.EnumMensajes.RenombrarFicheroFallido,

										Libros.get(queIndiceLibro).Ficheros[j].getPathFichero(), nombreFichero);

								hayError = true;

							}

							if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

								Libros.get(queIndiceLibro).Ficheros[j]

										.setPathFichero(_PathDatos + "\\" + nombreFichero);

							} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

								Libros.get(queIndiceLibro).Ficheros[j].setPathFichero(_PathDatos + "/" + nombreFichero);

							}

							Libros.get(queIndiceLibro).Ficheros[j].setNombreFichero(nombreFichero);

						}

					}

				}

			}

			return !hayError;

		} catch (Exception ex) {

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return false;

	}

	public boolean hayQueCopiarFicheroConProgreso(String pathFicheroOrigen) {

		if (!MGeneral.ModoBatch && (_Modo == EnumModo.Normal || _Modo == EnumModo.Recepcion

				|| _Modo == EnumModo.SoloLectura || _Modo == EnumModo.SoloReenviar)) {

			File ficheroOrigen = new File(pathFicheroOrigen);

			if (ficheroOrigen.exists()

					&& ficheroOrigen.length() > MGeneral.Configuracion.getBytesFicheroParaIndicarProgreso()) {

				return true;

			}

		}

		return false;

	}

	// Aqui va encriptacion

	public kLegalizacion.enumResultadoValidacion valida() {

		boolean resultadoPrimarias = true;

		boolean resultadoSecundarias = true;

		inicializaValidaciones(); // Se ponen todos los arrays a null

		if (!validarEstructura()) {

			// Si no valida estructura, no se realizan el resto de validaciones ya que no se

			// cumplirían datos desc nombres los tres txt de tu carpeta

			resultadoPrimarias = false;

		} else {

			if (!validarPrimariasCuestionario()) {

				resultadoPrimarias = false;

			}

			if (validarCargaLibros()) {

				// Si no valida la carga no se realizan el resto de validaciones de

				// libros/ficheros

				if (!validarPrimariasLibros()) {

					resultadoPrimarias = false;

				}

				if (!validarPrimariasTodosLosLibros()) {

					resultadoPrimarias = false;

				}

				if (!validarPrimariasTodosLosFicheros()) {

					resultadoPrimarias = false;

				}

			} else {

				resultadoPrimarias = false;

			}

			if (!validarImplicitasCuestionario()) {

				resultadoSecundarias = false;

			}

		}

		// aqui

		traduceValidaciones();

		if (!resultadoPrimarias) {

			return kLegalizacion.enumResultadoValidacion.NoValida;

		} else if (!resultadoSecundarias) {

			return kLegalizacion.enumResultadoValidacion.ValidaConAvisos;

		} else {

			return kLegalizacion.enumResultadoValidacion.Valida;

		}

	}

	public boolean validarEstructura() {

		boolean estructuraValida = false;

		try {

			ValidacionesEstructuraNoCumple = null;

			if (!new File(getPathFicheroNombres()).exists() || !new File(getPathFicheroDatos()).exists()) {

				aniadeValidacionEstructuraNoCumplida(

						kLegalizacion.EnumValidacionesEstructura.FaltanFicherosObligatorios);

			}

			if (ValidacionesEstructuraNoCumple == null) {

				estructuraValida = true;

			}

		} catch (Exception ex) {

			aniadeValidacionEstructuraNoCumplida(kLegalizacion.EnumValidacionesEstructura.Excepcion);

			ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return estructuraValida;

	}

	private void aniadeValidacionEstructuraNoCumplida(kLegalizacion.EnumValidacionesEstructura validacionNoCumplida) {

		int numNoCumplidas = (ValidacionesEstructuraNoCumple != null) ? ValidacionesEstructuraNoCumple.length : 0;

		numNoCumplidas += 1;

		kLegalizacion.EnumValidacionesEstructura[] nuevasValidaciones = new kLegalizacion.EnumValidacionesEstructura[numNoCumplidas];

		if (ValidacionesEstructuraNoCumple != null) {

			System.arraycopy(ValidacionesEstructuraNoCumple, 0, nuevasValidaciones, 0,

					ValidacionesEstructuraNoCumple.length);

		}

		nuevasValidaciones[numNoCumplidas - 1] = validacionNoCumplida;

		ValidacionesEstructuraNoCumple = nuevasValidaciones;

	}

	private boolean validarPrimariasCuestionario() {

		boolean resultado = false;

		try {

			ValidacionesPrimariasCuestionarioNoCumple = null;

			if (_Modo == EnumModo.Dll) {

				// Solo para la DLL no se realiza validación primaria de cuestionario 1

				// (descripción Registro Mercantil obligatoria)

			} else {

				if (Formato.ValorNulo(Presentacion.getRegistroMercantilDestinoDescripcion())) {

					aniadeValidacionPrimariaCuestionarioNoCumplida(

							EnumValidacionesPrimariasCuestionario.RegistroMercantilFalta);

				}

			}

			if (Formato.ValorNulo(Presentacion.getNombreSociedadoEmpresario())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.NombredelEmpresarioFalta);

			}

			if (Formato.ValorNulo(Presentacion.getNifCif())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.NIFdelEmpresarioFalta);

			}

			if (Formato.ValorNulo(Presentacion.getDomicilio())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.DomiciliodelEmpresarioFalta);

			}

			if (Formato.ValorNulo(Presentacion.getCiudad())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.CiudaddelEmpresarioFalta);

			}

			if (Formato.ValorNulo(Presentacion.getProvinciaCodigo())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.ProvinciadelEmpresarioFalta);

			} else {

				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Provincias");

				if (!maestro.existeCodigo(Presentacion.getProvinciaCodigo())) {

					aniadeValidacionPrimariaCuestionarioNoCumplida(

							EnumValidacionesPrimariasCuestionario.ProvinciadelEmpresarioNovalida);

				}

			}

			if (Formato.ValorNulo(Presentacion.getFechaSolicitud())) {

				aniadeValidacionPrimariaCuestionarioNoCumplida(

						EnumValidacionesPrimariasCuestionario.FechadelaSolicitudFalta);

			} else {

				if (!comprobarFecha(Presentacion.getFechaSolicitud(), new LocalDate[1])) {

					aniadeValidacionPrimariaCuestionarioNoCumplida(

							EnumValidacionesPrimariasCuestionario.FechaSolicitudNoValida);

				}

			}

			if (Datos.getFormato() == enumFormato.Legalia2

					|| MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				if (_Modo == EnumModo.Dll) {

					// Solo para la DLL no se realiza validación primaria de cuestionario 100

					// (descripción el Registro Mercantil no válido)

				} else {

					MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Registros");

					if (!maestro.existeDescripcion(Presentacion.getRegistroMercantilDestinoDescripcion())) {

						aniadeValidacionPrimariaCuestionarioNoCumplida(

								EnumValidacionesPrimariasCuestionario.NombreRegistroMercantilInexistente);

					}

				}

				// Otros campos y reglas específicas de Legalia2 pueden ser agregados aquí

			}

			if (ValidacionesPrimariasCuestionarioNoCumple == null) {

				resultado = true;

			}

		} catch (Exception ex) {

			aniadeValidacionEstructuraNoCumplida(EnumValidacionesEstructura.Excepcion);

			ex.printStackTrace();

			ErrorOcurrido.generaError(isMostrarMensajes(), EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return resultado;

	}

	/*
	 * 
	 * private static String procesarFormatoFecha(String formato) { String
	 * 
	 * nuevoFormato = formato.replace("-", ""); return nuevoFormato; }
	 * 
	 */

	private void aniadeValidacionPrimariaCuestionarioNoCumplida(

			EnumValidacionesPrimariasCuestionario validacionNoCumplida) {

		int numNoCumplidas = (ValidacionesPrimariasCuestionarioNoCumple != null)

				? ValidacionesPrimariasCuestionarioNoCumple.length

				: 0;

		numNoCumplidas += 1;

		EnumValidacionesPrimariasCuestionario[] newArray = new EnumValidacionesPrimariasCuestionario[numNoCumplidas];

		if (ValidacionesPrimariasCuestionarioNoCumple != null) {

			System.arraycopy(ValidacionesPrimariasCuestionarioNoCumple, 0, newArray, 0, numNoCumplidas - 1);

		}

		newArray[numNoCumplidas - 1] = validacionNoCumplida;

		ValidacionesPrimariasCuestionarioNoCumple = newArray;

	}

	private boolean validarImplicitasCuestionario() {

		boolean validarImplicitasCuestionario = false;

		boolean codigoPostalValido;

		String codigoProvinciaCP;

		boolean esCif = false;

		try {

			ValidacionesImplicitasCuestionarioNoCumple = null;

			esCif = false;

			if (Datos.get_TipoPersona().equalsIgnoreCase(kLegalizacion.kTipoPersonaJuridica.toString())) {

				esCif = true;

			} else if (Formato.ValorNulo(Datos.get_TipoPersona())) {

				if (!Formato.ValorNulo(Presentacion.getNifCif())) {

					if (Formato.esCifDeEmpresa(Presentacion.getNifCif())) {

						esCif = true;

					}

				}

			}

			if (!esCif) {

				if (Formato.ValorNulo(Presentacion.getApellido1())) {

					aniadeValidacionImplicitaCuestionarioNoCumplida(

							kLegalizacion.EnumValidacionesImplicitasCuestionario.PrimerApellidodelEmpresarioFalta);

				}

				if (Formato.ValorNulo(Presentacion.getApellido2())) {

					aniadeValidacionImplicitaCuestionarioNoCumplida(

							kLegalizacion.EnumValidacionesImplicitasCuestionario.SegundoApellidodelEmpresarioFalta);

				}

			}

			if (Formato.ValorNulo(Presentacion.getDatosRegistralesTomo())) {

				aniadeValidacionImplicitaCuestionarioNoCumplida(

						kLegalizacion.EnumValidacionesImplicitasCuestionario.TomoDatosRegistralesFalta);

			}

			if (Formato.ValorNulo(Presentacion.getDatosRegistralesFolio())) {

				aniadeValidacionImplicitaCuestionarioNoCumplida(

						kLegalizacion.EnumValidacionesImplicitasCuestionario.FolioDatosRegistralesFalta);

			}

			if (Formato.ValorNulo(Presentacion.getDatosRegistralesHoja())) {

				aniadeValidacionImplicitaCuestionarioNoCumplida(

						kLegalizacion.EnumValidacionesImplicitasCuestionario.HojaRegistralDatosRegistralesFalta);

			}

			// Nif / Cif

			if (!Formato.ValorNulo(Presentacion.getNifCif())) {

				if (!Formato.validaCifNif(Presentacion.getNifCif())) {

					aniadeValidacionImplicitaCuestionarioNoCumplida(

							kLegalizacion.EnumValidacionesImplicitasCuestionario.NIFNoValido);

				}

			}

			// Código postal

			codigoPostalValido = true;

			if (Formato.ValorNulo(Presentacion.getCodigoPostal())) {

				codigoPostalValido = false;

			}

			if (codigoPostalValido && !Formato.isNumeric(Presentacion.getCodigoPostal())) {

				codigoPostalValido = false;

			}

			if (codigoPostalValido && Presentacion.getCodigoPostal().length() != 5) {

				codigoPostalValido = false;

			}

			if (codigoPostalValido) {

				codigoProvinciaCP = Presentacion.getCodigoPostal().substring(0, 2);

				if (!Formato.ValorNulo(Presentacion.getProvinciaCodigo())) {

					if (!codigoProvinciaCP.equals(Presentacion.getProvinciaCodigo())) {

						codigoPostalValido = false;

					}

				}

			}

			if (!codigoPostalValido) {

				aniadeValidacionImplicitaCuestionarioNoCumplida(

						kLegalizacion.EnumValidacionesImplicitasCuestionario.CodigoPostalNoValido);

			}

			// A partir de estas reglas aplican sólo a legalizaciones formato Legalia2

			// o hasta que se indique por configuración que ya no validan las versiones de

			// Legalia

			if (Datos.getFormato() == kLegalizacion.enumFormato.Legalia2

					|| MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				// v1.0.3 Datos presentante, se añaden pero quedan como no obligatorios.

				// Nif del presentante

				// if (!util.formato.valorNulo(presentacion.getPresentante().getNif())) {

				// if (!util.formato.validaNif(presentacion.getPresentante().getNif())) {

				// aniadeValidacionImplicitaCuestionarioNoCumplida(kLegalizacion.enumValidacionesImplicitasCuestionario.PresentanteNifNoValido);

				// }

				// }

				// Código postal del presentante

				// codigoPostalValido = true;

				// if (util.formato.valorNulo(presentacion.getPresentante().getCodigoPostal()))

				// {

				// codigoPostalValido = false;

				// }

				// if (codigoPostalValido &&

				// !util.formato.esNumero(presentacion.getPresentante().getCodigoPostal())) {

				// codigoPostalValido = false;

				// }

				// if (codigoPostalValido &&

				// presentacion.getPresentante().getCodigoPostal().length() != 5) {

				// codigoPostalValido = false;

				// }

				// if (codigoPostalValido) {

				// codigoProvinciaCP =

				// presentacion.getPresentante().getCodigoPostal().substring(0, 2);

				// if

				// (!util.formato.valorNulo(presentacion.getPresentante().getProvinciaCodigo()))

				// {

				// if

				// (!codigoProvinciaCP.equals(presentacion.getPresentante().getProvinciaCodigo()))

				// {

				// codigoPostalValido = false;

				// }

				// }

				// }

				// if (!codigoPostalValido) {

				// aniadeValidacionImplicitaCuestionarioNoCumplida(kLegalizacion.enumValidacionesImplicitasCuestionario.PresentanteCodigoPostalNoValido);

				// }

			}

			if (ValidacionesImplicitasCuestionarioNoCumple == null) {

				validarImplicitasCuestionario = true;

			}

		} catch (Exception ex) {

			aniadeValidacionEstructuraNoCumplida(kLegalizacion.EnumValidacionesEstructura.Excepcion);

			ex.printStackTrace();

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarImplicitasCuestionario;

	}

	private void aniadeValidacionImplicitaCuestionarioNoCumplida(

			kLegalizacion.EnumValidacionesImplicitasCuestionario validacionNoCumplida) {

		int numNoCumplidas = 0;

		if (ValidacionesImplicitasCuestionarioNoCumple != null) {

			numNoCumplidas = ValidacionesImplicitasCuestionarioNoCumple.length;

		}

		numNoCumplidas += 1;

		kLegalizacion.EnumValidacionesImplicitasCuestionario[] tempArray = new kLegalizacion.EnumValidacionesImplicitasCuestionario[numNoCumplidas];

		if (ValidacionesImplicitasCuestionarioNoCumple != null) {

			System.arraycopy(ValidacionesImplicitasCuestionarioNoCumple, 0, tempArray, 0,

					ValidacionesImplicitasCuestionarioNoCumple.length);

		}

		tempArray[numNoCumplidas - 1] = validacionNoCumplida;

		ValidacionesImplicitasCuestionarioNoCumple = tempArray;

	}

	public boolean validarCargaLibros() {

		BufferedReader fileDatos = null;

		BufferedReader fileNombres = null;

		_ValidaCargaDeLibros = false;

		try {

			if (!validarEstructura())

				return false;

			fileDatos = new BufferedReader(new FileReader(getPathFicheroDatos()));

			fileNombres = new BufferedReader(new FileReader(getPathFicheroNombres()));

			String linea;

			String codigocampo;

			String valorcampo;

			String lineanombres;

			int contadorFicheros = 0;

			String codigosubcampo;

			String valorsubcampo;

			String numerosecuencialficheroleido;

			String numerosecuencialficheroactual = "";

			kLegalizacion.enumTipoLibro tipolibroactual = null;

			kLegalizacion.enumTipoLibro tipolibroleido = null;

			String nombreficheroleido = "";

			cFicheroLibro ficheroLibro = null;

			boolean leerLibros = false;

			while ((linea = fileDatos.readLine()) != null) {

				linea = linea.trim(); // v1.1.8 quitar espacios en blanco

				if (linea.length() > 0) {

					codigocampo = linea.substring(0, 3);

					valorcampo = linea.substring(3);

					if (codigocampo.equals(kLegalizacion.kNumeroTotalLibrosPresentadosCodigoCampo)) { // 501

						leerLibros = true; // Hasta no leer el 501 no se activa la lectura de libros

					}

					if (leerLibros && codigocampo.compareTo(kLegalizacion.kPrimerFicheroCodigoCampo) >= 0

							&& codigocampo.compareTo(kLegalizacion.kUltimoFicheroCodigoCampo) <= 0) {

						codigosubcampo = valorcampo.substring(0, 2);

						valorsubcampo = valorcampo.substring(2);

						numerosecuencialficheroleido = codigocampo;

						if (!numerosecuencialficheroleido.equals(numerosecuencialficheroactual)) {

							// Se trata de un fichero nuevo

							if (!numerosecuencialficheroactual.isEmpty()) {

								// Comprobar los datos del fichero anterior

								if (ficheroLibro.getNumero() == 0) {

									aniadeValidacionPrimariaLibrosNoCumplida(

											kLegalizacion.EnumValidacionesPrimariasLibros.InsuficientesDatos);

									return false;

								}

							}

							numerosecuencialficheroactual = numerosecuencialficheroleido;

							contadorFicheros++;

							if (!numerosecuencialficheroleido.equals(String.format("%03d", contadorFicheros))) {

								// No se cumple la secuencia de ficheros

								aniadeValidacionPrimariaLibrosNoCumplida(

										kLegalizacion.EnumValidacionesPrimariasLibros.NoCoincideLaSecuencia);

								return false;

							}

							if (!codigosubcampo.equals(kLegalizacion.kDescripcionLibroCodigoSubcampo)) {

								// Cuando comienza un fichero se obliga a que exista el 01 con el tipo de libro

								aniadeValidacionPrimariaLibrosNoCumplida(

										kLegalizacion.EnumValidacionesPrimariasLibros.InsuficientesDatos);

								return false;

							}

							// Lectura del nombre del fichero correspondiente de NOMBRES.TXT

							nombreficheroleido = "";

							if ((lineanombres = fileNombres.readLine()) != null) {

								lineanombres = lineanombres.trim(); // v1.1.8 quitar espacios en blanco

								if (!Formato.ValorNulo(lineanombres)) {

									nombreficheroleido = lineanombres;

								}

							}

							if (nombreficheroleido.isEmpty()) {

								aniadeValidacionPrimariaLibrosNoCumplida(

										kLegalizacion.EnumValidacionesPrimariasLibros.ExistenLibrosDeTipoError);

								return false;

							}

							tipolibroleido = cLibro.obtenerTipoLibroSegunNombreFichero(nombreficheroleido);

							if (tipolibroleido == null) { // tipolibroleido puede ser null si no corresponde con ninguno

								// de los admitidos

								aniadeValidacionPrimariaLibrosNoCumplida(

										kLegalizacion.EnumValidacionesPrimariasLibros.TipoLibroInexistente);

								return false;

							}

							// Cada vez que cambia el nº secuencial (se lee un 01 se va a añadir un fichero)

							ficheroLibro = new cFicheroLibro();

							ficheroLibro.setNombreFichero(nombreficheroleido);

							if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

								ficheroLibro.setPathFichero(_PathDatos + "\\" + nombreficheroleido);

							} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

								ficheroLibro.setPathFichero(_PathDatos + "/" + nombreficheroleido);

							}

						}

						if (!tipolibroleido.equals(tipolibroactual)) {

							// Ha cambiado el tipo de libro

							tipolibroactual = tipolibroleido;

						} else {

							// Es del mismo tipo de libro

						}

						switch (codigosubcampo) {

						case kLegalizacion.kDescripcionLibroCodigoSubcampo:

							ficheroLibro.setDescripcion(valorsubcampo);

							break;

						case kLegalizacion.kNumeroLibroCodigoSubcampo:

							if (Formato.isNumeric(valorsubcampo)) {

								ficheroLibro.setNumero(Integer.parseInt(valorsubcampo));

							}

							break;

						case kLegalizacion.kFechaAperturaCodigoSubcampo:

							ficheroLibro.setFechaApertura(valorsubcampo);

							break;

						case kLegalizacion.kFechaCierreCodigoSubcampo:

							ficheroLibro.setFechaCierre(valorsubcampo);

							break;

						case kLegalizacion.kFechaCierreUltimoLegalizadoCodigoSubcampo:

							break; // No se realiza ninguna acción para este subcampo

						case kLegalizacion.kHuellaCodigoSubcampo:

							ficheroLibro.setHuella(valorsubcampo);

							break;

						}

					}

				}

			}

			if (!numerosecuencialficheroactual.isEmpty()) {

				if (ficheroLibro.getNumero() == 0) {

					aniadeValidacionPrimariaLibrosNoCumplida(

							kLegalizacion.EnumValidacionesPrimariasLibros.InsuficientesDatos);

					return false;

				}

			}

			// Si se ha acabado de leer Datos.Txt y todavía quedasen ficheros en Nombres.Txt

			int contquedan = 0;

			while ((lineanombres = fileNombres.readLine()) != null) {

				lineanombres = lineanombres.trim(); // v1.1.8 quitar espacios en blanco

				if (!Formato.ValorNulo(lineanombres)) {

					contquedan++;

					break;

				}

			}

			if (contquedan > 0) {

				aniadeValidacionPrimariaLibrosNoCumplida(

						kLegalizacion.EnumValidacionesPrimariasLibros.DiferenteNumeroLibros);

				return false;

			}

			_ValidaCargaDeLibros = true;

			return true;

		} catch (IOException e) {

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			e.printStackTrace();

			return false;

		} finally {

			try {

				if (fileDatos != null) {

					fileDatos.close();

				}

			} catch (IOException e) {

			}

			try {

				if (fileNombres != null) {

					fileNombres.close();

				}

			} catch (IOException e) {

			}

		}

	}

	private boolean validarPrimariasLibros() {

		boolean validarPrimariasLibros = false;

		int i, j;

		boolean salir = false;

		try {

			ValidacionesPrimariasLibrosNoCumple = null;

			if (_NumeroLibros == 0) {

				aniadeValidacionPrimariaLibrosNoCumplida(

						kLegalizacion.EnumValidacionesPrimariasLibros.NingunLibroParaLegalizar);

				return false; // No se sigue comprobando nada más

			}

			// ExistenLibrosDeTipoError()

			for (i = 0; i < _NumeroLibros; i++) {

				for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					if (Libros.get(i).Ficheros[j].getNombreFichero().isEmpty()) {

						aniadeValidacionPrimariaLibrosNoCumplida(

								kLegalizacion.EnumValidacionesPrimariasLibros.ExistenLibrosDeTipoError);

						salir = true;

						break;

					}

				}

				if (salir) {

					break;

				}

			}

			// A partir de estas reglas aplican sólo a legalizaciones formato Legalia2

			// o hasta que se indique por configuración que ya no validan las versiones de

			// Legalia

			if (Datos.getFormato() == kLegalizacion.enumFormato.Legalia2

					|| MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				// Libros de diferentes ejercicios

				LocalDate minfa = getMenorFechaApertura(-1, -1);

				LocalDate maxfc = getMayorFechaCierre(-1, -1);

				if (!minfa.isEqual(LocalDate.MAX) && !maxfc.isEqual(LocalDate.of(1, 1, 1))) {

					LocalDate fecha = minfa.plusYears(1);

					if (maxfc.isAfter(fecha)) {

						aniadeValidacionPrimariaLibrosNoCumplida(

								kLegalizacion.EnumValidacionesPrimariasLibros.ExistenEjerciciosDistintos);

					}

				}

			}

			if (ValidacionesPrimariasLibrosNoCumple == null) {

				validarPrimariasLibros = true;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarPrimariasLibros;

	}

	private void aniadeValidacionPrimariaLibrosNoCumplida(

			kLegalizacion.EnumValidacionesPrimariasLibros validacionNoCumplida) {

		int numNoCumplidas = 0;

		if (ValidacionesPrimariasLibrosNoCumple != null) {

			numNoCumplidas = ValidacionesPrimariasLibrosNoCumple.length;

		}

		numNoCumplidas += 1;

		kLegalizacion.EnumValidacionesPrimariasLibros[] tempArray = new kLegalizacion.EnumValidacionesPrimariasLibros[numNoCumplidas];

		if (ValidacionesPrimariasLibrosNoCumple != null) {

			System.arraycopy(ValidacionesPrimariasLibrosNoCumple, 0, tempArray, 0,

					ValidacionesPrimariasLibrosNoCumple.length);

		}

		tempArray[numNoCumplidas - 1] = validacionNoCumplida;

		ValidacionesPrimariasLibrosNoCumple = tempArray;

	}

	private boolean validarPrimariasTodosLosLibros() {

		boolean validarPrimariasTodosLosLibros = false;

		int i;

		boolean todosCorrectos = true;

		try {

			for (i = 0; i < _NumeroLibros; i++) {

				if (!validarPrimariasLibro(i)) {

					todosCorrectos = false;

				}

			}

			if (todosCorrectos) {

				validarPrimariasTodosLosLibros = true;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarPrimariasTodosLosLibros;

	}

	private boolean validarPrimariasLibro(int indiceLibro) {

		boolean validarPrimariasLibro = false;

		try {

			Libros.get(indiceLibro).ValidacionesPrimariasNoCumple = null;

			if (Libros.get(indiceLibro).Ficheros[0].getNumero() > 1) {

				if (Libros.get(indiceLibro).getFechaCierreUltimoLegalizado().isEmpty()) {

					aniadeValidacionPrimariaLibroNoCumplida(indiceLibro,

							kLegalizacion.EnumValidacionesPrimariasLibro.FechaCierreULtimoLegalizadoFalta);

				} else {

					// aqui

					if (!comprobarFecha(Libros.get(indiceLibro).getFechaCierreUltimoLegalizado(), new LocalDate[1])) {

						aniadeValidacionPrimariaLibroNoCumplida(indiceLibro,

								kLegalizacion.EnumValidacionesPrimariasLibro.FechaCierreULtimoLegalizadoNoValida);

					}

				}

			}

			if (Libros.get(indiceLibro).getNumeroFicheros() > 9) {

				aniadeValidacionPrimariaLibroNoCumplida(indiceLibro,

						kLegalizacion.EnumValidacionesPrimariasLibro.DemasiadosFicheros);

			}

			if (Libros.get(indiceLibro).getValidacionesPrimariasNoCumple() == null) {

				validarPrimariasLibro = true;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarPrimariasLibro;

	}

	private void aniadeValidacionPrimariaLibroNoCumplida(int indiceLibro,

			kLegalizacion.EnumValidacionesPrimariasLibro validacionNoCumplida) {

		int numNoCumplidas = 0;

		if (Libros.get(indiceLibro).ValidacionesPrimariasNoCumple != null) {

			numNoCumplidas = Libros.get(indiceLibro).ValidacionesPrimariasNoCumple.length;

		}

		numNoCumplidas += 1;

		kLegalizacion.EnumValidacionesPrimariasLibro[] tempArray = new kLegalizacion.EnumValidacionesPrimariasLibro[numNoCumplidas];

		if (Libros.get(indiceLibro).ValidacionesPrimariasNoCumple != null) {

			System.arraycopy(Libros.get(indiceLibro).ValidacionesPrimariasNoCumple, 0, tempArray, 0,

					Libros.get(indiceLibro).ValidacionesPrimariasNoCumple.length);

		}

		tempArray[numNoCumplidas - 1] = validacionNoCumplida;

		Libros.get(indiceLibro).ValidacionesPrimariasNoCumple = tempArray;

	}

	private boolean validarPrimariasTodosLosFicheros() {

		boolean validarPrimariasTodosLosFicheros = false;

		int i, j;

		boolean todosCorrectos = true;

		try {

			for (i = 0; i < _NumeroLibros; i++) {

				for (j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

					if (!validarPrimariasFichero(i, j)) {

						todosCorrectos = false;

					}

				}

			}

			if (todosCorrectos) {

				validarPrimariasTodosLosFicheros = true;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarPrimariasTodosLosFicheros;

	}

	private boolean validarPrimariasFichero(int indiceLibro, int indiceFichero) {

		boolean validarPrimariasFichero = false;

		LocalDate[] fecApertura = { null };

		LocalDate[] fecCierre = { null };

		LocalDate[] fecCierreUltimoLegalizado = { null };

		LocalDate[] fecCierreAnterior = { null };

		int a = 0;

		try {

			Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple = null;

			if (!comprobarFecha(Libros.get(indiceLibro).Ficheros[indiceFichero].getFechaApertura(), fecApertura)) {

				aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

						kLegalizacion.EnumValidacionesPrimariasFichero.FechaAperturaNoValida);

			}

			if (!comprobarFecha(Libros.get(indiceLibro).Ficheros[indiceFichero].getFechaCierre(), fecCierre)) {

				aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

						kLegalizacion.EnumValidacionesPrimariasFichero.FechaCierreNoValida);

			}

			if (Libros.get(indiceLibro).Ficheros[indiceFichero].getNumero() <= 0) {

				aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

						kLegalizacion.EnumValidacionesPrimariasFichero.InsuficientesDatos);

			}

			if (Libros.get(indiceLibro).Ficheros[indiceFichero].getNombreFichero().isEmpty()) {

				aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

						kLegalizacion.EnumValidacionesPrimariasFichero.EsDeTipoError);

			} else {

				if (!Files.exists(Paths.get(Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero()))) {

					aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

							kLegalizacion.EnumValidacionesPrimariasFichero.FicheroInexistente);

				}

				if (Libros.get(indiceLibro).Ficheros[indiceFichero].getTipoExtensionFichero().ordinal() == -1) {

					aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

							kLegalizacion.EnumValidacionesPrimariasFichero.ExtensionFicheroNoPermitida);

				}

				if (Libros.get(indiceLibro).Ficheros[indiceFichero]

						.getNumero() != Libros.get(indiceLibro).Ficheros[indiceFichero].getNumeroSegunNombre()) {

					aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

							kLegalizacion.EnumValidacionesPrimariasFichero.ExtensionFicheroNoPermitida);

				}

				switch (_Modo) {

				case Normal:

					// En modo normal no se verifican las huellas, se van a generar

					break;

				case Dll:

					// No se verifican las huellas

					break;

				case Recepcion:

					if (!Libros.get(indiceLibro).Ficheros[indiceFichero].getHuellaManual()

							.equals(Libros.get(indiceLibro).Ficheros[indiceFichero].getHuellaCalculada())) {

						aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

								kLegalizacion.EnumValidacionesPrimariasFichero.HuellaIncorrecta);

					}

					break;

				case SoloLectura:

				case SoloReenviar:

					// No se verifican las huellas

					break;

				}

			}

//aqui

			if (fecApertura[0] != null && fecCierre[0] != null) {

				if (!fecApertura[0].isEqual(LocalDate.of(1, 1, 1))) {

					if (!fecCierre[0].isEqual(LocalDate.of(1, 1, 1))) {

						// FechaAperturaMayorFechaCierre()

						if (fecApertura[0].isAfter(fecCierre[0])) {

							aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

									kLegalizacion.EnumValidacionesPrimariasFichero.FechaAperturaMayorFechaCierre);

						}

					}

					// FechaAperturaMenorFechaCierreUltimoLegalizado() //aqui

					if (comprobarFecha(Libros.get(indiceLibro).getFechaCierreUltimoLegalizado(),

							fecCierreUltimoLegalizado)) {

						if (fecApertura[0].isBefore(fecCierreUltimoLegalizado[0])) {

							aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

									kLegalizacion.EnumValidacionesPrimariasFichero.FechaAperturaMenorFechaCierreUltimoLegalizado);

						}

					}

				}

			}

			if (Datos.getFormato() == kLegalizacion.enumFormato.Legalia2

					|| MGeneral.Configuracion.isAplicarNuevasReglasAVersionLegalia()) {

				if (Libros.get(indiceLibro).comprobarSolapamientoFechasAperturaYCierre()) {

					if (!fecApertura[0].isEqual(LocalDate.of(1, 1, 1)) && indiceFichero > 0) {

						if (comprobarFecha(Libros.get(indiceLibro).Ficheros[indiceFichero - 1].getFechaCierre(),

								fecCierreAnterior)) {

							if (fecApertura[0].isBefore(fecCierreAnterior[0])) {

								aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

										kLegalizacion.EnumValidacionesPrimariasFichero.FechaAperturaMenorFechaCierreAnterior);

							}

						}

					}

				}

				for (int i = 0; i < _NumeroLibros; i++) {

					for (int j = 0; j < Libros.get(i).getNumeroFicheros(); j++) {

						if (i != indiceLibro || j != indiceFichero) {

							if (Libros.get(i).getTipoLibro() == Libros.get(indiceLibro).getTipoLibro()) {

								if (Libros.get(i).Ficheros[j]

										.getNumero() == Libros.get(indiceFichero).Ficheros[indiceFichero].getNumero()) {

									aniadeValidacionPrimariaFicheroNoCumplida(indiceLibro, indiceFichero,

											kLegalizacion.EnumValidacionesPrimariasFichero.TipoLibroNumeroRepetido);

								}

							}

						}

					}

				}

			}

			if (Libros.get(indiceLibro).Ficheros[indiceFichero].getValidacionesPrimariasNoCumple() == null) {

				validarPrimariasFichero = true;

			}

		} catch (Exception ex) {

			// ex.printStackTrace();

			aniadeValidacionPrimariaLibrosNoCumplida(

					kLegalizacion.EnumValidacionesPrimariasLibros.ExcepcionEnLaCargaLibros);

			ErrorOcurrido.generaError(isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

		return validarPrimariasFichero;

	}

	private void aniadeValidacionPrimariaFicheroNoCumplida(int indiceLibro, int indiceFichero,

			kLegalizacion.EnumValidacionesPrimariasFichero validacionNoCumplida) {

		int numNoCumplidas = 0;

		if (Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple != null) {

			numNoCumplidas = Libros.get(indiceFichero).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple.length;

		}

		numNoCumplidas += 1;

		kLegalizacion.EnumValidacionesPrimariasFichero[] newArray = new kLegalizacion.EnumValidacionesPrimariasFichero[numNoCumplidas];

		if (Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple != null) {

			System.arraycopy(Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple, 0, newArray,

					0, Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple.length);

		}

		newArray[numNoCumplidas - 1] = validacionNoCumplida;

		Libros.get(indiceLibro).Ficheros[indiceFichero].ValidacionesPrimariasNoCumple = newArray;

	}

	private void traduceValidaciones() {

		try {

			StringBuilder mensaje = new StringBuilder();

			StringBuilder campofoco = new StringBuilder();

			StringBuilder codigoretorno = new StringBuilder();

			String cad = "";

			int[] contadormensajes = { 0 };

			int i, j, k, m;

			MensajesReglas = null;

			if (!isValidaEstructura()) {

				for (i = 0; i < ValidacionesEstructuraNoCumple.length; i++) {

					MGeneral.Idioma.obtenerDatosMensajeReglas(ObjetosIdioma.TipoMensajesReglas.Estructura.name(),

							ValidacionesEstructuraNoCumple[i].getValue(), mensaje, campofoco, codigoretorno);

					aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsError, campofoco, mensaje, codigoretorno,

							MensajesReglasC.Origen.Estructura);

				}

			}

			if (!isValidaPrimariasCuestionario()) {

				for (i = 0; i < ValidacionesPrimariasCuestionarioNoCumple.length; i++) {

					MGeneral.Idioma.obtenerDatosMensajeReglas(

							ObjetosIdioma.TipoMensajesReglas.PrimariasCuestionario.name(),

							ValidacionesPrimariasCuestionarioNoCumple[i].getValue(), mensaje, campofoco, codigoretorno);

					aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsError, campofoco, mensaje, codigoretorno,

							MensajesReglasC.Origen.PrimariasCuestionario);

				}

			}

			if (!isValidaPrimariasLibros()) {

				for (i = 0; i < ValidacionesPrimariasLibrosNoCumple.length; i++) {

					MGeneral.Idioma.obtenerDatosMensajeReglas(ObjetosIdioma.TipoMensajesReglas.PrimariasLibros.name(),

							ValidacionesPrimariasLibrosNoCumple[i].getValue(), mensaje, campofoco, codigoretorno);

					aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsError, campofoco, mensaje, codigoretorno,

							MensajesReglasC.Origen.PrimariasLibros);

				}

			}

			for (i = 0; i < _NumeroLibros; i++) {

				if (!Libros.get(i).validaPrimarias()) {

					for (j = 0; j < Libros.get(i).ValidacionesPrimariasNoCumple.length; j++) {

						StringBuilder aux = new StringBuilder();

						MGeneral.Idioma.obtenerDatosMensajeReglas(

								ObjetosIdioma.TipoMensajesReglas.PrimariasLibro.name(),

								Libros.get(i).ValidacionesPrimariasNoCumple[j].getValue(), mensaje, campofoco,

								codigoretorno);

						int x = kLegalizacion.dameIndiceDeTipoLibro(Libros.get(i).getTipoLibro());

						aux.append(

								String.format(mensaje.toString(), kLegalizacion.Tiposlibros.get(x).getDescripcion()));

						mensaje = aux;

						aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsError, campofoco, mensaje,

								codigoretorno, MensajesReglasC.Origen.PrimariasLibros);

					}

				}

				for (k = 0; k < Libros.get(i).getNumeroFicheros(); k++) {

					if (!Libros.get(i).Ficheros[k].isValidaPrimarias()) {

						for (m = 0; m < Libros.get(i).Ficheros[k].ValidacionesPrimariasNoCumple.length; m++) {

							StringBuilder aux = new StringBuilder();

							MGeneral.Idioma.obtenerDatosMensajeReglas(

									ObjetosIdioma.TipoMensajesReglas.PrimariasFichero.name(),

									Libros.get(i).Ficheros[k].ValidacionesPrimariasNoCumple[m].getValue(), mensaje,

									campofoco, codigoretorno);

							int x = kLegalizacion.dameIndiceDeTipoLibro(Libros.get(i).getTipoLibro());

							switch (Libros.get(i).Ficheros[k].ValidacionesPrimariasNoCumple[m]) {

							case FicheroInexistente:

								aux.append(String.format(mensaje.toString(),

										kLegalizacion.Tiposlibros.get(x).getDescripcion(),

										Libros.get(i).Ficheros[k].getNumero(),

										Libros.get(i).Ficheros[k].getPathFichero()));

								mensaje = aux;

								break;

							case FechaAperturaMenorFechaCierreAnterior:

								if (k > 0) {

									aux.append(String.format(mensaje.toString(),

											kLegalizacion.Tiposlibros.get(x).getDescripcion(),

											Libros.get(i).Ficheros[k].getNumero(),

											Libros.get(i).Ficheros[k - 1].getNumero()));

									mensaje = aux;

								} else {

									aux.append(String.format(mensaje.toString(),

											kLegalizacion.Tiposlibros.get(x).getDescripcion(),

											Libros.get(i).Ficheros[k].getNumero()));

									mensaje = aux;

								}

								break;

							default:

								aux.append(String.format(mensaje.toString(),

										kLegalizacion.Tiposlibros.get(x).getDescripcion(),

										Libros.get(i).Ficheros[k].getNumero()));

								mensaje = aux;

								break;

							}

//Error o aviso, crear mas cases

							aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsError, campofoco, mensaje,

									codigoretorno, MensajesReglasC.Origen.PrimariasLibros);

						}

					}

				}

			}

			if (!isValidaImplicitasCuestionario()) {

				for (i = 0; i < ValidacionesImplicitasCuestionarioNoCumple.length; i++) {

					MGeneral.Idioma.obtenerDatosMensajeReglas(

							ObjetosIdioma.TipoMensajesReglas.ImplicitasCuestionario.name(),

							ValidacionesImplicitasCuestionarioNoCumple[i].getValue(), mensaje, campofoco,

							codigoretorno);

					aniadeMensaje(contadormensajes, MensajesReglasC.Grado.EsAviso, campofoco, mensaje, codigoretorno,

							MensajesReglasC.Origen.ImplicitasCuestionario);

				}

			}

		} catch (Exception ex) {

			// ex.printStackTrace();

			ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), null);

		}

	}

	private void aniadeMensaje(int[] contadorMensajes, MensajesReglasC.Grado grado, StringBuilder campofoco,

			StringBuilder mensaje, StringBuilder codigoretorno, MensajesReglasC.Origen origen) {

		contadorMensajes[0] += 1;

		if (MensajesReglas == null) {

			MensajesReglas = new MensajesReglasC[contadorMensajes[0]];

		} else {

			MensajesReglasC[] newArray = new MensajesReglasC[contadorMensajes[0]];

			System.arraycopy(MensajesReglas, 0, newArray, 0, contadorMensajes[0] - 1);

			MensajesReglas = newArray;

		}

		MensajesReglas[contadorMensajes[0] - 1] = new MensajesReglasC();

		MensajesReglas[contadorMensajes[0] - 1].setGrado(grado);

		MensajesReglas[contadorMensajes[0] - 1].setTextoMensaje(mensaje.toString());

		MensajesReglas[contadorMensajes[0] - 1].setCampoFoco(campofoco.toString());

		MensajesReglas[contadorMensajes[0] - 1].setCodigoRetorno(codigoretorno.toString());

		MensajesReglas[contadorMensajes[0] - 1].setOrigen(origen);

	}

	public static boolean comprobarFecha(String cadenaFecha, LocalDate[] fechaResultante) {

		boolean result = false;

		LocalDate fecha;

		if (Formato.ValorNulo(cadenaFecha)) {

			return false;

		}

		List<DateTimeFormatter> formateadores = Arrays.asList(DateTimeFormatter.ofPattern("ddMMyyyy"));

		for (DateTimeFormatter formatter : formateadores) {

			try {

				fecha = LocalDate.parse(cadenaFecha, formatter);

				// Si no se lanza ninguna excepción, se encontró el formato adecuado

				// fechaResultante=new LocalDate[1];

				result = true;

				fechaResultante[0] = fecha;

				break;

			} catch (Exception e) {

				fechaResultante = new LocalDate[1];

				result = false;

				continue;

			}

		}

		return result;

	}

	public static LocalDate dameFecha(String cadenaFecha) {

		LocalDate fecha = null;

		if (Formato.ValorNulo(cadenaFecha)) {

			return fecha;

		}

		/*
		 * 
		 * if (cadenaFecha.trim().length() != 8 || !isNumeric(cadenaFecha)) { return
		 * 
		 * fecha; }
		 * 
		 */

		List<DateTimeFormatter> formateadores = Arrays.asList(DateTimeFormatter.ofPattern("ddMMyyyy"),

				DateTimeFormatter.ofPattern("dd-MM-yyyy"), DateTimeFormatter.ofPattern("dd/MM/yyyy"),

				DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		for (DateTimeFormatter formatter : formateadores) {

			try {

				fecha = LocalDate.parse(cadenaFecha, formatter);

				// Si no se lanza ninguna excepción, se encontró el formato adecuado

				break;

			} catch (Exception e) {

				// Si ocurre una excepción, intenta con el siguiente formato

				continue;

			}

		}

		return fecha;

		/*
		 * 
		 * int dia = Integer.parseInt(cadenaFecha.substring(0, 2)); int mes =
		 * 
		 * Integer.parseInt(cadenaFecha.substring(2, 4)); int anio =
		 * 
		 * Integer.parseInt(cadenaFecha.substring(4, 8));
		 * 
		 * 
		 * 
		 * // v1.2.7 para evitar años metidos con 2 ceros iniciales: metían 31120015 if
		 * 
		 * (Integer.toString(anio).length() != 4) { return fecha; }
		 * 
		 * 
		 * 
		 * try { fecha = new
		 * 
		 * SimpleDateFormat("dd/MM/yyyy").parse(String.format("%02d/%02d/%04d", dia,
		 * 
		 * mes, anio)); } catch (ParseException e) { // Manejo de excepción, puedes
		 * 
		 * imprimir el mensaje de error o hacer algo más // según tu lógica }
		 * 
		 * 
		 * 
		 * return fecha;
		 * 
		 */

	}

	public static String dameCadenaFecha(Date fecha) {

		if (Formato.ValorNulo(fecha)) {

			return "";

		}

		String dia = String.format("%02d", fecha.getDate());

		String mes = String.format("%02d", fecha.getMonth() + 1);

		String anio = Integer.toString(fecha.getYear() + 1900);

		return dia + mes + anio;

	}

	private static boolean isNumeric(String str) {

		return str.matches("-?\\d+(\\.\\d+)?");

	}

	public static boolean ficheroEncriptadoConLegalia(String PathFicheroEntrada) {

		cEncriptacion objencrip = new cEncriptacion();

		try {

			StringBuilder nombrefichero = new StringBuilder();

			if (!objencrip.obtenerDatosEncriptacionSegunNombreFichero(PathFicheroEntrada, nombrefichero)) {

				return false;

			} else {

				// Solo si el modo de cifrado de bloques es el modo por defecto (ECB)

				if (objencrip.getModo().getCodigo() != objencrip.getModo().getModoPorDefecto()) {

					return false;

				}

			}

			String extensionfichero = new File(nombrefichero.toString()).getName();

			int puntoIndex = extensionfichero.lastIndexOf('.');

			if (puntoIndex >= 0) {

				extensionfichero = extensionfichero.substring(puntoIndex + 1);

			}

			kLegalizacion.enumExtensionFichero tipoextension = kLegalizacion

					.dameCodigoDeExtensionFicheroSegunExtension(extensionfichero);

			if (tipoextension == null) {

				return false;

			}

			return true;

		} catch (Exception ex) {

			// ErrorOcurrido.generaError(_MostrarMensajes, IdiomaC.EnumMensajes.Excepcion,

			// ex.getMessage());

			return false;

		}

	}

	public boolean verLibroFichero(int indiceLibro, int indiceFichero) {

		// Check compilation flag (assuming a boolean variable isCOMPILACION)

		// if (K_COMPILACION.equals("LEGALIA2")) {

		String fichero;

		try {

			// Assuming Libros is an array or list of Libro objects

			fichero = Libros.get(indiceLibro).Ficheros[indiceFichero].getPathFichero();

			// Check for encryption

			if (Libros.get(indiceLibro).Ficheros[indiceFichero].isEsEncriptado()) {

				Stage stage;

				Scene scene;

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Encriptacion.fxml"));

				Parent abrirEncriptacion = loader.load();

				stage = new Stage();

				scene = new Scene(abrirEncriptacion);

				// quitando el maximizar y minimizar

				stage.initModality(Modality.APPLICATION_MODAL);

				// bloquea la interacción con otras ventanas de la aplicación

				stage.initStyle(StageStyle.UTILITY);

				// quitando iconos

				stage.getIcons().clear();

				stage.setScene(scene);

				MGeneral.Idioma.cargarIdiomaControles(stage, null);

				stage.showAndWait();

				fichero = cEncriptacion.getFicheroSalida();

			}

			// Check file existence

			if (!new File(fichero).exists()) {

				// MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente,

				// fichero,"","");

				return false;

			}

			// Open the file

			Utilidades.ProcessStartFichero(fichero);

			return true;

		} catch (Exception ex) {

			// Handle exception

			System.err.println("Error: " + ex.getMessage());

			return false;

		}

		// } else {

		// return false;

		// }

	}

	public boolean generaInstancia() {

		boolean resultado = false;

		try {

			kLegalizacion.enumResultadoValidacion resultadoValida;

			/*
			 * 
			 * if (vProgreso != null) { vProgreso.siguienteProceso(1, ""); }
			 * 
			 */

			Ficheros.FicheroBorra(getPathFicheroInstancia());

			resultadoValida = valida();

			if (resultadoValida == kLegalizacion.enumResultadoValidacion.NoValida) {

				return false;

			}

			if (!generarHuellas()) {

				return false;

			}

			if (K_COMPILACION.equals("LEGALIA2")) {

				/*
				 * 
				 * if (!MInstalacion.estaFuenteCodigoBarrasInstalada()) {
				 * 
				 * ErrorOcurrido.generaError(_MostrarMensajes,
				 * 
				 * IdiomaC.enumMensajes.FuenteCodigoBarrarDesinstalada); return false; }
				 * 
				 */

				/*
				 * 
				 * if (vProgreso != null) { vProgreso.mostrarProgreso(1); }
				 * 
				 */

				resultado = true;

			} else {

				resultado = false;

			}

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			/*
			 * 
			 * if (vProgreso != null) { vProgreso.procesoTerminado(); }
			 * 
			 */

		}

		return resultado;

	}

}
