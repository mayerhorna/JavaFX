package com.commerceapp.domain;

import java.util.Locale;
import java.util.logging.Logger;

import com.commerceapp.CommerceApp;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.service.LegalizacionService;

import javafx.application.Platform;

public class MGeneral {
	private static final Logger logger = Logger.getLogger(CommerceApp.class.getName());

	public static final String kgNombreAplicacion = "Legalia 2";
	public static final String KUrlRegistradores = "https://www.registradores.org";
	public static final String kUrlSip2000 = "http://www.sip2000.es";
	public static final String kUrl3eTrade = "http://www.3etrade.es";

	public static ConfiguracionC Configuracion = new ConfiguracionC();
	public static IdiomaC Idioma;
	public static LegalizacionService mlform = null;
	public static cEncriptacion Encriptacion = null;
	public static boolean ModoBatch = false;
	public static String batchAbrirDir = "";
	public static String batchAbrirForm = "";
	public static boolean RetornoFormulario = false;
	public static boolean RetornoFormaDeEnvioDirecto = false;

	public static boolean CargaGlobales() {

		if (!EstablecerCultura()) {

			return false;
		}

		// TODO Auto-generated method stub
		Idioma = new IdiomaC("es");

		if (!Configuracion.iniciar()) {

			return false;
		}

		if (!Configuracion.getIdiomaConfigurado().equals("es")) {

			Idioma = new IdiomaC(Configuracion.getIdiomaConfigurado());
		}

		kLegalizacion.constantes();

		return true;
	}

	public static boolean EstablecerCultura() {
		try {
			Locale locale = new Locale("es", "ES");
			Locale.setDefault(locale);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static void MostrarCultura(String TextoLlamada) {
		try {
			String Cad = TextoLlamada + System.lineSeparator();
			Cad += "Cultura: " + Locale.getDefault().toString() + System.lineSeparator();
			// Cad += "ShortDatePattern: " +
			// Locale.getDefault().getDateTimeFormat(Locale.ShortFormat).getPattern();
			System.out.println(Cad);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
