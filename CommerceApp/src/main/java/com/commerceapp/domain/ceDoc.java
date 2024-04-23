package com.commerceapp.domain;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.XmlTexto;

public class ceDoc {
	private static final String kRespuestaCORPMEeFERespuesta = "CORPMEeFE-Respuesta";
	private static final String kRespuestaCodigoRespuesta = "CodigoRespuesta";
	private static final String kRespuestaDescripcionRespuesta = "DescripcionRespuesta";
	private static final String kRespuestaIdTramite = "Id_Tramite";
	private static final String kRespuestaNombreFicheroDPRZip = "NombreFicheroDPRZip";
	private static final String kRespuestaNombreFicheroNE = "NombreFicheroNE";
	private static final String kRespuestaNombreFicheroAcuseEntrada = "NombreFicheroAcuseEntrada";
	private static final String kRespuestaEnvioReintentable = "EnvioReintentable";
	private static final String kRespuestaCodAccesoNif = "CodAccesoNif";
	private static final String kRespuestaPresentanteNombreConfirmado = "PresentanteNombreConfirmado";
	private static final String kRespuestaPresentanteApellidosConfirmados = "PresentanteApellidosConfirmados";
	private static final String kRespuestaPresentanteCorreoElectronicoConfirmado = "PresentanteCorreoElectronicoConfirmado";

	// Constantes de códigos de respuesta
	private enum ECodigoRespuesta {
		Correcta(0), ErrorParametrosEntrada(1), ErroreDocEntrada(2), ErrorTamanioMaximoSuperado(3),
		ErrorSolicitarTramite(4), ErroreDocRespuestaSolicitarTramite(5), ErrorSeleccionCertificados(6),
		ErrorConfirmacionEnvio(7), ErrorFirmaEnvio(8), ErrorFirmareDocMemoriaInsuficiente(9),
		ErrorFirmareDocSolicitarTimeStamp(10), ErrorFirmareDocOtro(11), ErrorGenerarZip(12), ErrorEnviarTramite(13),
		ErrorProcesarRespuestaEnvioTramiteCorrecto(14), ErrorEnvioTramiteCorrectoSinNE(15),
		SeleccionCertificadosCancelado(100), ConfirmacionEnvioCancelado(101), FirmareDocCancelada(102),
		GenerarZipCancelado(103), EnviarTramiteCancelado(104), ErrorNoContemplado(999);

		private final int valor;

		ECodigoRespuesta(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}

	// Array de caracteres no válidos
	// private final String[] arrayCaracteresNoValidos = {"&", "<", ">", "\"", "'"};
	private final String[] arrayCaracteresNoValidos = {};

	public boolean enviarPorServicio(boolean esReenvio) {
		String pathFicheroEDocInicial = "";
		// CorpmeeFE.FirmaEnvio veFE = new CorpmeeFE.FirmaEnvio();
		// CorpmeeFE.FirmaEnvio.eCodigoRespuesta codigorespuesta;
		StringBuilder xmlrespuesta = new StringBuilder();
		StringBuilder cadidtramite = new StringBuilder();
		StringBuilder caddescripcionrespuesta = new StringBuilder();
		StringBuilder cadnombreficheroenviado = new StringBuilder();
		StringBuilder cadnombreficheroNE = new StringBuilder();
		StringBuilder cadnombreficheroacuse = new StringBuilder();
		boolean bresul = false;

		StringBuilder cadenvioreintentable= new StringBuilder();
		StringBuilder cadcodaccesonif = new StringBuilder();
		StringBuilder cadpresentantenombreconfirmado = new StringBuilder();
		StringBuilder cadpresentanteapellidosconfirmados = new StringBuilder();
		StringBuilder cadpresentantecorreoelectronicoconfirmado = new StringBuilder();

		XmlTexto txml = new XmlTexto();

		try {
			if (MGeneral.mlform.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioCorrectamente) {
				 MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(), IdiomaC.EnumMensajes.EnvioServicioYaRealizado,"","");
				return false;
			}

			if (!esReenvio) {
				if (!new File(MGeneral.mlform.getPathFicheroZip()).exists()) {
					 MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(),
					IdiomaC.EnumMensajes.FicheroInexistente, MGeneral.mlform.getPathFicheroZip(),"");
					return false;
				}

				// if (!MGeneral.mlform.Adjuntos.Valida) return false;

				pathFicheroEDocInicial = MGeneral.mlform.getPathFicheroeDocInicial();
				if (!Ficheros.FicheroBorra(pathFicheroEDocInicial)) {
					 MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(),IdiomaC.EnumMensajes.ErrorAlBorrarFichero, pathFicheroEDocInicial,"");
					return false;
				}

				if (!generareDocInicial(pathFicheroEDocInicial))
					return false;
			}

			do {
				if (!esReenvio) {
					// bresul =
					// veFE.FirmaEnvio(MGeneral.Configuracion.ServicioEnvioCodigoAplicacion,
					// MGeneral.Configuracion.Version,
					// MGeneral.Configuracion.ServicioEnvioTipoSolicitud,
					// pathFicheroEDocInicial,
					// false,
					// codigorespuesta,
					// xmlrespuesta);
				} else {
					/*
					 * bresul = veFE.Reenvio(Configuracion.ServicioEnvioCodigoAplicacion,
					 * MGeneral.mlform.PathFicheroEnviado, MGeneral.mlform.Datos.getCodAccesoNif(),
					 * MGeneral.mlform.Datos.getPresentanteNombreConfirmado(),
					 * MGeneral.mlform.Datos.getPresentanteApellidosConfirmados(),
					 * MGeneral.mlform.Datos.getPresentanteCorreoElectronicoConfirmado(),
					 * codigorespuesta, xmlrespuesta);
					 */
				}
				StringBuilder nulo = new StringBuilder();
				txml.leeNodo(xmlrespuesta, kRespuestaDescripcionRespuesta, caddescripcionrespuesta, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaIdTramite, cadidtramite, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaNombreFicheroDPRZip, cadnombreficheroenviado, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaNombreFicheroNE, cadnombreficheroNE, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaNombreFicheroAcuseEntrada, cadnombreficheroacuse, false, nulo);

				txml.leeNodo(xmlrespuesta, kRespuestaEnvioReintentable, cadenvioreintentable, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaCodAccesoNif, cadcodaccesonif, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaPresentanteNombreConfirmado, cadpresentantenombreconfirmado, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaPresentanteApellidosConfirmados,
						cadpresentanteapellidosconfirmados, false, nulo);
				txml.leeNodo(xmlrespuesta, kRespuestaPresentanteCorreoElectronicoConfirmado,
						cadpresentantecorreoelectronicoconfirmado, false, nulo);

				/*if (codigorespuesta == CorpmeeFE.FirmaEnvio.eCodigoRespuesta.ErrorEnvioTramiteCorrectoSinNE)
					bresul = true;*/

				if (bresul) {
					//MGeneral.mlform.Datos.setEnviado(System.DateTime.Now.toString());

					MGeneral.mlform.Datos.set_eDocIdTramite(cadidtramite.toString());
					MGeneral.mlform.Datos.set_eDocNombreFicheroEnviado(cadnombreficheroenviado.toString());
					MGeneral.mlform.Datos.set_eDocNombreFicheroNE(cadnombreficheroNE.toString());
					MGeneral.mlform.Datos.set_eDocNombreFicheroAcuseEntrada(cadnombreficheroacuse.toString());

					MGeneral.mlform.Datos.set_EnvioReintentable("");
					MGeneral.mlform.Datos.set_CodAccesoNif(cadcodaccesonif.toString());
					MGeneral.mlform.Datos.set_PresentanteNombreConfirmado(cadpresentantenombreconfirmado.toString());
					MGeneral.mlform.Datos.set_PresentanteApellidosConfirmados(cadpresentanteapellidosconfirmados.toString());
					MGeneral.mlform.Datos
							.set_PresentanteCorreoElectronicoConfirmado(cadpresentantecorreoelectronicoconfirmado.toString());

					MGeneral.mlform.guardaDatosGenerales();
					Ficheros.FicheroBorra(pathFicheroEDocInicial);

					/*if (codigorespuesta == CorpmeeFE.FirmaEnvio.eCodigoRespuesta.ErrorEnvioTramiteCorrectoSinNE) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EnvioCorrectoSinObtenerNE, "", "", "");
						return false;
					}
*/
					return true;

				} else {
					if (!cadenvioreintentable.isEmpty()) {
						MGeneral.mlform.Datos.set_eDocIdTramite(cadidtramite.toString());
						MGeneral.mlform.Datos.set_eDocNombreFicheroEnviado(cadnombreficheroenviado.toString());

						MGeneral.mlform.Datos.set_EnvioReintentable(cadenvioreintentable.toString());
						MGeneral.mlform.Datos.set_CodAccesoNif(cadcodaccesonif.toString());
						MGeneral.mlform.Datos.set_PresentanteNombreConfirmado(cadpresentantenombreconfirmado.toString());
						MGeneral.mlform.Datos.set_PresentanteApellidosConfirmados(cadpresentanteapellidosconfirmados.toString());
						MGeneral.mlform.Datos
								.set_PresentanteCorreoElectronicoConfirmado(cadpresentantecorreoelectronicoconfirmado.toString());
						MGeneral.mlform.guardaDatosGenerales();
					}

				/*	switch (codigorespuesta) {
					case ErrorFirmareDocMemoriaInsuficiente:
						MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(),
								IdiomaC.EnumMensajes.MemoriaInsuficienteFirma, caddescripcionrespuesta,"");
						return false;
					case ConfirmacionEnvioCancelado:
					case SeleccionCertificadosCancelado:
					case FirmareDocCancelada:
					case GenerarZipCancelado:
					case EnviarTramiteCancelado:
						return false;
					case ErroreDocEntrada:
						MGeneral.mlform.ErrorOcurrido.GeneraError(MGeneral.mlform.MostrarMensajes,
								IdiomaC.EnumMensajes.Excepcion, caddescripcionrespuesta);
						return false;
					default:
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorAlEnviarTramite,
								caddescripcionrespuesta, "", "");
						return false;
					}*/
				}
			} while (true);
		} catch (Exception ex) {
			MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion,
					ex.getMessage(),"");
			return false;
		}
		//return false;
	}

	public boolean generareDocInicial(String pathficherodestino) {
		boolean resultado = false;
		XmlTexto txmlsal = new XmlTexto();

		try {
			if (!compruebaTodosLosNodos())
				return false;

			if (!generarCabeceraYComienzo(txmlsal))
				return false;

			if (!generarIdentificacionPresentante(txmlsal))
				return false;

			if (!generarDatosFacturacion(txmlsal))
				return false;

			if (!generarObjetos(txmlsal))
				return false;

			if (!generarIdentificacionDocumento(txmlsal))
				return false;

			txmlsal.escribeFinNodo(meDoc.kNodoEnvio_Tramite);
			txmlsal.escribeFinNodo(meDoc.kNodoDatos_PRIVADO);

			generarDocumentos(txmlsal);

			txmlsal.escribeFinNodo(meDoc.kNodoCORPMEeDoc);

			if (!txmlsal.guardarEnFichero(pathficherodestino, meDoc.kNombreCodificacioneDoc)) {
				MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(),
						IdiomaC.EnumMensajes.EscrituraFicheroFallida, pathficherodestino,"");
				return false;
			}

			resultado = true;

		} catch (Exception ex) {
			MGeneral.mlform.ErrorOcurrido.generaError(MGeneral.mlform.isMostrarMensajes(), IdiomaC.EnumMensajes.Excepcion,
					ex.getMessage(),"");
		}

		return resultado;
	}

	private boolean generarIdentificacionDocumento(XmlTexto txmlsal) {
	    String cad;
	    int i, j;
	    String caddeslib;

	    txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_Documento, true);

	    txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_DocumentoEntrada, true);

	    if (MGeneral.mlform.Datos.get_eDocEntradaTipo().equals(meDoc.kValorIdentificacion_DocumentoEntradaTipoSUBSANACION)) {

	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoEntradaTipo, meDoc.kValorIdentificacion_DocumentoEntradaTipoSUBSANACION, true);

	        txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_DocumentoEntradaNumEntradaMercantil, true);

	        StringBuilder libro = new StringBuilder();
	        StringBuilder anyo = new StringBuilder();
	        StringBuilder numero = new StringBuilder();

	        dameDatosEntrada(MGeneral.mlform.Datos.get_eDocEntradaSubsanada(), libro, anyo, numero);

	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilLibro, libro.toString(), true);
	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilAnyo, anyo.toString(), true);
	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilNumero, numero.toString(), true);

	        txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_DocumentoEntradaNumEntradaMercantil);

	    } else {
	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoEntradaTipo, meDoc.kValorIdentificacion_DocumentoEntradaTipoENTRADA_NUEVA, true);
	    }

	    txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_DocumentoEntrada);

	    txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoFechaDocumento, dameFechaEdoc(MGeneral.mlform.Presentacion.getFechaSolicitud()), true);

	    if (!Formato.ValorNulo(MGeneral.mlform.Datos.get_eDocNumeroDocumento())) {
	        txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoNumeroDocumento, MGeneral.mlform.Datos.get_eDocNumeroDocumento(), true);
	    }

	    txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacion, true);

	    txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacionMercantil, true);

	    txmlsal.escribeNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacionMercantilOperacion, meDoc.kValorIdentificacion_DocumentoTipoOperacionMercantilOperacionLEGALIZACION, true);

	    txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacionMercantilDatos_Operacion, true);

	    txmlsal.escribeInicioNodo(meDoc.kNodoLegalizacion_Libros, true);

	    txmlsal.escribeInicioNodo(meDoc.kNodoLegalizacion_LibrosLibros, false);
	    txmlsal.escribeAtributo(meDoc.kAtributoLegalizacion_LibrosLibrosNumero_Libros, Integer.toString(MGeneral.mlform.getNumeroFicheros()), true);

	    for (i = 0; i < MGeneral.mlform.getNumeroLibros(); i++) {
	        for (j = 0; j < MGeneral.mlform.Libros.get(i).getNumeroFicheros(); j++) {
	            txmlsal.escribeInicioNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibro, true);
	            caddeslib = MGeneral.mlform.Libros.get(i).Ficheros[j].getDescripcion().replace("#", "").trim();
	            txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroNombre, caddeslib, true);
	            txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroNombre_Fichero, MGeneral.mlform.Libros.get(i).Ficheros[j].getNombreFichero(), true);
	            txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroNumero, Integer.toString(MGeneral.mlform.Libros.get(i).Ficheros[j].getNumero()), true);

	            if (!Formato.ValorNulo(MGeneral.mlform.Libros.get(i).Ficheros[j].getFechaApertura())) {
	                txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroFecha_Apertura, dameFechaEdoc(MGeneral.mlform.Libros.get(i).Ficheros[j].getFechaApertura()), true);
	            }

	            if (!Formato.ValorNulo(MGeneral.mlform.Libros.get(i).Ficheros[j].getFechaCierre())) {
	                txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroFecha_Cierre, dameFechaEdoc(MGeneral.mlform.Libros.get(i).Ficheros[j].getFechaCierre()), true);
	            }

	            if (!Formato.ValorNulo(MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado())) {
	                txmlsal.escribeNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibroFecha_Cierre_Anterior, dameFechaEdoc(MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado()), true);
	            }

	            txmlsal.escribeFinNodo(meDoc.kNodoLegalizacion_LibrosLibrosLibro);
	        }
	    }

	    txmlsal.escribeFinNodo(meDoc.kNodoLegalizacion_LibrosLibros);

	    txmlsal.escribeFinNodo(meDoc.kNodoLegalizacion_Libros);

	    txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacionMercantilDatos_Operacion);

	    txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacionMercantil);

	    txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_DocumentoTipoOperacion);

	    txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_Documento);

	    return true;
	}
	private String dameFechaEdoc(String cadenaFecha) {
	    String fechaFormateada = "";

	    if (Formato.ValorNulo(cadenaFecha)) return fechaFormateada;

	    if (cadenaFecha.trim().length() != 8) return fechaFormateada;

	    if (!cadenaFecha.matches("\\d+")) return fechaFormateada;

	    String dia = cadenaFecha.substring(0, 2);
	    String mes = cadenaFecha.substring(2, 4);
	    String anio = cadenaFecha.substring(4, 8);

	    try {
	        fechaFormateada = dia + "/" + mes + "/" + anio;
	    } catch (Exception e) {
	        return fechaFormateada;
	    }

	    return fechaFormateada;
	}
	
	
	private void generarDocumentos(XmlTexto txmlsal) {
		boolean hayadjuntos = true; // MGeneral.mlform.Adjuntos.getNumeroFicheros() > 0;

		// CORPME-eDoc/Documentos
		txmlsal.escribeInicioNodo(meDoc.kNodoDocumentos, false);
		// Si hay adjuntos se envían 2 documentos, sino 1
		txmlsal.escribeAtributo(meDoc.kAtributoNumero_Documentos, hayadjuntos ? "2" : "1", true);

		// CORPME-eDoc/Documentos/Documento
		txmlsal.escribeInicioNodo(meDoc.kNodoDocumento, true);

		// CORPME-eDoc/Documentos/Documento/Firma_Electronica
		txmlsal.escribeNodo(meDoc.kNodoFirma_Electronica, meDoc.kValorSi, true);

		// CORPME-eDoc/Documento/Documento/Ficheros
		txmlsal.escribeInicioNodo(meDoc.kNodoFicheros, false);
		// txmlsal.EscribeAtributo(meDoc.kAtributoNumero_Ficheros, Integer.toString(1 +
		// MGeneral.mlform.Adjuntos.getNumeroFicheros()), true);

		// CORPME-eDoc/Documento/Documento/Ficheros/Fichero
		txmlsal.escribeInicioNodo(meDoc.kNodoFichero, true);

		// CORPME-eDoc/Documento/Documento/Ficheros/Fichero/Nomre_Fichero: hay que poner
		// el path completo del fichero
		txmlsal.escribeNodo(meDoc.kNodoNombreFichero, MGeneral.mlform.getPathFicheroZip(), true);

		txmlsal.escribeFinNodo(meDoc.kNodoFichero);

		if (hayadjuntos) {
			// Un nodo Fichero por cada fichero adjunto
			/*
			 * for (int i = 0; i < MGeneral.mlform.Adjuntos.getNumeroFicheros(); i++) { //
			 * CORPME-eDoc/Documento/Documento/Ficheros/Fichero
			 * txmlsal.escribeInicioNodo(meDoc.kNodoFichero, true);
			 * 
			 * // CORPME-eDoc/Documento/Documento/Ficheros/Fichero/Nomre_Fichero: hay que
			 * poner el path completo del fichero
			 * txmlsal.EscribeNodo(meDoc.kNodoNombreFichero,
			 * MGeneral.mlform.Adjuntos.getFicheros()[i].getPathFichero(), true);
			 * 
			 * txmlsal.escribeFinNodo(meDoc.kNodoFichero); }
			 */
		}

		txmlsal.escribeFinNodo(meDoc.kNodoFicheros);
		txmlsal.escribeFinNodo(meDoc.kNodoDocumento);
		txmlsal.escribeFinNodo(meDoc.kNodoDocumentos);
	}

	private String dameNumeros(String cadena) {
		StringBuilder cad = new StringBuilder();

		// Se devuelven todos los caracteres numéricos concatenados
		for (int i = 0; i < cadena.length(); i++) {
			if (Character.isDigit(cadena.charAt(i))) {
				cad.append(cadena.charAt(i));
			}
		}

		return cad.toString();
	}

	public String dameHojaeDoc(String HojaLegalia) {
		String cad;
		int pos;

		if (Formato.isNumeric(HojaLegalia))
			return dameNumeros(HojaLegalia);

		// Hay un '-', y lo que tiene a la derecha es numérico se devuelve
		pos = HojaLegalia.indexOf("-", 0);
		if (pos > 0) {
			cad = HojaLegalia.substring(pos + 1);
			if (Formato.isNumeric(cad))
				return dameNumeros(cad);
		}

		return dameNumeros(HojaLegalia);
	}

	private boolean generarObjetos(XmlTexto txmlsal) {
		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos
		txmlsal.escribeInicioNodo(meDoc.kNodoObjetos, true);

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos/Sociedades
		txmlsal.escribeInicioNodo(meDoc.kNodoSociedades, false);
		txmlsal.escribeAtributo(meDoc.kAtributoNumero_Sociedades, "1", true);

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos/Sociedades/Sociedad
		txmlsal.escribeInicioNodo(meDoc.kNodoSociedad, true);

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos/Sociedades/Sociedad/Denominacion_Social
		txmlsal.escribeInicioNodo(meDoc.KNodoSociedadDenominacion_Social, true);
		txmlsal.escribeTexto(MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario(), true);
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getApellido1())) {
			txmlsal.escribeTexto(" " + MGeneral.mlform.Presentacion.getApellido1(), true);
		}
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getApellido2())) {
			txmlsal.escribeTexto(" " + MGeneral.mlform.Presentacion.getApellido2(), true);
		}
		txmlsal.escribeFinNodo(meDoc.KNodoSociedadDenominacion_Social);

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos/Sociedades/Sociedad/CIF
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getNifCif())) {
			txmlsal.escribeNodo(meDoc.KNodoSociedadCIF, MGeneral.mlform.Presentacion.getNifCif(), true);
		}

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite/Objetos/Sociedades/Sociedad/Hoja
		if (!Formato.ValorNulo(dameHojaeDoc(MGeneral.mlform.Presentacion.getDatosRegistralesHoja()))) {
			txmlsal.escribeNodo(meDoc.kNodoSociedadHoja,
					dameHojaeDoc(MGeneral.mlform.Presentacion.getDatosRegistralesHoja()), true);
		}

		txmlsal.escribeFinNodo(meDoc.kNodoSociedad);
		txmlsal.escribeFinNodo(meDoc.kNodoSociedades);
		txmlsal.escribeFinNodo(meDoc.kNodoObjetos);

		return true;
	}

	private boolean generarDatosFacturacion(XmlTexto txmlsal) {
		// Datos_Facturacion
		txmlsal.escribeInicioNodo(meDoc.kNodoDatos_Facturacion, true);

		// Destinatario
		txmlsal.escribeInicioNodo(meDoc.kNodoDestinatario, true);

		switch (MGeneral.mlform.Datos.get_TipoPersona()) {
		case kLegalizacion.kTipoPersonaFisica:
			txmlsal.escribeNodo(meDoc.kNodoTipo_Personalidad, meDoc.kTipo_PersonalidadFisica, true);
			txmlsal.escribeNodo(meDoc.kNodoNombre, MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario(), true);
			String cadapellidos = MGeneral.mlform.Presentacion.getApellido1() + " "
					+ MGeneral.mlform.Presentacion.getApellido2();
			txmlsal.escribeNodo(meDoc.kNodoApellidos, cadapellidos.trim(), true);
			txmlsal.escribeInicioNodo(meDoc.kNodoIdentificador_Administrativo, true);
			txmlsal.escribeNodo(meDoc.kNodoIdentificador_AdministrativoTipo, meDoc.kTipoIdentificador_AdministrativoNIF,
					true);
			txmlsal.escribeNodo(meDoc.kNodoIdentificador_AdministrativoNumero_Documento,
					MGeneral.mlform.Presentacion.getNifCif(), true);
			txmlsal.escribeFinNodo(meDoc.kNodoIdentificador_Administrativo);
			break;
		default:
			txmlsal.escribeNodo(meDoc.kNodoTipo_Personalidad, meDoc.kTipo_PersonalidadJuridica, true);
			txmlsal.escribeNodo(meDoc.kNodoNombre, MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario(), true);
			txmlsal.escribeInicioNodo(meDoc.kNodoIdentificador_Administrativo, true);
			txmlsal.escribeNodo(meDoc.kNodoIdentificador_AdministrativoTipo, meDoc.kTipoIdentificador_AdministrativoCIF,
					true);
			txmlsal.escribeNodo(meDoc.kNodoIdentificador_AdministrativoNumero_Documento,
					MGeneral.mlform.Presentacion.getNifCif(), true);
			txmlsal.escribeFinNodo(meDoc.kNodoIdentificador_Administrativo);
			break;
		}

		txmlsal.escribeFinNodo(meDoc.kNodoDestinatario);

		// Domicilio
		txmlsal.escribeInicioNodo(meDoc.kNodoDomicilio, true);

		// Cod_Provincia
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getProvinciaCodigo())) {
			txmlsal.escribeNodo(meDoc.kNodoDomicilioCod_Provincia,
					Integer.parseInt(MGeneral.mlform.Presentacion.getProvinciaCodigo()) + "", true); // Es integer
		}

		// Des_Municipio
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getCiudad())) {
			txmlsal.escribeNodo(meDoc.kNodoDomicilioDes_Municipio, MGeneral.mlform.Presentacion.getCiudad(), true);
		}

		// Cod_Tipo_Via
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getDomicilio())) {
			txmlsal.escribeNodo(meDoc.kNodoDomicilioCod_Tipo_Via, meDoc.kValorDomicilioCod_Tipo_ViaCALLE, true);
		}

		// Nombre_Via
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getDomicilio())) {
			txmlsal.escribeNodo(meDoc.kNodoDomicilioNombre_Via, MGeneral.mlform.Presentacion.getDomicilio(), true);
		}

		// Codigo_Postal
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getCodigoPostal())) {
			txmlsal.escribeNodo(meDoc.kNodoDomicilioCodigo_Postal, MGeneral.mlform.Presentacion.getCodigoPostal(),
					true);
		}

		// Pais
		txmlsal.escribeNodo(meDoc.kNodoDomicilioPais, meDoc.kValorDomicilioPaisESPANIA, true);

		txmlsal.escribeFinNodo(meDoc.kNodoDomicilio);

		// IRPF
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getPresentante().get_SolicitaRetencion())) {
			txmlsal.escribeInicioNodo(meDoc.kNodoDatos_FacturacionIRPF, true);

			if (MGeneral.mlform.Presentacion.getPresentante().get_SolicitaRetencion().equals("SI")) {
				txmlsal.escribeNodo(meDoc.kNodoDatos_FacturacionIRPFAplicar_IRPF, "S", true);
			} else {
				txmlsal.escribeNodo(meDoc.kNodoDatos_FacturacionIRPFAplicar_IRPF, "N", true);
			}

			txmlsal.escribeFinNodo(meDoc.kNodoDatos_FacturacionIRPF);
		}

		txmlsal.escribeFinNodo(meDoc.kNodoDatos_Facturacion);

		return true;
	}

	private boolean generarIdentificacionPresentante(XmlTexto txmlsal) {
		// Identificacion_Presentante
		txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_Presentante, true);

		txmlsal.escribeInicioNodo(meDoc.kNodoIdentificacion_PresentantePresentante, true);

		txmlsal.escribeNodo(meDoc.kNodoTipo_Personalidad, meDoc.kTipo_PersonalidadFisica, true);

		txmlsal.escribeNodo(meDoc.kNodoNombre, MGeneral.mlform.Presentacion.getPresentante().get_Nombre(), true);
		String cadapellidos = MGeneral.mlform.Presentacion.getPresentante().get_Apellido1();
		if (!Formato.ValorNulo(MGeneral.mlform.Presentacion.getPresentante().get_Apellido2()))
			cadapellidos += " " + MGeneral.mlform.Presentacion.getPresentante().get_Apellido2();
		txmlsal.escribeNodo(meDoc.kNodoApellidos, cadapellidos, true);

		txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_PresentantePresentante);

		txmlsal.escribeInicioNodo(meDoc.kNodoDatos_Contacto, true);
		txmlsal.escribeNodo(meDoc.kNodoDatos_ContactoCorreo_Electronico,
				MGeneral.mlform.Presentacion.getPresentante().get_Email(), true);
		txmlsal.escribeFinNodo(meDoc.kNodoDatos_Contacto);

		txmlsal.escribeFinNodo(meDoc.kNodoIdentificacion_Presentante);

		return true;
	}

	private boolean generarCabeceraYComienzo(XmlTexto txmlsal) {
		// Inicio del xml
		txmlsal.escribeInicioXml(meDoc.kNombreCodificacioneDoc);

		// CORPME-eDoc
		txmlsal.escribeInicioNodo(meDoc.kNodoCORPMEeDoc, false);

		// Atributos de la cabecera
		txmlsal.escribeAtributo(meDoc.kAtributoCodigo_Registro,
				dameCodigoRegistro(MGeneral.mlform.Presentacion.getRegistroMercantilDestinoCodigo()), false);

		txmlsal.escribeFinNombreNodo();

		// Datos_PRIVADO
		txmlsal.escribeInicioNodo(meDoc.kNodoDatos_PRIVADO, true);

		// CORPME-eDoc/Datos_PRIVADO/Envio_Tramite
		txmlsal.escribeInicioNodo(meDoc.kNodoEnvio_Tramite, true);

		return true;
	}

	private String dameCodigoRegistro(String CodigoRegistro) {
		String codigo = "00000" + CodigoRegistro;
		return codigo.substring(codigo.length() - 5, codigo.length());
	}

	public void dameDatosEntrada(String entrada, StringBuilder libro, StringBuilder anyo, StringBuilder numero) {
		libro.setLength(0);
		anyo.setLength(0);
		numero.setLength(0);

		String[] cadentrada = entrada.split("/", -1);

		if (cadentrada.length > 0) {
			libro.append(cadentrada[0]);
		}
		if (cadentrada.length > 1) {
			anyo.append(cadentrada[1]);
		}
		if (cadentrada.length > 2) {
			numero.append(cadentrada[2]);
		}
	}

	private boolean compruebaCaracteresNoValidos(String Texto, StringBuilder ContenidoNoValido) {
		ContenidoNoValido.setLength(0);

		for (int i = 0; i < arrayCaracteresNoValidos.length; i++) {
			if (Texto.contains(arrayCaracteresNoValidos[i])) {
				ContenidoNoValido.append(arrayCaracteresNoValidos[i]);
				return false;
			}
		}

		return true;
	}

	private boolean compruebaContenidoNodo(String NombreNodo, String Texto) {
		StringBuilder caracter = new StringBuilder();

		if (compruebaCaracteresNoValidos(Texto, caracter) == false) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ContenidoCampoCaracterNoPermitido, NombreNodo,
					caracter.toString(), "");
			return false;
		}

		return true;
	}

	public boolean compruebaTodosLosNodos() {
		if (!Formato.validaSoloLetrasYNumeros(MGeneral.mlform.Presentacion.getNifCif())) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ContendioCamposSoloLetrasYNumeros,
					meDoc.kNodoSociedad + " " + meDoc.KNodoSociedadCIF, "", "");
			return false;
		}

		if (!Formato.validaSoloLetrasYNumeros(MGeneral.mlform.Presentacion.getProvinciaCodigo())) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ContenidoCampoSoloNumeros,
					meDoc.kNodoSociedad + " " + meDoc.kNodoDomicilioCod_Provincia, "", "");
			return false;
		}

		if (arrayCaracteresNoValidos.length == 0)
			return true; // Se admiten todos los caracteres

		if (!compruebaContenidoNodo(meDoc.KNodoSociedadDenominacion_Social,
				MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario()))
			return false;
		if (!compruebaContenidoNodo(meDoc.KNodoSociedadDenominacion_Social,
				MGeneral.mlform.Presentacion.getApellido1()))
			return false;
		if (!compruebaContenidoNodo(meDoc.KNodoSociedadDenominacion_Social,
				MGeneral.mlform.Presentacion.getApellido2()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.KNodoSociedadCIF,
				MGeneral.mlform.Presentacion.getNifCif()))
			return false;

		if (!compruebaContenidoNodo(meDoc.kNodoIdentificacion_PresentantePresentante + " " + meDoc.kNodoNombre,
				MGeneral.mlform.Presentacion.getPresentante().get_Nombre()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoIdentificacion_PresentantePresentante + " " + meDoc.kNodoApellidos,
				MGeneral.mlform.Presentacion.getPresentante().get_Apellido1()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoIdentificacion_PresentantePresentante + " " + meDoc.kNodoApellidos,
				MGeneral.mlform.Presentacion.getPresentante().get_Apellido2()))
			return false;
		if (!compruebaContenidoNodo(
				meDoc.kNodoIdentificacion_PresentantePresentante + " " + meDoc.kNodoDatos_ContactoCorreo_Electronico,
				MGeneral.mlform.Presentacion.getPresentante().get_Email()))
			return false;

		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.kNodoDomicilioDes_Municipio,
				MGeneral.mlform.Presentacion.getCiudad()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.kNodoDomicilioNombre_Via,
				MGeneral.mlform.Presentacion.getDomicilio()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.kNodoDomicilioCodigo_Postal,
				MGeneral.mlform.Presentacion.getCodigoPostal()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.kNodoDatos_ContactoTelefono,
				MGeneral.mlform.Presentacion.getTelefono()))
			return false;
		if (!compruebaContenidoNodo(meDoc.kNodoSociedad + " " + meDoc.kNodoDatos_ContactoFax,
				MGeneral.mlform.Presentacion.getFax()))
			return false;

		if (!compruebaContenidoNodo(meDoc.kNodoIdentificacion_DocumentoNumeroDocumento,
				MGeneral.mlform.Datos.get_eDocNumeroDocumento()))
			return false;

		for (int i = 0; i < MGeneral.mlform.getNumeroLibros(); i++) {
			for (int j = 0; j < MGeneral.mlform.Libros.get(i).getNumeroFicheros(); j++) {
				// Nombre del tipo de libro
				if (!compruebaContenidoNodo(
						meDoc.kNodoLegalizacion_LibrosLibrosLibro + " "
								+ meDoc.kNodoLegalizacion_LibrosLibrosLibroNombre,
						MGeneral.mlform.Libros.get(i).Ficheros[j].getDescripcion()))
					return false;
			}
		}

		return true;
	}
}
