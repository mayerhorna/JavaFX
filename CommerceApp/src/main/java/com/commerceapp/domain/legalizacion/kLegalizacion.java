package com.commerceapp.domain.legalizacion;

import java.util.List;
import java.util.logging.Logger;

import com.commerceapp.controller.EspecificarLibrosController;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.maestros.FormatoFichero;
import com.commerceapp.maestros.MaestroModoEncriptacion;
import com.commerceapp.maestros.TipoLibro;

public class kLegalizacion {
	private static final Logger logger = Logger.getLogger(kLegalizacion.class.getName());
	// Códigos de campo del fichero DATOS.TXT
	public static final String kRegistroMercantilDestinoDescripcionCodigoCampo = "100";
	public static final String kFechaSolicitudCodigoCampo = "101";
	public static final String kNombreSociedadoEmpresarioCodigoCampo = "102";
	public static final String kApellido1CodigoCampo = "103";
	public static final String kApellido2CodigoCampo = "104";
	public static final String kNifCifCodigoCampo = "105";
	public static final String kDomicilioCodigoCampo = "106";
	public static final String kCiudadCodigoCampo = "107";
	public static final String kCodigoPostalCodigoCampo = "108";
	public static final String kProvinciaCodigoCodigoCampo = "109";
	public static final String kFaxCodigoCampo = "110";
	public static final String kTelefonoCodigoCampo = "111";
	public static final String kRegistroMercantilDestinoCodigoCodigoCampo = "112";

	public static final String kDatosRegistralesTomoCodigoCampo = "201";
	public static final String kDatosRegistralesLibroCodigoCampo = "202";
	public static final String kDatosRegistralesFolioCodigoCampo = "204";
	public static final String kTipoRegistroPublicoCodigoCampo = "205";
	public static final String kDatosRegistralesHojaCodigoCampo = "206";
	public static final String kDatosRegistralesOtrosCodigoCampo = "207";

	public static final String kPresentanteNombreCodigoCampo = "301";
	public static final String kPresentanteApellido1CodigoCampo = "302";
	public static final String kPresentanteApellido2CodigoCampo = "303";
	public static final String kPresentanteNifCodigoCampo = "304";
	public static final String kPresentanteDomicilioCodigoCampo = "305";
	public static final String kPresentanteCiudadCodigoCampo = "306";
	public static final String kPresentanteCodigoPostalCodigoCampo = "307";
	public static final String kPresentanteProvinciaCodigoCodigoCampo = "308";
	public static final String kPresentanteFaxCodigoCampo = "309";
	public static final String kPresentanteTelefonoCodigoCampo = "310";
	public static final String kPresentanteEmailCodigoCampo = "311";
	public static final String kPresentanteSolicitaRetencionCodigoCampo = "401";

	public static final String kNumeroTotalLibrosPresentadosCodigoCampo = "501";
	public static final String kPrimerFicheroCodigoCampo = "001";
	public static final String kUltimoFicheroCodigoCampo = "135";

	public static final String kDescripcionLibroCodigoSubcampo = "01";
	public static final String kNumeroLibroCodigoSubcampo = "02";
	public static final String kFechaAperturaCodigoSubcampo = "03";
	public static final String kFechaCierreCodigoSubcampo = "04";
	public static final String kFechaCierreUltimoLegalizadoCodigoSubcampo = "05";
	public static final String kHuellaCodigoSubcampo = "06";

	// Nombres de los ficheros de texto con los diferentes datos de la legalización
	public static final String kNombreFicheroDatos = "DATOS.TXT";
	public static final String kNombreFicheroNombres = "NOMBRES.TXT";
	public static final String kNombreFicheroDesc = "DESC.TXT";

	// Códigos de campo del fichero DESC.TXT
	public static final String kCodigoDatoEjercicio = "Ejercicio";
	public static final String kCodigoDatoFormato = "Formato";
	public static final String kCodigoDatoEnviado = "Enviado";
	public static final String kCodigoDatoNombreZip = "NombreZip";
	public static final String kCodigoDatoTipoPersona = "TipoPersona";
	public static final String kVersionLegalia2Generacion = "VersionLegalia2";
	public static final String kCodigoDatoeDocNumeroDocumento = "eDocNumeroDocumento";
	public static final String kCodigoDatoeDocEntradaTipo = "eDocEntradaTipo";
	public static final String kCodigoDatoeDocEntradaSubsanada = "eDocEntradaSubsanada";
	public static final String kCodigoDatoeDocIdTramite = "eDocIdTramite";
	public static final String kCodigoDatoeDocNombreFicheroEnviado = "eDocNombreFicheroEnviado";
	public static final String kCodigoDatoeDocNombreFicheroAcuseEntrada = "eDocNombreFicheroAcuseEntrada";
	public static final String kCodigoDatoeDocNombreFicheroNE = "eDocNombreFicheroNE";

	public static final String kCodigoDatoEnvioReintentable = "EnvioReintentable";
	public static final String kCodigoDatoCodAccesoNif = "CodAccesoNif";
	public static final String kCodigoDatoPresentanteNombreConfirmado = "PresentanteNombreConfirmado";
	public static final String kCodigoDatoPresentanteApellidosConfirmados = "PresentanteApellidosConfirmados";
	public static final String kCodigoDatoPresentanteCorreoElectronicoConfirmado = "PresentanteCorreoElectronicoConfirmado";

	// Nombre del fichero de la instancia
	public static final String kNombreFicheroInstancia = "Instancia.Pdf";

	// Constantes relacionadas con el navegador y parámetros para el envío del ZIP
	// por el portal
	public static final String kNavegadorPortal = "iexplore";
	public static final String kVariablesUrlPortal = "?tipoOperacion=112&ficheroZip=";

	// Longitud de las huellas
	public static final int kLongitudHuellaMD5 = 26; // Utilizada en versiones anteriores Legalia
	public static final int kLongitudHuellaSHA256 = 44; // Utilizada en Legalia2

	public static List<TipoLibro> Tiposlibros;
	public static List<FormatoFichero> ExtensionesFicheros;
	public static List<MaestroModoEncriptacion> ModosEncriptacion;

	// Tipo de persona Física Jurídica
	public static final String kTipoPersonaFisica = "F";
	public static final String kTipoPersonaJuridica = "J";
	public static final String KTipoPersonaDefecto = kTipoPersonaJuridica;

	// Directorio de los Adjuntos Pdf
	public static final String kDirectorioAdjuntos = "Adjuntos";

	public enum enumFormato {
		Legalia, // Primer formato, anterior a Legalia
		Legalia2 // Formato 2, realizado con Legalia2
	}

	public enum enumExtensionFichero {
		XLS, XLSX, DOC, DOCX, WQ1, WK1, RTF, PDF, ODX, ODT, TIF, TIFF, AES128, AES192, AES256, TripleDES
	}

	public enum enumTipoLibro {
		DIARIO, INV_CUEN, BAL_SUMS, INVENTAR, BALANCES, MEMORIA, MAYOR, PER_GAN, IVA, FAC_EMIT, FAC_RECI, DET_DIA,
		ACCIONES, SOCIOS, OTROS, ACTASCON, ACTASDET, SOCUNICO, ACTALCON, ACTACODE
	}

	public enum enumResultadoValidacion {
		Valida, NoValida, ValidaConAvisos
	}

	public enum EnumValidacionesEstructura {
		FaltanFicherosObligatorios(1), // Falta alguno de los ficheros obligatorios Nombres.Txt o Datos.Txt
		Excepcion(2); // Se ha producido una excepción

		private final int value;

		EnumValidacionesEstructura(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumValidacionesPrimariasCuestionario {
		RegistroMercantilFalta(1), NombredelEmpresarioFalta(2), NIFdelEmpresarioFalta(3),
		DomiciliodelEmpresarioFalta(4), CiudaddelEmpresarioFalta(5), ProvinciadelEmpresarioFalta(6),
		FechadelaSolicitudFalta(7), TipodeRegistroPublicoFalta(8), FechaSolicitudNoValida(9),
		ProvinciadelEmpresarioNovalida(10), NombreRegistroMercantilInexistente(100); // Se harán coincidir los nombres
																						// de los Registros Mercantiles
																						// de LEGALIA2 con los del
																						// portal de tramitación
																						// telemática CORPME

		private final int value;

		EnumValidacionesPrimariasCuestionario(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumValidacionesImplicitasCuestionario {
		PrimerApellidodelEmpresarioFalta(1), SegundoApellidodelEmpresarioFalta(2), TomoDatosRegistralesFalta(3),
		FolioDatosRegistralesFalta(4), HojaRegistralDatosRegistralesFalta(5), NIFNoValido(6), CodigoPostalNoValido(7);

		private final int value;

		EnumValidacionesImplicitasCuestionario(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumValidacionesPrimariasLibros {
		DiferenteNumeroLibros(1), // Diferente número de libros (NOMBRES.TXT != DATOS.TXT): El número de libros
									// del fichero nombres.txt no corresponde con el número de libros del fichero
									// datos.txt
		NingunLibroParaLegalizar(2), // Ningún libro para legalizar: Se debe legalizar como mínimo un libro.
		ExistenLibrosDeTipoError(3), // Existen libros de tipo ERROR. Libros con información en el fichero Datos.txt
										// que no están indicados en el fichero nombres.txt.
		NoCoincideLaSecuencia(4), // No coincide el libro %s con el libro %s. No está claro que significa, le
									// hemos dado la funcionalidad de error cuando la secuencia de libros en
									// Datos.Txt no es consecutiva
		InsuficientesDatos(5), // Datos insuficientes de un libro en el fichero datos.txt. Lo usamos si no
								// sabemos el tipo de libro.
		TipoLibroInexistente(6), // El libro X es de tipo INCORRECTO. No es ninguno de los 15 tipos definidos.
		ExcepcionEnLaCargaLibros(7), // Excepción en la carga de libros

		ExistenEjerciciosDistintos(101); // A partir de estas reglas aplican sólo a legalizaciones formato Legalia2

		private final int value;

		EnumValidacionesPrimariasLibros(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumValidacionesPrimariasLibro {
		FechaCierreULtimoLegalizadoFalta(1), FechaCierreULtimoLegalizadoNoValida(2), DemasiadosFicheros(3), // Demasiados
																											// libros
																											// del tipo
																											// X: Se
																											// permite
																											// un máximo
																											// de 9
																											// documentos
																											// por tipo.
		InsuficientesDatos(4); // Insuficientes datos en el libro: Datos insuficientes de un libro en el
								// fichero datos.txt.

		private final int value;

		EnumValidacionesPrimariasLibro(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumValidacionesPrimariasFichero {
		FechaAperturaNoValida(1), FechaCierreNoValida(2), InsuficientesDatos(3), // Insuficientes datos en el libro:
																					// Datos insuficientes de un libro
																					// en el fichero datos.txt.
		NoCoincide(4), // Desconocido. No se está usando. Pendiente ver qué es: No coincide el libro %s
						// con el libro %s. (Error Primario)
		EsDeTipoError(5), // Tiene información en el fichero Datos.txt y no en el fichero nombres.txt.
		FicheroInexistente(6), // El fichero del libro X NO EXISTE: Se produce si un documento está presente en
								// el fichero nombres.txt, pero no se encuentra en el disco
		NumeroLibroDiferente(7), // Número de libro DIFERENTE X vs Y: Hay un conflicto entre los libros definidos
									// en nombres.txt y los definidos en datos.txt.
		ExtensionFicheroNoPermitida(8), HuellaIncorrecta(9), FechaAperturaMayorFechaCierre(10),
		FechaAperturaMenorFechaCierreUltimoLegalizado(11),

		TipoLibroNumeroRepetido(100), // Control tipo de libro y número de orden. Solamente debe permitir un fichero
										// por tipo de libro y número de orden. El LEGALIA anterior permitía incluir más
										// de un fichero para un mismo tipo de libro y mismo número de orden si tenían
										// diferente extensión
		FechaAperturaMenorFechaCierreAnterior(101); // A partir de estas reglas aplican sólo a legalizaciones formato
													// Legalia2

		private final int value;

		EnumValidacionesPrimariasFichero(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static void constantes() {

		Tiposlibros = MGeneral.Idioma.obtenerListaTiposLibro();
		ExtensionesFicheros = MGeneral.Idioma.obtenerListaFormatosFichero("es");
		// MaestroModoEncriptacion m = new MaestroModoEncriptacion();
		// ModosEncriptacion = m.ObtenerListaMaestro();
	}

	public static int dameIndiceDeTipoLibro(enumTipoLibro tipolibro) {
		for (int i = 0; i < kLegalizacion.Tiposlibros.size(); i++) {
			if (kLegalizacion.Tiposlibros.get(i).getCodigo() == tipolibro) {
				return i;
			}
		}
		return -1;
	}

	public static int dameIndiceDeExtensionFichero(enumExtensionFichero extensionfichero) {

		for (int i = 0; i < kLegalizacion.ExtensionesFicheros.size(); i++) {
			if (kLegalizacion.ExtensionesFicheros.get(i).getCodigo() == extensionfichero) {
				return i;
			}
		}
		return -1;
	}

	public static String dameCadenaExtensionesPermitidas(boolean IncluirFormatosEncriptacion) {
		String cad1 = "";
		String cad2 = "";
		StringBuilder cadaux = new StringBuilder();
		String[] extensionesactuales;
		String cadextensionesactuales;

		for (int i = 0; i < kLegalizacion.ExtensionesFicheros.size(); i++) {
			if (!IncluirFormatosEncriptacion && ExtensionesFicheros.get(i).isEsDeEncriptacion()) {
				continue;
			}

			cadaux.append(ExtensionesFicheros.get(i).getExtension());
			extensionesactuales = cadaux.toString().split(";");

			cadextensionesactuales = "";

			if (extensionesactuales.length > 1) {
				for (String extension : extensionesactuales) {
					if (!extension.trim().isEmpty()) {
						if (!cadextensionesactuales.isEmpty()) {
							cadextensionesactuales += ";";
						}
						cadextensionesactuales += "*." + extension;
					}
				}
				cad1 += String.format("|%s (%s)|%s", ExtensionesFicheros.get(i).getDescripcion(),
						cadextensionesactuales, cadextensionesactuales);
			} else {
				cad1 += String.format("|%s (*.%s)|*.%s", ExtensionesFicheros.get(i).getDescripcion(),
						ExtensionesFicheros.get(i).getExtension(), ExtensionesFicheros.get(i).getExtension());
			}

			if (i > 0) {
				cad2 += ";";
			}
			if (extensionesactuales.length > 1) {
				cad2 += cadextensionesactuales;
			} else {
				cad2 += String.format("*.%s", ExtensionesFicheros.get(i).getExtension());
			}
		}

		cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Todos);
		if (cadaux.isEmpty()) {
			cadaux.append("Todos");
		}

		cadaux.append(" (*.*)|");

		cad2 = cadaux + cad2;

		return cad2 + cad1;
	}

	public static enumExtensionFichero dameCodigoDeExtensionFicheroSegunExtension(String Extension) {

		for (int i = 0; i < ExtensionesFicheros.size(); i++) {
			//logger.info("Recorriendo Extensiones: " + kLegalizacion.ExtensionesFicheros.get(i).getExtension());
			if (kLegalizacion.ExtensionesFicheros.get(i).getExtension().equalsIgnoreCase(Extension)) {
				//logger.info("Se retorna: " + kLegalizacion.ExtensionesFicheros.get(i).getCodigo());
				return kLegalizacion.ExtensionesFicheros.get(i).getCodigo();
			}

		}
		return null;
	}

	public static int dameIndiceDeExtensionFicheroSegunExtension(String Extension) {

		for (int i = 0; i < kLegalizacion.ExtensionesFicheros.size(); i++) {
			if (kLegalizacion.ExtensionesFicheros.get(i).getExtension().equalsIgnoreCase(Extension)) {
				return i;
			}
		}

		return -1;
	}

	public static boolean extensionEsDeModoEncriptacion(String extension) {
		kLegalizacion.ModosEncriptacion = MaestroModoEncriptacion.obtenerListaMaestro();
		for (int i = 0; i < kLegalizacion.ModosEncriptacion.size(); i++) {
			if (kLegalizacion.ModosEncriptacion.get(i).getDescripcion().equals(extension.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
