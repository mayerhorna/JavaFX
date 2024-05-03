package com.commerceapp.domain;

import java.awt.SystemColor;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.StringReader;

import java.io.UnsupportedEncodingException;

import java.lang.module.ModuleDescriptor.Version;

import java.net.HttpURLConnection;

import java.net.URL;

import java.net.URLDecoder;

import java.nio.charset.Charset;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.nio.file.LinkOption;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.security.MessageDigest;

import java.text.Normalizer;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.Map.Entry;

import java.util.Objects;

import java.util.Properties;

import java.util.jar.Attributes;

import java.util.jar.Manifest;

import java.util.logging.Logger;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.TransformerException;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import javax.xml.xpath.XPath;

import javax.xml.xpath.XPathConstants;

import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.NamedNodeMap;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import org.w3c.dom.xpath.XPathExpression;

import org.xml.sax.SAXException;

import com.commerceapp.Main;
import com.commerceapp.controller.ConfiguracionController;
import com.commerceapp.controller.EncriptacionController;
import com.commerceapp.controller.MensajeController;
import com.commerceapp.controller.PoliticaPrivacidadController;
import com.commerceapp.controller.VersionesController;
import com.commerceapp.domain.MiVersion;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.ManejoXml;
import com.commerceapp.util.Utilidades;

import org.xml.sax.InputSource;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;

import javafx.stage.Stage;

import javafx.stage.StageStyle;

public class ConfiguracionC {

	private static final Logger logger = Logger.getLogger(ConfiguracionC.class.getName());

	public static class sMunicipiosCentral {

		public String CPRO;

		public String Nombre;

		public String Operacion;

		public String NombreModificado;

	}

	// Constantes

	private static final String _kNombreFicheroConfiguracion = "ConfLegaliaMP.xml";

	private static final String _kNombreDirectorioTrabajo = ".CommerceApp";

	private static final String _kNombreDirectorioAuxiliar = "DirAux";

	private static final String _kPatronNombreFicherosSetup = "Legalia2*.exe";

	private static final String _kPatronNombreFicherosMSI = "Legalia2*.msi";

	private static final String _kPatronNombreFicheroSetup = "Legalia2Instalar.msi";

	private static final String _kNodoPrincipal = "Configuracion";

	private static final String _kNodoLegalia2 = "CommerceApp";

	private static final String _KNodoLogActivado = "LogActivado";

	private static final boolean _kValorDefectoLogActivado = false;

	private static final String _kNodoVersionCentral = "Version";

	private static final String _kUrlXmlCentral = "https://desarrollo.3etrade.com/LegaliaMP/VersionesLegaliaMP.xml";

	// Nodo que si existe, se tomará su valor para la dirección del XML DE

	// CONFIGURACION CENTRAL

	private static final String _kNodoDesarrolloUrlXmlCentral = "DesarrolloUrlXmlCentral";

	private static final String _kNodoAplicarNuevasReglasAVersionLegalia = "AplicarNuevasReglasAVersionLegalia";

	private static final boolean _kValorDefectoAplicarNuevasReglasAVersionLegalia = false;

	private static final String _kNodoBytesMaximosZip = "BytesMaximosZip";

	private static final long _kValorDefectoBytesMaximosZip = 300L * 1024L * 1024L;

	private static final String _kNodoBytesAvisoZip = "BytesAvisoZip";

	private static final long _kValorDefectoBytesAvisoZip = 330L * 1024L * 1024L;

	private static final String _kNodoAdjuntosMaximos = "AdjuntosMaximos";

	private static final int _kValorDefectoAdjuntosMaximos = 3;

	private static final String _kNodoCentralContenidoVersiones = "ContenidoVersiones";

	private static final String _kNodoCentralContenidoVersionesContenidoVersion = "ContenidoVersion";

	private static final String _kNodoCentralContenidoVersionesContenidoVersionVersion = "Version";

	private static final String _kNodoCentralContenidoVersionesContenidoVersionObligatoria = "Obligatoria";

	private static final String _kNodoCentralContenidoVersionesContenidoVersionContenido = "Contenido";

	private static final String _kNodoCentralContenidoVersionesContenidoVersionMensaje = "Mensaje";

	private static final String _kNodoIdioma = "Idioma";

	private static final String _kValorDefectoIdioma = "es";

	private static final String _kNodoPathDatos = "PathDatos";

	private static final String _kValorDefectoNombreDirectorioDatos = "CommerceApp";

//DIRECCIÓN DEL PORTAL DE ENVÍO DE LOS LIBROS

	private static final String _kNodoUrlPortal = "UrlPortal";

//Entorno pre-producción (pruebas)

//private static final String _kValorDefectoUrlPortal = "https://webintegracion.registradores.org/registroVirtual/accesoCargaDirectaLibroCuenta.do";

//Entorno producción

	private static final String _kValorDefectoUrlPortal = "https://sede.registradores.org/";

	private static final String _kNodoComprobarActualizaciones = "ComprobarActualizaciones";

	private static final String _KNodoUrlSetup = "UrlSetup"; // Nombre del nodo en xml central que indica la URL donde

	private static final String _KNodoUrlSetupWindows = "UrlSetupWindows";

	private static final String _KNodoUrlSetupMac = "UrlSetupMac";

	private static final String _KNodoUrlSetupLinux = "UrlSetupLinux";// está el setup de la versión

	private static final String _kNodoValorDefectoCodigoRegistro = "ValorDefectoCodigoRegistro";

	private static final String _kNodoValorDefectoCodigoProvincia = "ValorDefectoCodigoProvincia";

	private static final String _kNodoValoresDefectoPresentante = "ValoresDefectoPresentante";

	private static final String _KNodoValorDefectoPresentanteNombre = "Nombre";

	private static final String _KNodoValorDefectoPresentanteApellido1 = "Apellido1";

	private static final String _KNodoValorDefectoPresentanteApellido2 = "Apellido2";

	private static final String _KNodoValorDefectoPresentanteNif = "Nif";

	private static final String _KNodoValorDefectoPresentanteDomicilio = "Domicilio";

	private static final String _KNodoValorDefectoPresentanteCiudad = "Ciudad";

	private static final String _KNodoValorDefectoPresentanteCodigoPostal = "CodigoPostal";

	private static final String _KNodoValorDefectoPresentanteProvincia = "Provincia";

	private static final String _KNodoValorDefectoPresentanteTelefono = "Telefono";

	private static final String _KNodoValorDefectoPresentanteFax = "Fax";

	private static final String _KNodoValorDefectoPresentanteEmail = "Email";

	private static final String _kNodoValorDefectoPresentanteSolicitaRetencion = "SolicitaRetencion";

	private static final String _kNodoMostrarMensajeAlEncriptarEnLegalizacion = "MostrarMensajeAlEncriptarEnLegalizacion";

	private static final boolean _kValorDefectoMostrarMensajeAlEncriptarEnLegalizacion = true;

	private static final String _kNodoPoliticaPrivacidadAceptada = "PoliticaPrivacidadAceptada";

	private static final long _kBytesFicheroParaIndicarProgreso = (1204 * 1024 * 10);

	private static final String kServicioEnvioCodigoAplicacion = "LEGALIA";

	private static final String kServicioEnvioTipoSolicitud = "LEGALIZACION_LIBROS";

	private static final String AssemblyCompanyAttribute = "CORPME";

	private static final String AssemblyCopyrightAttribute = "© 2024";

	private static final String OperatingSystem = "os.name";

	private static boolean InicioSesion = false;
	public static ArrayList<String[]> ProductoPedido = new ArrayList<>();

	// Variables

	public static boolean isInicioSesion() {
		return InicioSesion;
	}

	public static void setInicioSesion(boolean inicioSesion) {
		InicioSesion = inicioSesion;
	}

	public static String getOperatingsystem() {

		return System.getProperty(OperatingSystem).toLowerCase();

	}

	private String _PathRaiz;// Directorio donde estaran las carpetas .CommerceApp y CommerceApp

	public static String getAssemblycompanyattribute() {

		return AssemblyCompanyAttribute;

	}

	public static String getAssemblycopyrightattribute() {

		return AssemblyCopyrightAttribute;

	}

	private static String _PathTrabajo;

	private static String _PathAuxiliar;

	private String _FicheroConfiguracion;

	private boolean _ConfiguracionCorrecta;

	private String _ErrorCargaConfiguracion;

	private boolean _LogActivado;

	private boolean _CentralConfiguracionCorrecta;

	private StringBuilder _CentralVersion;

	private boolean _AplicarNuevasReglasAVersionLegalia;

	private boolean _CentralAplicarNuevasReglasAVersionLegalia;

	private long _BytesMaximosZip;

	private long _CentralBytesMaximosZip;

	private long _BytesAvisoZip;

	private long _CentralBytesAvisoZip;

	private int _AdjuntosMaximos;

	private int _CentralAdjuntosMaximos;

	private String _CentralContenidoVersiones;

	private boolean _EsPrimeraUtilizacion = false;

	private String _IdiomaConfigurado;

	private String _PathDatos;

	private String _UrlPortal;

	private String _CentralUrlSetup;

	private String _CentralUrlSetupWindows;

	private String _CentralUrlSetupMac;

	private String _CentralUrlSetupLinux;

	private String _ValorDefectoCodigoRegistro;

	private String _ValorDefectoCodigoProvincia;

	private String _DesarrolloUrlXmlCentral;

	private String _ValorDefectoPresentanteNombre;

	private String _ValorDefectoPresentanteApellido1;

	private String _ValorDefectoPresentanteApellido2;

	private String _ValorDefectoPresentanteNif;

	private String _ValorDefectoPresentanteDomicilio;

	private String _ValorDefectoPresentanteCiudad;

	private String _ValorDefectoPresentanteCodigoPostal;

	private String _ValorDefectoPresentanteCodigoProvincia;

	private String _ValorDefectoPresentanteTelefono;

	private String _ValorDefectoPresentanteFax;

	private String _ValorDefectoPresentanteEmail;

	private String _ValorDefectoPresentanteSolicitaRetencion;

	private float _FactorResolucionX;

	private float _FactorResolucionY;

	private boolean _MostrarMensajeAlEncriptarEnLegalizacion;

	private String _PoliticaPrivacidadAceptada;

	private boolean _EsPrimeraEjecucionDeLaVersion;

	public ConfiguracionC() {

		inicializa();

	}

	public String get_DesarrolloUrlXmlCentral() {

		return _DesarrolloUrlXmlCentral;

	}

	public void set_DesarrolloUrlXmlCentral(String _DesarrolloUrlXmlCentral) {

		this._DesarrolloUrlXmlCentral = _DesarrolloUrlXmlCentral;

	}

	public void inicializa() {

		String os = System.getProperty("os.name").toLowerCase();

		String strRuta = "";

		if (os.contains("win")) {

			strRuta = System.getenv("UserProfile"); // usuario

		} else if ((os.contains("nix") || os.contains("nux"))) {

			strRuta = System.getenv("HOME");

		}

		_PathRaiz = strRuta;

		_CentralUrlSetup = "";

		_ValorDefectoCodigoRegistro = "";

		_ValorDefectoCodigoProvincia = "";

		_DesarrolloUrlXmlCentral = "";

		_ValorDefectoPresentanteNombre = "";

		_ValorDefectoPresentanteApellido1 = "";

		_ValorDefectoPresentanteApellido2 = "";

		_ValorDefectoPresentanteNif = "";

		_ValorDefectoPresentanteDomicilio = "";

		_ValorDefectoPresentanteCiudad = "";

		_ValorDefectoPresentanteCodigoPostal = "";

		_ValorDefectoPresentanteCodigoProvincia = "";

		_ValorDefectoPresentanteTelefono = "";

		_ValorDefectoPresentanteFax = "";

		_ValorDefectoPresentanteEmail = "";

		_ValorDefectoPresentanteSolicitaRetencion = "";

		_FactorResolucionX = 0;

		_FactorResolucionY = 0;

		_MostrarMensajeAlEncriptarEnLegalizacion = _kValorDefectoMostrarMensajeAlEncriptarEnLegalizacion;

		_PoliticaPrivacidadAceptada = "";

		_CentralContenidoVersiones = "";

		_EsPrimeraEjecucionDeLaVersion = false;

	}

	public boolean isLogActivado() {

		return _LogActivado;

	}

	public void setLogActivado(boolean logActivado) {

		this._LogActivado = logActivado;

	}

	/**
	 * 
	 * 
	 * 
	 * Url absoluta al xml de configuración central
	 * 
	 * 
	 * 
	 */

	public String getUrlXmlCentral() {

		_DesarrolloUrlXmlCentral = "";

		if (Formato.ValorNulo(_DesarrolloUrlXmlCentral)) {

			return _kUrlXmlCentral;

		} else {

			return _DesarrolloUrlXmlCentral;

		}

	}

	/**
	 * 
	 * 
	 * 
	 * Url absoluta al xml de configuración central
	 * 
	 * 
	 * 
	 */

	public boolean isEnDesarrollo() {

		return Formato.ValorNulo(_DesarrolloUrlXmlCentral);

	}

	/**
	 * 
	 * 
	 * 
	 * Version del ensamblado
	 * 
	 * 
	 * 
	 */

	public static String getVersion() {

		try (InputStream input = Main.class.getClassLoader().getResourceAsStream("version.properties")) {

			Properties prop = new Properties();

			prop.load(input);

			String version = prop.getProperty("version");

			return version;

		} catch (IOException ex) {

			ex.printStackTrace();

			return "";

		}

	}

	/**
	 * 
	 * 
	 * 
	 * Version que figura en el xml de configuracion central (atributo version)
	 * 
	 * 
	 * 
	 */

	private String getVersionEnXmlConfiguracion() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(new File(_FicheroConfiguracion));

			XPathFactory xPathfactory = XPathFactory.newInstance();

			XPath xpath = xPathfactory.newXPath();

			XPathExpression expr = (XPathExpression) xpath.compile(String.format("/%s", _kNodoPrincipal));

			Node xmlNodo = (Node) expr.evaluate(xmlDoc, (short) 0, XPathConstants.NODE);

			if (xmlNodo != null) {

				NamedNodeMap attributes = xmlNodo.getAttributes();

				Node versionAttribute = attributes.getNamedItem("version");

				if (versionAttribute != null) {

					return versionAttribute.getNodeValue();

				}

			}

		} catch (Exception e) {

		}

		return "1.0.0"; // Valor predeterminado en caso de error

	}

	/**
	 * 
	 * 
	 * 
	 * Path del directorio de trabajo (C:\Users\Usuario\.Legalia2)
	 * 
	 * 
	 * 
	 */

	public String getPathTrabajo() {

		return _PathTrabajo;

	}

	// borrar luego

	public String getPathRaiz() {

		return _PathRaiz;

	}

	public void setPathRaiz(String PathRaiz) {

		this._PathRaiz = PathRaiz;

	}

	/**
	 * 
	 * 
	 * 
	 * Path del directorio auxiliar (C:\Users\Usuario\.Legalia2\DirAux)
	 * 
	 * 
	 * 
	 */

	public static String getPathAuxiliar() {

		return _PathAuxiliar;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo AplicarNuevasReglasAVersionLegalia del xml de
	 * 
	 * 
	 * 
	 * configuración
	 * 
	 * 
	 * 
	 */

	public boolean isAplicarNuevasReglasAVersionLegalia() {

		return _AplicarNuevasReglasAVersionLegalia;

	}

	public void setAplicarNuevasReglasAVersionLegalia(boolean value) {

		_AplicarNuevasReglasAVersionLegalia = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo BytesMaximosZip del xml de configuración
	 * 
	 * 
	 * 
	 */

	public long getBytesMaximosZip() {

		return _BytesMaximosZip;

	}

	public void setBytesMaximosZip(long value) {

		_BytesMaximosZip = value;

	}

	public long getBytesAvisoZip() {

		return _BytesAvisoZip;

	}

	public void setBytesAvisoZip(long value) {

		_BytesAvisoZip = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo AdjuntosMaximos del xml de configuración
	 * 
	 * 
	 * 
	 */

	public int getAdjuntosMaximos() {

		return _AdjuntosMaximos;

	}

	public void setAdjuntosMaximos(int value) {

		_AdjuntosMaximos = value;

	}

	/**
	 * 
	 * 
	 * 
	 * True si es la primera ejecución del programa en la cual se ha creado el xml
	 * 
	 * 
	 * 
	 * de configuracion
	 * 
	 * 
	 * 
	 */

	public boolean isEsPrimeraUtilizacion() {

		return _EsPrimeraUtilizacion;

	}

	/**
	 * 
	 * 
	 * 
	 * Idioma configurado
	 * 
	 * 
	 * 
	 */

	public String getIdiomaConfigurado() {

		return _IdiomaConfigurado;

	}

	public void setIdiomaConfigurado(String value) {

		_IdiomaConfigurado = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Path al directorio de los datos. Por defecto (C:\Users\Usuario\Legalia2) que
	 * 
	 * 
	 * 
	 * puede ser configurado por el usuario
	 * 
	 * 
	 * 
	 */

	public String getPathDatos() {

		return _PathDatos;

	}

	public void setPathDatos(String value) {

		_PathDatos = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo UrlPortal del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getUrlPortal() {

		return _UrlPortal;

	}

	public void setUrlPortal(String value) {

		_UrlPortal = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoCodigoRegistro del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoCodigoRegistro() {

		return _ValorDefectoCodigoRegistro;

	}

	public void setValorDefectoCodigoRegistro(String value) {

		_ValorDefectoCodigoRegistro = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoCodigoProvincia del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoCodigoProvincia() {

		return _ValorDefectoCodigoProvincia;

	}

	public void setValorDefectoCodigoProvincia(String value) {

		_ValorDefectoCodigoProvincia = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteNombre del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteNombre() {

		return _ValorDefectoPresentanteNombre;

	}

	public void setValorDefectoPresentanteNombre(String value) {

		_ValorDefectoPresentanteNombre = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteApellido1 del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteApellido1() {

		return _ValorDefectoPresentanteApellido1;

	}

	public void setValorDefectoPresentanteApellido1(String value) {

		_ValorDefectoPresentanteApellido1 = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteApellido2 del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteApellido2() {

		return _ValorDefectoPresentanteApellido2;

	}

	public void setValorDefectoPresentanteApellido2(String value) {

		_ValorDefectoPresentanteApellido2 = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteNif del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteNif() {

		return _ValorDefectoPresentanteNif;

	}

	public void setValorDefectoPresentanteNif(String value) {

		_ValorDefectoPresentanteNif = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteDomicilio del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteDomicilio() {

		return _ValorDefectoPresentanteDomicilio;

	}

	public void setValorDefectoPresentanteDomicilio(String value) {

		_ValorDefectoPresentanteDomicilio = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteCiudad del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteCiudad() {

		return _ValorDefectoPresentanteCiudad;

	}

	public void setValorDefectoPresentanteCiudad(String value) {

		_ValorDefectoPresentanteCiudad = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteCodigoPostal del xml de
	 * 
	 * 
	 * 
	 * configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteCodigoPostal() {

		return _ValorDefectoPresentanteCodigoPostal;

	}

	public void setValorDefectoPresentanteCodigoPostal(String value) {

		_ValorDefectoPresentanteCodigoPostal = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteCodigoProvincia del xml de
	 * 
	 * 
	 * 
	 * configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteCodigoProvincia() {

		return _ValorDefectoPresentanteCodigoProvincia;

	}

	public void setValorDefectoPresentanteCodigoProvincia(String value) {

		_ValorDefectoPresentanteCodigoProvincia = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteTelefono del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteTelefono() {

		return _ValorDefectoPresentanteTelefono;

	}

	public void setValorDefectoPresentanteTelefono(String value) {

		_ValorDefectoPresentanteTelefono = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteFax del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteFax() {

		return _ValorDefectoPresentanteFax;

	}

	public void setValorDefectoPresentanteFax(String value) {

		_ValorDefectoPresentanteFax = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteEmail del xml de configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteEmail() {

		return _ValorDefectoPresentanteEmail;

	}

	public void setValorDefectoPresentanteEmail(String value) {

		_ValorDefectoPresentanteEmail = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo ValorDefectoPresentanteSolicitaRetencion del xml de
	 * 
	 * 
	 * 
	 * configuración
	 * 
	 * 
	 * 
	 */

	public String getValorDefectoPresentanteSolicitaRetencion() {

		return _ValorDefectoPresentanteSolicitaRetencion;

	}

	public void setValorDefectoPresentanteSolicitaRetencion(String value) {

		_ValorDefectoPresentanteSolicitaRetencion = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo FactorResolucionX del xml de configuración
	 * 
	 * 
	 * 
	 */

	// quiza se quite

	public float getFactorResolucionX() {

		return _FactorResolucionX;

	}

	public void setFactorResolucionX(float value) {

		_FactorResolucionX = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo FactorResolucionY del xml de configuración
	 * 
	 * 
	 * 
	 */

	public float getFactorResolucionY() {

		return _FactorResolucionY;

	}

	public void setFactorResolucionY(float value) {

		_FactorResolucionY = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde a la constante _kBytesFicheroParaIndicarProgreso
	 * 
	 * 
	 * 
	 */

	public long getBytesFicheroParaIndicarProgreso() {

		return _kBytesFicheroParaIndicarProgreso;

	}

	/**
	 * 
	 * 
	 * 
	 * Corresponde al nodo MostrarMensajeAlEncriptarEnLegalizacion del xml de
	 * 
	 * 
	 * 
	 * configuración
	 * 
	 * 
	 * 
	 */

	public boolean isMostrarMensajeAlEncriptarEnLegalizacion() {

		return _MostrarMensajeAlEncriptarEnLegalizacion;

	}

	public void setMostrarMensajeAlEncriptarEnLegalizacion(boolean value) {

		_MostrarMensajeAlEncriptarEnLegalizacion = value;

	}

	/**
	 * 
	 * 
	 * 
	 * Código de aplicación en el servicio de envío
	 * 
	 * 
	 * 
	 */

	public String getServicioEnvioCodigoAplicacion() {

		return kServicioEnvioCodigoAplicacion;

	}

	public String getServicioEnvioTipoSolicitud() {

		return kServicioEnvioTipoSolicitud;

	}

	/**
	 * 
	 * 
	 * 
	 * Politica privacidad aceptada
	 * 
	 * 
	 * 
	 */

	public String getPoliticaPrivacidadAceptada() {

		return _PoliticaPrivacidadAceptada;

	}

	private List<sMunicipiosCentral> _listamunicipioscentral = null;

	/**
	 * 
	 * 
	 * 
	 * Lista de municipios central
	 * 
	 * 
	 * 
	 */

	public List<sMunicipiosCentral> getListaMunicipiosCentral() {

		return _listamunicipioscentral;

	}

	/**
	 * 
	 * 
	 * 
	 * Forma una cadena con todas las variables de configuracion y sus valores.
	 * 
	 * 
	 * 
	 * Usada en desarrollo.
	 * 
	 * 
	 * 
	 */

	public String getInformacion() {

		StringBuilder cad = new StringBuilder();

		cad.append("Idioma: ").append(_IdiomaConfigurado).append("\n\n");

		cad.append("Es primera utilización: ").append(_EsPrimeraUtilizacion).append("\n\n");

		cad.append("Path datos: ").append(_PathDatos).append("\n\n");

		cad.append("Versión central: ").append(_CentralVersion).append("\n");

		cad.append("Versión local: ").append(getVersion()).append("\n\n");

		cad.append("Central, validar version legalia anterior: ").append(_CentralAplicarNuevasReglasAVersionLegalia)

				.append("\n");

		cad.append("Validar version legalia anterior: ").append(_AplicarNuevasReglasAVersionLegalia).append("\n\n");

		cad.append("Central bytes máximos Zip: ").append(_CentralBytesMaximosZip).append("\n");

		cad.append("Bytes máximos Zip: ").append(_BytesMaximosZip).append("\n\n");

		cad.append("Central bytes aviso Zip: ").append(_CentralBytesAvisoZip).append("\n");

		cad.append("Bytes aviso Zip: ").append(_BytesAvisoZip).append("\n\n");

		cad.append("Central adjuntos máximos: ").append(_CentralAdjuntosMaximos).append("\n");

		cad.append("Adjuntos máximos: ").append(_AdjuntosMaximos).append("\n\n");

		cad.append("UrlPortal: ").append(_UrlPortal).append("\n\n");

		cad.append("Log activado: ").append(_LogActivado).append("\n\n");

		cad.append("FactorResolucionX: ").append(Float.toString(_FactorResolucionX)).append("\n");

		cad.append("FactorResolucionY: ").append(Float.toString(_FactorResolucionY)).append("\n");

		return cad.toString();

	}

	// Para hacer un nuevo xmlDoc

	private Document newDocument() throws ParserConfigurationException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		return dBuilder.newDocument();

	}

	public boolean GuardaConfiguracion() {

		try {

			ManejoXml mXml = new ManejoXml();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document xmlDoc = dBuilder.newDocument();

			if (!creaConfiguracionSiNoExiste()) {

				return false;

			}

			mXml.CrearXmlSiNoExiste(_FicheroConfiguracion, _kNodoPrincipal);

			if (!mXml.AbrirArchivoXML(xmlDoc, _FicheroConfiguracion)) {

				return false;

			}

			File archivoXml = new File(_FicheroConfiguracion);

			xmlDoc = dBuilder.parse(archivoXml);

			Element legaliaNode = (Element) xmlDoc.getElementsByTagName(_kNodoLegalia2).item(0);

			legaliaNode.getElementsByTagName(_KNodoLogActivado).item(0).setTextContent(String.valueOf(_LogActivado));

			legaliaNode.getElementsByTagName(_kNodoAplicarNuevasReglasAVersionLegalia).item(0)

					.setTextContent(String.valueOf(_AplicarNuevasReglasAVersionLegalia));

			legaliaNode.getElementsByTagName(_kNodoBytesMaximosZip).item(0)

					.setTextContent(String.valueOf(_BytesMaximosZip));

			legaliaNode.getElementsByTagName(_kNodoBytesAvisoZip).item(0)

					.setTextContent(String.valueOf(_BytesAvisoZip));

			legaliaNode.getElementsByTagName(_kNodoAdjuntosMaximos).item(0)

					.setTextContent(String.valueOf(_AdjuntosMaximos));

			legaliaNode.getElementsByTagName(_kNodoPathDatos).item(0).setTextContent(_PathDatos);

			legaliaNode.getElementsByTagName(_kNodoIdioma).item(0).setTextContent(_IdiomaConfigurado);

			legaliaNode.getElementsByTagName(_kNodoUrlPortal).item(0).setTextContent(_UrlPortal);

			legaliaNode.getElementsByTagName(_kNodoValorDefectoCodigoRegistro).item(0)

					.setTextContent(_ValorDefectoCodigoRegistro);

			legaliaNode.getElementsByTagName(_kNodoValorDefectoCodigoProvincia).item(0)

					.setTextContent(_ValorDefectoCodigoProvincia);

			legaliaNode.getElementsByTagName(_kNodoPoliticaPrivacidadAceptada).item(0)

					.setTextContent(String.valueOf(_PoliticaPrivacidadAceptada));

			Element presentanteNode = (Element) legaliaNode.getElementsByTagName(_kNodoValoresDefectoPresentante)

					.item(0);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteNombre).item(0)

					.setTextContent(_ValorDefectoPresentanteNombre);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteApellido1).item(0)

					.setTextContent(_ValorDefectoPresentanteApellido1);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteApellido2).item(0)

					.setTextContent(_ValorDefectoPresentanteApellido2);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteNif).item(0)

					.setTextContent(_ValorDefectoPresentanteNif);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteDomicilio).item(0)

					.setTextContent(_ValorDefectoPresentanteDomicilio);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteCodigoPostal).item(0)

					.setTextContent(_ValorDefectoPresentanteCodigoPostal);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteCiudad).item(0)

					.setTextContent(_ValorDefectoPresentanteCiudad);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteProvincia).item(0)

					.setTextContent(_ValorDefectoPresentanteCodigoProvincia);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteTelefono).item(0)

					.setTextContent(_ValorDefectoPresentanteTelefono);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteFax).item(0)

					.setTextContent(_ValorDefectoPresentanteFax);

			presentanteNode.getElementsByTagName(_KNodoValorDefectoPresentanteEmail).item(0)

					.setTextContent(_ValorDefectoPresentanteEmail);

			presentanteNode.getElementsByTagName(_kNodoValorDefectoPresentanteSolicitaRetencion).item(0)

					.setTextContent(_ValorDefectoPresentanteSolicitaRetencion);

			legaliaNode.getElementsByTagName(_kNodoMostrarMensajeAlEncriptarEnLegalizacion).item(0)

					.setTextContent(String.valueOf(_MostrarMensajeAlEncriptarEnLegalizacion));

			trimWhitespace(xmlDoc.getElementsByTagName(_kNodoPrincipal).item(0));

			mXml.saveXML(xmlDoc, _FicheroConfiguracion);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public static void trimWhitespace(Node node) {

		NodeList children = node.getChildNodes();

		for (int i = 0; i < children.getLength(); ++i) {

			Node child = children.item(i);

			if (child.getNodeType() == Node.TEXT_NODE) {

				child.setTextContent(child.getTextContent().trim());

			}

			trimWhitespace(child);

		}

	}

	private boolean creaConfiguracionSiNoExiste() {

		try {

			ManejoXml mXml = new ManejoXml();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document xmlDoc = dBuilder.newDocument();

			boolean creadoAlgunNodo = false;

			Element xmlNodo;

			File archivoConfiguracion = new File(_FicheroConfiguracion);

			if (!archivoConfiguracion.exists()) {

				_EsPrimeraUtilizacion = true;

			}

			mXml.CrearXmlSiNoExiste(_FicheroConfiguracion, _kNodoPrincipal);

			if (!mXml.AbrirArchivoXML(xmlDoc, _FicheroConfiguracion)) {

				return false;

			}

			xmlDoc = dBuilder.parse(archivoConfiguracion);

			if (mXml.creaNodoSiNoExiste(xmlDoc, null, _kNodoPrincipal, "test")) {

				creadoAlgunNodo = true;

			}

			xmlNodo = (Element) xmlDoc.getElementsByTagName(_kNodoPrincipal).item(0);

			if (mXml.creaAtributoNodoSiNoExiste(xmlDoc, xmlNodo, "version", "2.0.0")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoLegalia2, "")) {

				creadoAlgunNodo = true;

			}

			xmlNodo = (Element) xmlDoc.getElementsByTagName(_kNodoLegalia2).item(0);

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoLogActivado,

					String.valueOf(_kValorDefectoLogActivado))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoAplicarNuevasReglasAVersionLegalia,

					String.valueOf(_kValorDefectoAplicarNuevasReglasAVersionLegalia))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoBytesMaximosZip,

					String.valueOf(_kValorDefectoBytesMaximosZip))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoBytesAvisoZip,

					String.valueOf(_kValorDefectoBytesAvisoZip))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoAdjuntosMaximos,

					String.valueOf(_kValorDefectoAdjuntosMaximos))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoIdioma, _kValorDefectoIdioma)) {

				creadoAlgunNodo = true;

			}

			String ruta = "";

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				ruta = _PathRaiz + "\\" + _kValorDefectoNombreDirectorioDatos;

			} else if ((os.contains("nix") || os.contains("nux"))) {

				ruta = _PathRaiz + "/" + _kValorDefectoNombreDirectorioDatos;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoPathDatos, ruta)) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoUrlPortal, _kValorDefectoUrlPortal)) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoValorDefectoCodigoRegistro, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoValorDefectoCodigoProvincia, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoPoliticaPrivacidadAceptada, "")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoMostrarMensajeAlEncriptarEnLegalizacion,

					String.valueOf(_kValorDefectoMostrarMensajeAlEncriptarEnLegalizacion))) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoValoresDefectoPresentante, " ")) {

				creadoAlgunNodo = true;

			}

			xmlNodo = (Element) xmlDoc.getElementsByTagName(_kNodoValoresDefectoPresentante).item(0);

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteNombre, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteApellido1, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteApellido2, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteNif, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteDomicilio, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteCodigoPostal, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteCiudad, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteProvincia, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteTelefono, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteFax, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _KNodoValorDefectoPresentanteEmail, " ")) {

				creadoAlgunNodo = true;

			}

			if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, _kNodoValorDefectoPresentanteSolicitaRetencion, " ")) {

				creadoAlgunNodo = true;

			}

			if (validarManualmenteDesarrollo(false)) {

				if (mXml.creaNodoSiNoExiste(xmlDoc, xmlNodo, "DesarrolloUrlXmlCentral",

						"http://127.0.0.1:5500/VersionesCommerceApp.xml")) {

					creadoAlgunNodo = true;

				}
			}

			if (creadoAlgunNodo) {

				mXml.saveXML(xmlDoc, _FicheroConfiguracion);

			}

			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

			return false;

		}

	}

	public String obtenerURLDesarrolloXMLCentral() throws ParserConfigurationException, SAXException, IOException {

		ManejoXml mXml = new ManejoXml();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document xmlDoc = dBuilder.newDocument();

		String rutaNodo;

		StringBuilder valorNodo = new StringBuilder();

		_ErrorCargaConfiguracion = "";

		if (!creaConfiguracionSiNoExiste()) {

			return "";

		}

		File archivoConfiguracion = new File(_FicheroConfiguracion);

		xmlDoc = dBuilder.parse(archivoConfiguracion);

		rutaNodo = "DesarrolloUrlXmlCentral";

		String valorurldesarrollo = "";

		if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

			valorurldesarrollo = valorNodo.toString();

		}

		return valorurldesarrollo;

	}

	public boolean cargaConfiguracion() {

		boolean configuracionCorrecta = false;

		try {

			ManejoXml mXml = new ManejoXml();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document xmlDoc = dBuilder.newDocument();

			String rutaNodo;

			StringBuilder valorNodo = new StringBuilder();

			configuracionCorrecta = false;

			_ErrorCargaConfiguracion = "";

			if (!creaConfiguracionSiNoExiste()) {

				return false;

			}

			File archivoConfiguracion = new File(_FicheroConfiguracion);

			xmlDoc = dBuilder.parse(archivoConfiguracion);

			// LogActivado

			rutaNodo = _KNodoLogActivado;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_LogActivado = Boolean.parseBoolean(valorNodo.toString());

			} else {

				_LogActivado = _kValorDefectoLogActivado;

			}

			// Aplicar Nuevas Reglas A VersionLegalia

			rutaNodo = _kNodoAplicarNuevasReglasAVersionLegalia;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_AplicarNuevasReglasAVersionLegalia = Boolean.parseBoolean(valorNodo.toString());

			} else {

				_AplicarNuevasReglasAVersionLegalia = _kValorDefectoAplicarNuevasReglasAVersionLegalia;

			}

			// BytesMaximosZip

			rutaNodo = _kNodoBytesMaximosZip;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_BytesMaximosZip = Long.parseLong(valorNodo.toString());

			} else {

				_BytesMaximosZip = _kValorDefectoBytesMaximosZip;

			}

			// BytesAvisoZip

			rutaNodo = _kNodoBytesAvisoZip;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_BytesAvisoZip = Long.parseLong(valorNodo.toString());

			} else {

				_BytesAvisoZip = _kValorDefectoBytesAvisoZip;

			}

			// Adjuntos máximos

			rutaNodo = _kNodoAdjuntosMaximos;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_AdjuntosMaximos = Integer.parseInt(valorNodo.toString());

			} else {

				_AdjuntosMaximos = _kValorDefectoAdjuntosMaximos;

			}

			// Idioma

			rutaNodo = _kNodoIdioma;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_IdiomaConfigurado = valorNodo.toString();

			} else {

				_IdiomaConfigurado = _kValorDefectoIdioma;

			}

			// Directorio datos

			rutaNodo = _kNodoPathDatos;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_PathDatos = valorNodo.toString();

			} else {

				_PathDatos = _PathRaiz + "/" + "_kValorDefectoNombreDirectorioDatos";

			}

			// Url portal

			rutaNodo = _kNodoUrlPortal;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_UrlPortal = valorNodo.toString();

			} else {

				_UrlPortal = _kValorDefectoUrlPortal;

			}

			rutaNodo = _kNodoDesarrolloUrlXmlCentral;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_DesarrolloUrlXmlCentral = valorNodo.toString();

			} else {

				// No es ningún error,

				_DesarrolloUrlXmlCentral = "";

			}

			// Valor defecto código Registro

			rutaNodo = _kNodoValorDefectoCodigoRegistro;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_ValorDefectoCodigoRegistro = valorNodo.toString();

			}

			// Valor defecto código provincia

			rutaNodo = _kNodoValorDefectoCodigoProvincia;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_ValorDefectoCodigoProvincia = valorNodo.toString();

			}

			// MostrarMensajeAlEncriptarEnLegalizacion

			rutaNodo = _kNodoMostrarMensajeAlEncriptarEnLegalizacion;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_MostrarMensajeAlEncriptarEnLegalizacion = Boolean.parseBoolean(valorNodo.toString());

			} else {

				_MostrarMensajeAlEncriptarEnLegalizacion = _kValorDefectoMostrarMensajeAlEncriptarEnLegalizacion;

			}

			// PoliticaPrivacidadAceptada

			rutaNodo = _kNodoPoliticaPrivacidadAceptada;

			if (mXml.LeeNodo(xmlDoc, rutaNodo, valorNodo)) {

				_PoliticaPrivacidadAceptada = valorNodo.toString();

			}

			// Valores defecto datos del presentante

			rutaNodo = _kNodoValoresDefectoPresentante;

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteNombre, valorNodo)) {

				_ValorDefectoPresentanteNombre = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteApellido1, valorNodo)) {

				_ValorDefectoPresentanteApellido1 = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteApellido2, valorNodo)) {

				_ValorDefectoPresentanteApellido2 = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteNif, valorNodo)) {

				_ValorDefectoPresentanteNif = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteDomicilio, valorNodo)) {

				_ValorDefectoPresentanteDomicilio = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteCodigoPostal, valorNodo)) {

				_ValorDefectoPresentanteCodigoPostal = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteCiudad, valorNodo)) {

				_ValorDefectoPresentanteCiudad = valorNodo.toString();

			}

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteProvincia, valorNodo)) {

				_ValorDefectoPresentanteCodigoProvincia = valorNodo.toString();

			}

			// Lee el nodo y asigna el valor a _ValorDefectoPresentanteTelefono

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteTelefono, valorNodo)) {

				_ValorDefectoPresentanteTelefono = valorNodo.toString();

			}

			// Lee el nodo y asigna el valor a _ValorDefectoPresentanteFax

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteFax, valorNodo)) {

				_ValorDefectoPresentanteFax = valorNodo.toString();

			}

			// Lee el nodo y asigna el valor a _ValorDefectoPresentanteEmail

			if (mXml.LeeNodo(xmlDoc, _KNodoValorDefectoPresentanteEmail, valorNodo)) {

				_ValorDefectoPresentanteEmail = valorNodo.toString();

			}

			// Lee el nodo y asigna el valor a _ValorDefectoPresentanteSolicitaRetencion

			if (mXml.LeeNodo(xmlDoc, _kNodoValorDefectoPresentanteSolicitaRetencion, valorNodo)) {

				_ValorDefectoPresentanteSolicitaRetencion = valorNodo.toString();

			}

			// Verifica si no hay errores durante la carga de configuración

			if (_ErrorCargaConfiguracion.equals("")) {

				_ConfiguracionCorrecta = true;

			}

			trimWhitespace(xmlDoc.getElementsByTagName(_kNodoPrincipal).item(0));

			mXml.saveXML(xmlDoc, _FicheroConfiguracion);

			// Asigna el valor de _ConfiguracionCorrecta a CargaConfiguracion

		} catch (Exception e) {

			e.printStackTrace();

		}

		return _ConfiguracionCorrecta;

	}

	public boolean iniciar() {

		boolean resultado = false;

		try {

			if (!CompruebaPathTrabajo()) {

				if (!MGeneral.ModoBatch) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.DirectorioTrabajoNoValido, _PathTrabajo, "",

							"");

					if (System.getProperty("os.version").startsWith("6")) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EjecutarComoAdministrador, "", "", "");

					}

				} else {

					System.out.println(MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.DirectorioTrabajoNoValido,

							_PathTrabajo, "", ""));

				}

				return resultado;

			}

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				_FicheroConfiguracion = _PathTrabajo + "\\" + _kNombreFicheroConfiguracion;

			} else if ((os.contains("nix") || os.contains("nux"))) {

				_FicheroConfiguracion = _PathTrabajo + "/" + _kNombreFicheroConfiguracion;

			}

			if (!MGeneral.ModoBatch && !compruebaPathAuxiliar()) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.DirectorioAuxiliarNoValido, _PathTrabajo, "", "");

				if (System.getProperty("os.version").startsWith("6")) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EjecutarComoAdministrador, "", "", "");

				}

				return resultado;

			}

			if (!cargaConfiguracion()) {

				if (!MGeneral.ModoBatch) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorSeHaProducido, _ErrorCargaConfiguracion,

							"", "");

				} else {

					System.out.println(MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.ErrorSeHaProducido,

							_ErrorCargaConfiguracion, "", ""));

				}

				return resultado;

			}

			if (!MGeneral.ModoBatch && !actualizaVersionesPendientes()) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorAlActualizarVersion, "", "", "");

				return resultado;

			}

			if (!MGeneral.ModoBatch && !compruebaPathDatos()) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.DirectorioDatosNoValido, _PathDatos, "", "");

				if (System.getProperty("os.version").startsWith("6")) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EjecutarComoAdministrador, "", "", "");

				}

				return resultado;

			}

			resultado = true;

		} catch (Exception ex) {

			if (!MGeneral.ModoBatch) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			} else {

				System.out.println(

						MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", ""));

			}

			ex.printStackTrace();

		}

		return resultado;

	}

	public boolean actualizaVersionesPendientes() {

		try {

			String versionEnXml = getVersionEnXmlConfiguracion();

			MiVersion vEnXml = new MiVersion(versionEnXml);

			boolean haActualizado = false;

			if (vEnXml.compareTo(new MiVersion("1.0.0")) < 0) {

				if (!version_1_0_0())

					throw new Exception("Error al actualizar a la versión 1.0.0");

				if (!grabaVersionConfiguracion("1.0.0"))

					throw new Exception("Error al actualizar la versión del XML a 1.0.0");

				haActualizado = true;

			}

			if (vEnXml.compareTo(new MiVersion("1.0.3")) < 0) {

				if (!version_1_0_3())

					throw new Exception("Error al actualizar a la versión 1.0.3");

				if (!grabaVersionConfiguracion("1.0.3"))

					throw new Exception("Error al actualizar la versión del XML a 1.0.3");

				haActualizado = true;

			}

			if (vEnXml.compareTo(new MiVersion("1.2.4")) < 0) {

				if (!version_1_2_4())

					throw new Exception("Error al actualizar a la versión 1.2.4");

				if (!grabaVersionConfiguracion("1.2.4"))

					throw new Exception("Error al actualizar la versión del XML a 1.2.4");

				haActualizado = true;

			}

			if (vEnXml.compareTo(new MiVersion("1.3.2")) < 0) {

				if (!version_1_3_2())

					throw new Exception("Error al actualizar a la versión 1.3.2");

				if (!grabaVersionConfiguracion("1.3.2"))

					throw new Exception("Error al actualizar la versión del XML a 1.3.2");

				haActualizado = true;

			}

			if (haActualizado) {

				if (!cargaConfiguracion())

					throw new Exception(_ErrorCargaConfiguracion);

			}

			if (!grabaVersionConfiguracion(getVersion()))

				throw new Exception("Error al actualizar la versión del XML a " + getVersion());

			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return false;

	}

	public boolean version_1_0_0() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			xmlNodo = xmlDoc.getElementsByTagName(_kNodoLegalia2).item(0);

			// MXml.CreaNodoSiNoExiste(xmlDoc, xmlNodo, _knodo, "valor nodo", true);

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean version_1_3_2() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			// En esta versión se cambia la fuente para los códigos de barras

			// Se marca a False el nodo que indica si la fuente ya ha sido registrada en

			// equipos de XP

			// xmlNodo =
			// xmlDoc.getElementsByTagName(_kNodoRegistradaFuenteCodigoBarrasXP).item(0);

			xmlNodo.setTextContent("False");

			// MXml.CreaNodoSiNoExiste(xmlDoc, xmlNodo, _knodo, "valor nodo", true);

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			// Hay que actualizar el valor de las propiedades que se han actualizado con la

			// version

			// _RegistradaFuenteCodigoBarrasXP = false;

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean version_1_2_4() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			// En esta versión se cambia la fuente para los códigos de barras

			// Se marca a False el nodo que indica si la fuente ya ha sido registrada en

			// equipos de XP

			// xmlNodo =
			// xmlDoc.getElementsByTagName(_kNodoRegistradaFuenteCodigoBarrasXP).item(0);

			xmlNodo.setTextContent("False");

			// MXml.CreaNodoSiNoExiste(xmlDoc, xmlNodo, _knodo, "valor nodo", true);

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			// Hay que actualizar el valor de las propiedades que se han actualizado con la

			// version

			// _RegistradaFuenteCodigoBarrasXP = false;

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean version_1_0_3() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			// En esta versión se cambia la fuente para los códigos de barras

			// Se marca a False el nodo que indica si la fuente ya ha sido registrada en

			// equipos de XP

			// xmlNodo =
			// xmlDoc.getElementsByTagName(_kNodoRegistradaFuenteCodigoBarrasXP).item(0);

			xmlNodo.setTextContent("False");

			// MXml.CreaNodoSiNoExiste(xmlDoc, xmlNodo, _knodo, "valor nodo", true);

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			// Hay que actualizar el valor de las propiedades que se han actualizado con la

			// version

			// _RegistradaFuenteCodigoBarrasXP = false;

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean Version_1_2_4() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			// Desaparece el nodo ComprobarActualizaciones

			xmlNodo = xmlDoc.getElementsByTagName(_kNodoLegalia2).item(0);

			/*
			 * 
			 * 
			 * 
			 * Node xmlNodo2 =
			 * 
			 * 
			 * 
			 * xmlNodo.getElementsByTagName(_kNodoComprobarActualizaciones).item(0); if
			 * 
			 * 
			 * 
			 * (xmlNodo2 != null) xmlNodo.removeChild(xmlNodo2);
			 * 
			 * 
			 * 
			 */

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean Version_1_3_2() {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(_FicheroConfiguracion);

			Node xmlNodo = xmlDoc.getDocumentElement();

			// Actualización de la url del portal

			xmlNodo = xmlDoc.getElementsByTagName(_kNodoUrlPortal).item(0);

			xmlNodo.setTextContent(_kValorDefectoUrlPortal);

			xmlDoc.normalize();

			// xmlDoc.save(_FicheroConfiguracion);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean grabaVersionConfiguracion(String queVersion) {

		try {

			// Configurar el parser de XML

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Parsear el archivo de configuración XML

			Document doc = dBuilder.parse(_FicheroConfiguracion);

			// Obtener el elemento <Configuracion>

			Element configuracionElement = (Element) doc.getElementsByTagName("Configuracion").item(0);

			// Actualizar el valor del atributo 'version'

			configuracionElement.setAttribute("version", queVersion);

			// Guardar los cambios en el archivo XML

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(_FicheroConfiguracion);

			transformer.transform(source, result);

			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

			return false;

		}

	}

	private boolean CompruebaPathTrabajo() {

		boolean resultado = false;

		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {

			_PathTrabajo = _PathRaiz + "\\" + _kNombreDirectorioTrabajo;

		} else if ((os.contains("nix") || os.contains("nux"))) {

			_PathTrabajo = _PathRaiz + "/" + _kNombreDirectorioTrabajo;

		}

		if (!Ficheros.DirectorioComprueba(_PathTrabajo)) {

			return false;

		}

		File oDir = new File(_PathTrabajo);

		if (!oDir.isHidden()) {

			oDir.setWritable(true);

			oDir.setExecutable(true);

			oDir.setReadable(true);

			// Directorio oculto

			try {

				Path path = Paths.get(_PathRaiz, _kNombreDirectorioTrabajo);

				Boolean hidden = (Boolean) Files.getAttribute(path, "dos:hidden", LinkOption.NOFOLLOW_LINKS);

				if (hidden != null && !hidden) {

					Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);

				}

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		resultado = true;

		return resultado;

	}

	private static boolean compruebaPathAuxiliar() {

		boolean resultado = false;

		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {

			_PathAuxiliar = _PathTrabajo + "\\" + _kNombreDirectorioAuxiliar;

		} else if ((os.contains("nix") || os.contains("nux"))) {

			_PathAuxiliar = _PathTrabajo + "/" + _kNombreDirectorioAuxiliar;

		}

		if (!Ficheros.DirectorioComprueba(_PathAuxiliar)) {

			return false;

		}

		resultado = true;

		return resultado;

	}

	private boolean compruebaPathDatos() {

		boolean resultado = false;

		if (!Ficheros.DirectorioComprueba(_PathDatos)) {

			return false;

		}

		resultado = true;

		return resultado;

	}

	public void actualizarVersion() {

		String carpetaDestino;

		StringBuilder contenido = new StringBuilder();

		boolean obligatoria[] = { false };

		try {

			// Estado(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.ComprobandoActualizaciones));

			if (!cargaConfiguracionCentral()) {

				return;

			}

			GuardaConfiguracion();

			borraFicherosInstalacionNoValidos();

			if (!hayNuevaVersion()) {

				return;

			}

			obtenerContenidoNuevasVersiones(contenido, obligatoria);

			try {

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Versiones.fxml"));

				Parent versiones = loader.load();

				VersionesController versionesController = loader.getController();

				versionesController.setActualizacionObligatoria(true);

				versionesController.setContenido("");

				versionesController.initialize(null, null);

				Stage stage = new Stage();

				Scene scene = new Scene(versiones);

				stage.initModality(Modality.APPLICATION_MODAL);

				stage.initStyle(StageStyle.UTILITY);

				stage.getIcons().add(new Image(getClass().getResource("/imagenes/IconoColegioR.png").toString()));

				stage.setScene(scene);

				MGeneral.Idioma.cargarIdiomaControles(stage, null);

				stage.toFront();

				stage.showAndWait();

			} catch (Exception e) {

				e.printStackTrace();

			}

			if (!MGeneral.RetornoFormulario) {

				if (obligatoria[0]) {

					cerrarAplicacion();

				}

				return;

			}

			carpetaDestino = _PathTrabajo;

			if (!Ficheros.DirectorioComprueba(carpetaDestino)) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.DirectorioTrabajoNoValido, carpetaDestino, "", "");

				return;

			}

			borraFicherosInstalacionNoValidos();

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				URL urlObject = new URL(_CentralUrlSetupWindows);

				String fileName = new File(urlObject.getFile()).getName();

				String ficDestino = Paths.get(carpetaDestino, fileName).toString();

				Utilidades.downloadFile(_CentralUrlSetupWindows, ficDestino);

				Utilidades.ProcessStartFichero(ficDestino);

			} else if (os.contains("nux")) {

			}

			cerrarAplicacion();

		} catch (Exception ex) {

			ex.printStackTrace();

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

		} finally {

			// Estado("");

		}

	}

	private void cerrarAplicacion() {

		System.exit(0);

	}

	public boolean cargaConfiguracionCentral() {

		try {

			_CentralConfiguracionCorrecta = false;

			_CentralVersion = new StringBuilder();

			URL url = new URL(getUrlXmlCentral());

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(20000); // Timeout en milisegundos

			connection.setRequestMethod("GET");

			connection.connect();

			int responseCode = connection.getResponseCode();

			if (responseCode != HttpURLConnection.HTTP_OK) {

				return false;

			}

			InputStream inputStream = connection.getInputStream();

			Document xmlDoc = null;

			try {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				xmlDoc = dBuilder.newDocument();

				xmlDoc = dBuilder.parse(inputStream);

			} catch (ParserConfigurationException | SAXException e) {

				e.printStackTrace();

			}

			// reader.close();

			inputStream.close();

			if (xmlDoc == null) {

				return false;

			}

			ManejoXml mXml = new ManejoXml();

			_CentralVersion.append("0.0.0");

			mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _kNodoVersionCentral, _CentralVersion);

			StringBuilder valorNodo = new StringBuilder();

			mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _kNodoAplicarNuevasReglasAVersionLegalia,

					valorNodo);

			if (!valorNodo.isEmpty()) {

				_CentralAplicarNuevasReglasAVersionLegalia = Boolean.valueOf(valorNodo.toString());

				_AplicarNuevasReglasAVersionLegalia = _CentralAplicarNuevasReglasAVersionLegalia;

			}

			// Procesar el nodo _kNodoBytesMaximosZip

			if (mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _kNodoBytesMaximosZip, valorNodo)) {

				if (!Formato.ValorNulo(valorNodo)) {

					_CentralBytesMaximosZip = Long.parseLong(valorNodo.toString());

					setBytesMaximosZip(_CentralBytesMaximosZip);

				}

			}

			// Procesar el nodo _kNodoBytesAvisoZip

			if (mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _kNodoBytesAvisoZip, valorNodo)) {

				if (!Formato.ValorNulo(valorNodo)) {

					_CentralBytesAvisoZip = Long.parseLong(valorNodo.toString());

					setBytesAvisoZip(_CentralBytesAvisoZip);

				}

			}

			// Procesar el nodo _kNodoAdjuntosMaximos

			if (mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _kNodoAdjuntosMaximos, valorNodo)) {

				if (!Formato.ValorNulo(valorNodo)) {

					_CentralAdjuntosMaximos = Integer.parseInt(valorNodo.toString());

					setAdjuntosMaximos(_CentralAdjuntosMaximos);

				}

			}

			// Procesar el nodo _kNodoCentralContenidoVersiones

			if (mXml.LeeNodoCentralXMLContenidoVersiones(xmlDoc, _kNodoCentralContenidoVersiones, valorNodo)) {

				if (!Formato.ValorNulo(valorNodo)) {

					_CentralContenidoVersiones = valorNodo.toString();

				}

			}

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				_CentralUrlSetupWindows = "";

				if (mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _KNodoUrlSetupWindows,

						valorNodo)) {

					if (!Formato.ValorNulo(valorNodo)) {

						_CentralUrlSetupWindows = valorNodo.toString();

					}

				}

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				_CentralUrlSetupLinux = "";

				if (mXml.LeeNodoCentralXML(xmlDoc, "/" + _kNodoPrincipal + "/" + _KNodoUrlSetupLinux, valorNodo)) {

					if (!Formato.ValorNulo(valorNodo)) {

						_CentralUrlSetupLinux = valorNodo.toString();

					}

				}

			}

			try {

				NodeList nodeList = xmlDoc.getElementsByTagName(_kNodoPrincipal);

				if (nodeList.getLength() > 0) {

					Element configuracionElement = (Element) nodeList.item(0); // Obtener el primer elemento

					// <Configuracion>

					// Buscar el elemento "Municipios" dentro de "Configuracion"

					NodeList municipiosList = configuracionElement.getElementsByTagName("Municipios");

					if (municipiosList.getLength() > 0) {

						Element municipiosElement = (Element) municipiosList.item(0); // Obtener el primer elemento

						// <Municipios>

						// Ahora puedes iterar sobre los elementos <Municipio> dentro de <Municipios>

						NodeList municipioList = municipiosElement.getElementsByTagName("Municipio");

						_listamunicipioscentral = new ArrayList<>();

						// Iterar sobre los elementos <Municipio> y agregarlos a la lista

						for (int i = 0; i < municipioList.getLength(); i++) {

							Node node = municipioList.item(i);

							if (node.getNodeType() == Node.ELEMENT_NODE) {

								Element element = (Element) node;

								sMunicipiosCentral mun = new sMunicipiosCentral();

								mun.CPRO = element.getElementsByTagName("CPRO").item(0).getTextContent();

								mun.Nombre = element.getElementsByTagName("Nom").item(0).getTextContent();

								mun.Operacion = element.getElementsByTagName("Ope").item(0).getTextContent();

								if (mun.Operacion.equals("M")) {

									mun.NombreModificado = element.getElementsByTagName("NomM").item(0)

											.getTextContent();

								}

								_listamunicipioscentral.add(mun);

							}

						}

					}

				}

			} catch (Exception ex) {

				ex.printStackTrace();

			}

			_CentralConfiguracionCorrecta = true;

			return true;

		} catch (IOException ex) {

			ex.printStackTrace();

		}

		return false;

	}

	public boolean hayNuevaVersion() {

		try {

			if (!_CentralConfiguracionCorrecta) {

				return false;

			}

			Version vLocal = Version.parse(getVersion());

			Version vCentral = Version.parse(_CentralVersion.toString());

			if (vCentral.compareTo(vLocal) > 0) {

				return true;

			}

		} catch (Exception ex) {

			// Captura cualquier excepción y no hace nada

			// vLog.LogExcepcion(EspacioFuncion, ex);

		}

		return false;

	}

	private void borraFicherosInstalacionNoValidos() {

		try {

			String carpetaDestino = _PathTrabajo;

			// Borra los ficheros con el patrón _kPatronNombreFicherosSetup

			String[] ficherosOld = new File(carpetaDestino)

					.list((dir, name) -> name.matches(_kPatronNombreFicherosSetup));

			if (ficherosOld != null) {

				for (String fichero : ficherosOld) {

					Files.delete(Paths.get(carpetaDestino, fichero));

				}

			}

			// Borra los ficheros con el patrón _kPatronNombreFicherosMSI

			ficherosOld = new File(carpetaDestino).list((dir, name) -> name.matches(_kPatronNombreFicherosMSI));

			if (ficherosOld != null) {

				for (String fichero : ficherosOld) {

					Files.delete(Paths.get(carpetaDestino, fichero));

				}

			}

		} catch (IOException ex) {

			// Manejar la excepción apropiadamente

		}

	}

	public boolean aceptarPoliticaPrivacidad() {

		try {

			String cad = getVersion() + obtenerIdMaquina();

			MessageDigest sha1Obj = MessageDigest.getInstance("SHA-1");

			byte[] bytesToHash = cad.getBytes(StandardCharsets.UTF_8);

			bytesToHash = sha1Obj.digest(bytesToHash);

			StringBuilder strResult = new StringBuilder();

			for (byte b : bytesToHash) {

				strResult.append(String.format("%02x", b));

			}

			if (!_PoliticaPrivacidadAceptada.equals(strResult.toString())) {

				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PoliticaPrivacidad.fxml"));

				Parent root = fxmlLoader.load();

				PoliticaPrivacidadController politicaPrivacidad = fxmlLoader.getController();

				// configuracionController.setParentController(this);

				Stage stage = new Stage();

				Scene scene = new Scene(root);

				// quitando el maximizar y minimizar

				stage.initStyle(StageStyle.UTILITY);

				// quitando iconos

				stage.getIcons().clear();

				// bloquea la interacción con otras ventanas de la aplicación

				stage.initModality(Modality.APPLICATION_MODAL);

				stage.setScene(scene);

				MGeneral.Idioma.cargarIdiomaControles(stage, null);

				// stage.setTitle("Configuracion");

				stage.toFront();

				stage.showAndWait();

				if (!MGeneral.RetornoFormulario) {

					cerrarAplicacion();

					return false;

				}

				_PoliticaPrivacidadAceptada = strResult.toString();

				GuardaConfiguracion();

				_EsPrimeraEjecucionDeLaVersion = true;

			}

			return true;

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			cerrarAplicacion();

			return false;

		}

	}

	public String obtenerIdMaquina() {

		try {

			String result = "";

			String line;

			Process process = Runtime.getRuntime().exec("wmic cpu get ProcessorId");

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = reader.readLine()) != null) {

				result += line.trim();

			}

			reader.close();

			return result;

		} catch (Exception ex) {

			return "";

		}

	}

	public void obtenerContenidoNuevasVersiones(StringBuilder contenido, boolean[] obligatoria) {

		// falta

		String cadContenidoVersiones = "";

		try {

			contenido.setLength(0);

			obligatoria[0] = false;

			cadContenidoVersiones = _CentralContenidoVersiones;

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = null; // = builder.parse(new InputStream(new StringReader("<" +

			// _kNodoCentralContenidoVersiones + ">" + cadContenidoVersiones + "</" +

			// _kNodoCentralContenidoVersiones + ">")));

			NodeList nodeList = xmlDoc.getElementsByTagName(_kNodoCentralContenidoVersionesContenidoVersion);

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				try {

					String cadVersion = node.getChildNodes().item(0).getTextContent().trim();

					String cadObligatoria = node.getChildNodes().item(1).getTextContent().trim();

					String cadContenido = node.getChildNodes().item(2).getTextContent().trim();

					// Version vLocal = new Version(Version);

					// Version vCentral = new Version(cadVersion);

					/*
					 * 
					 * 
					 * 
					 * if (vCentral.compareTo(vLocal) > 0) { if
					 * 
					 * 
					 * 
					 * (Boolean.parseBoolean(cadObligatoria)) obligatoria[0] = true; if
					 * 
					 * 
					 * 
					 * (contenido.length() > 0)
					 * 
					 * 
					 * 
					 * contenido.append(System.lineSeparator()).append(System.lineSeparator());
					 * 
					 * 
					 * 
					 * contenido.append(cadContenido.replace("\n", System.lineSeparator())); }
					 * 
					 * 
					 * 
					 */

				} catch (Exception ex) {

				}

			}

		} catch (Exception ex) {

		}

	}

	public void obtenerMensajeDeLaVersionActual(StringBuilder mensaje) {

		String cadContenidoVersiones = _CentralContenidoVersiones;

		try {

			mensaje.setLength(0);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document xmlDoc = builder.parse(new InputSource(new StringReader("<" + _kNodoCentralContenidoVersiones + ">"

					+ cadContenidoVersiones + "</" + _kNodoCentralContenidoVersiones + ">")));

			NodeList nodelist = xmlDoc.getElementsByTagName(_kNodoCentralContenidoVersionesContenidoVersion);

			for (int i = 0; i < nodelist.getLength(); i++) {

				Node node = nodelist.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;

					String cadversion = element

							.getElementsByTagName(_kNodoCentralContenidoVersionesContenidoVersionVersion).item(0)

							.getTextContent();

					String cadmensaje = element

							.getElementsByTagName(_kNodoCentralContenidoVersionesContenidoVersionMensaje).item(0)

							.getTextContent();

					Version vLocal = Version.parse(getVersion());

					Version vCentral = Version.parse(_CentralVersion.toString());

					if (vCentral.equals(vLocal)) {

						mensaje.append(reemplazarSecuenciasUnicode(cadmensaje).replace("\n",

								System.getProperty("line.separator")));

						break;

					}

				}

			}

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	public void mostrarMensajeSiEsPrimeraEjecucionDeLaVersion() {

		StringBuilder mensaje = new StringBuilder();

		try {

			// _EsPrimeraEjecucionDeLaVersion se calcula en AceptarPoliticaPrivacidad

			if (!_EsPrimeraEjecucionDeLaVersion)

				return;

			obtenerMensajeDeLaVersionActual(mensaje);

			if (!mensaje.isEmpty()) {

				// frmMensaje

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Mensaje.fxml"));

				Parent frmMensaje = loader.load();

				MensajeController mensajeController = loader.getController();

				mensajeController.set_mensaje(mensaje.toString());

				Stage stage = new Stage();

				Scene scene = new Scene(frmMensaje);

				mensajeController.initialize(null, null);

				// quitando el maximizar y minimizar

				stage.initModality(Modality.APPLICATION_MODAL);

				// bloquea la interacción con otras ventanas de la aplicación

				stage.initStyle(StageStyle.UTILITY);

				// quitando iconos

				stage.getIcons().clear();

				stage.setScene(scene);

				MGeneral.Idioma.cargarIdiomaControles(stage, null);

				stage.showAndWait();

			}

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

		}

	}

	public static String reemplazarSecuenciasUnicode(String texto) {

		Map<String, String> reemplazos = Map.ofEntries(entry("C3", "\u00F3"), entry("C3a", "\u00E1"),

				entry("C3e", "\u00E9"), entry("C3i", "\u00ED"), entry("C3o", "\u00F3"), entry("C3u", "\u00FA"),

				entry("C3A", "\u00C1"), entry("C3E", "\u00C9"), entry("C3I", "\u00CD"), entry("C3O", "\u00D3"),

				entry("C3U", "\u00DA"), entry("C3N", "\u00D1"), entry("C3c", "\u00E7"), entry("C3C", "\u00C7"),

				entry("C2A1", "\u00A1"), entry("C2BF", "\u00BF"));

		for (Map.Entry<String, String> entry : reemplazos.entrySet()) {

			texto = texto.replace(entry.getKey(), entry.getValue());

		}

		return texto;

	}

	public static <K, V> Entry<K, V> entry(K key, V value) {

		return Map.entry(key, value);

	}

	public boolean esPrimeraEjecucionDeLaVersion() {

		return _EsPrimeraEjecucionDeLaVersion;

	}

	public boolean validarManualmenteDesarrollo(boolean validarDes) {
		return validarDes;
	}

}
