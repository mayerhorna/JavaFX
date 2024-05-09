package com.commerceapp.domain;

import java.io.InputStream;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.commerceapp.domain.ConfiguracionC.sMunicipiosCentral;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.domain.legalizacion.kLegalizacion.enumTipoLibro;
import com.commerceapp.maestros.FormatoFichero;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.maestros.TipoLibro;
import com.commerceapp.util.Formato;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class IdiomaC {
	private static final Logger logger = Logger.getLogger(IdiomaC.class.getName());
	public static final String kPrefijoFicheroAyuda = "AyudaCAPP";
	public static final String kFormatoFicheroAyuda = ".pdf";

	public enum EnumMensajes {
		Excepcion, DirectorioInexistente, FicheroInexistente, EstructuraDatosTxtIncorrecta, EscrituraFicheroFallida,
		RenombrarFicheroFallido, LegalizacionGuardada, VerificarElementoLista, AvisoEliminarLegalizacion,
		SeleccionarElemento, FicheroExistente, ImportacionRealizada, ValidarCamposObligatorios, ErrorFechas,
		ConfirmacionGuardar, NombreLegalizacionExistente, ElDirectorioNoContieneUnaLegalizacionValida,
		AvisoEliminarLibro, NoSeHaPodidoRealizarLaOperacion, NoSeHaPodidoGuardar, FicheroSinContenido, CampoObligatorio,
		BytesMaximosZipExcedidos, ExistenErroresSecundarios, HuellaDigitalMaximo, FicheroNoEncontrado,
		DirectorioTrabajoNoValido, DirectorioDatosNoValido, DirectorioAuxiliarNoValido,
		NoSeHaPodidoCargarLaConfiguracion, ErrorAlActualizarVersion, ExisteNuevaVersion, DirectorioSeleccionadoNoValido,
		NoExisteNuevaVersion, ElZipNoContieneUnaLegalizacionValida, NoSeHaPodidoBorrarContenidoDirectorio,
		NoSeHaPodidoDescomprimirZip, NoSeHaPodidoGenerarLasHuellas, FechaNoValida,
		FechaCierreUltimoLegalizadoMayorFechaApertura, BatchSeleccionandoEliminar, BatchGenerandoLog,
		BatchSeleccioneUnaOpcion, NombreFicheroDirectorioNoValido, ErrorSeHaProducido,
		FechaAperturaFechaCierreMasDeUnAnio, CambioConfiguracionConReinicio, BytesAvisoZip, RegistroProvinciaDiferentes,
		MensajeFaltante,ZipGeneradoCorrectamente, FuenteCodigoBarrarDesinstalada, EjecutarComoAdministrador, ErrorEnSoloLectura,
		ErrorAlBorrarFichero, ErrorNavegadorPortal, ExtensionFicheroNoAdmitida, NoSeHaPodidoCrearElDirectorio,
		AvisoEliminarFichero, NumeroMaximoAdjuntosSuperado, FicheroZipYaGenerado, FicheroZipYaEnviado,
		LibrosDeEjerciciosDiferentesApertura, LibrosDeEjerciciosDiferentesCierre, FormatoCifIncorrecto,
		FormatoNifIncorrecto, CodigoPostalNoConcuerdaConProvincia, CodigoPostalNoTiene5Digitos, Framework452NoInstalado,
		BatchAbrirRaizLegalizaciones, FormatoEsLegalia, FormatoEsLegalia2, SeAplicanReglasNuevasaLegalia,
		NoSeAplicanReglasNuevasaLegalia, NoSeHaPodidoAccederALaConfiguracionCentral, FactorResolucionIncorrecto,
		DebeSeleccionarUnFicheroZip, FicheroExistenteDeseaSobreescribir, ResultadoEncriptacion, VectorInicializacion,
		ErrorAlEncriptar, NoEncriptadoConLegalia, ErrorAlDesencriptar, ErrorTamanioClaveEncriptacion,
		ClaveEncriptacionyConfirmacionDistintas, ErrorReglasClaveEncriptacion, NoHayLibrosNoEncriptados,
		FicheroAEncriptarPerteneceALegalizacion, EnvioServicioHojaNoValida, EnvioServicioCorrecto,
		EnvioServicioYaRealizado, MemoriaInsuficienteFirma, ErrorAlEnviarTramite, ContenidoCampoCaracterNoPermitido,
		ContenidoCampoSoloNumeros, ContendioCamposSoloLetrasYNumeros, EnvioCorrectoSinObtenerNE, ZIPYaExiste,
		UserPassIncorrectos, EliminarProducto,NoexisteProducto,ConfirmPedido,AvisoEliminarPedido
	}

	public enum EnumLiterales {
		Todos, Recepcion, FicherosTxt, FicherosZip, Correcta, Erronea, Ficheros, Ejercicio, SeleccioneCarpetaDestino,
		ComprobandoActualizaciones, GenerandoPdfDeLaInstancia, GenerandoZip, DescargandoFicheroVersion,
		GenerandoHuellas, CampoObligatorio, FormatoEmailIncorrecto, DescomprimiendoZip, Si, No, Encriptando,
		Desencriptando, Fichero, DirectorioDatosLegalizacion, SoloLectura, EnvioDirecto, EnvioPortal, Subsanacion,
		SoloReenviar, MunicipioNoEnLista
	}

	private static String _idioma; // Idioma actual
	private static String _idiomaPredeterminado; // Idioma predeterminado

	// Variables para la carga y lectura del fichero de recursos
	private String _xmlRuta;
	private static Document _xmlDocument;
	private NodeList listaNodos;
	private Node _xmlNodo;

	// Getters y Setters
	public String getIdioma() {
		return _idioma;
	}

	public void setIdioma(String value) {
		this._idioma = value;
	}

	public static String getIdiomaPredeterminado() {
		return _idiomaPredeterminado;
	}

	public String getXmlRuta() {
		return _xmlRuta;
	}

	public void setXmlRuta(String value) {
		this._xmlRuta = value;
	}

	public IdiomaC(String queIdioma) {

		this._xmlRuta = "RecursosApp.xml"; // "/LegaliaMP/src/main/resources/RecursosApp.xml";//MGeneralRuta.RecursosApp;
											// Ruta a recursosapp.xml

		if (!cargarXml()) {
			System.exit(0);
		}

		this._idiomaPredeterminado = ObtenerPredeterminado();

		if (compruebaIdioma(queIdioma)) {
			this._idioma = queIdioma;
		} else {
			this._idioma = this._idiomaPredeterminado;
		}
	}

	private boolean cargarXml() {
		try {
			if (_xmlRuta != null && !_xmlRuta.isEmpty()) {

				InputStream streamXml = getClass().getClassLoader().getResourceAsStream(_xmlRuta);
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				_xmlDocument = documentBuilder.parse(streamXml);
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean compruebaIdioma(String codigoIdioma) {
		try {
			NodeList listaIdiomas = _xmlDocument.getElementsByTagName("Idioma");

			for (int i = 0; i < listaIdiomas.getLength(); i++) {
				Node nodo = listaIdiomas.item(i);
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					String codigo = nodo.getChildNodes().item(1).getTextContent();
					if (codigo.equals(codigoIdioma)) {
						return true;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private String ObtenerPredeterminado() {
		try {
			NodeList listanodos = _xmlDocument.getElementsByTagName("Idioma");
			return listanodos.item(0).getChildNodes().item(1).getTextContent();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error";
		}

	}

	private static String obtenerValor(String codigoIdioma, String tipo, String nombre, String elemento,
			String nombreControl, String nombreSubControl) {
		String textoEncontrado = "";

		try {
			String xPath = "/Recursos/" + codigoIdioma + "/" + tipo + "/" + nombre;
			if (!elemento.isEmpty())
				xPath += "/" + elemento;
			if (!nombreControl.isEmpty())
				xPath += "/" + nombreControl;
			if (!nombreSubControl.isEmpty())
				xPath += "/" + nombreSubControl;

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile(xPath);
			Node nodo = (Node) expr.evaluate(_xmlDocument, XPathConstants.NODE);

			if (nodo != null) {
				textoEncontrado = nodo.getTextContent();
			}

			textoEncontrado = textoEncontrado.replace("/crlf", System.lineSeparator());
			return textoEncontrado;

		} catch (Exception ex) {

			return "Error";
		}

	}

	public static String obtenerValor(String tipo, String nombre, String elemento, String nombreControl,
			String nombreSubControl) {

		String textoEncontrado = obtenerValor(_idioma, tipo, nombre, elemento, nombreControl, nombreSubControl);

		if (textoEncontrado.isEmpty() && !_idioma.equals(_idiomaPredeterminado)) {
			textoEncontrado = obtenerValor(_idiomaPredeterminado, tipo, nombre, elemento, nombreControl,
					nombreSubControl);
		}

		return textoEncontrado;
	}

	public void cargarIdiomaControles(Stage objetoFormulario, Parent parent) {

		try {
			String texto = "";
			Parent padre;
			if (objetoFormulario != null) {
				padre = objetoFormulario.getScene().getRoot();

			} else {
				padre = parent;
			}

			texto = obtenerValor(ObjetosIdioma.FORMULARIOS, padre.getId().toString(), ElementosIdiomaC.TEXT_FORMULARIO,
					"", "");

			if (!texto.isEmpty()) {

				objetoFormulario.setTitle(texto);
			}

			for (javafx.scene.Node componente : padre.getChildrenUnmodifiable()) {

				cargarIdiomaControl(objetoFormulario, padre, componente);

				if (componente instanceof Parent) {
					for (javafx.scene.Node subControl : ((Parent) componente).getChildrenUnmodifiable()) {
						cargarIdiomaControl(objetoFormulario, padre, subControl);
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	public void cargarIdiomaControl(Stage objetoFormulario, Parent parent, javafx.scene.Node componente) {

		try {

			String texto = "";
			String textotooltip = "";
			Parent padre;
			if (objetoFormulario != null) {
				padre = objetoFormulario.getScene().getRoot();
			} else {
				padre = parent;
			}

			texto = obtenerValor(ObjetosIdioma.FORMULARIOS, padre.getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,
					componente.getId(), "");

			if (componente instanceof ToolBar) {

				for (javafx.scene.Node nodo : ((ToolBar) componente).getItems()) {
					if (nodo instanceof Pane) {
						Pane pane = (Pane) nodo; // Cast a tipo Pane
						ObservableList<javafx.scene.Node> children = pane.getChildren();

						textotooltip = obtenerValor(ObjetosIdioma.FORMULARIOS, padre.getId().toString(),
								ElementosIdiomaC.TEXT_CONTROLES, children.get(0).getId(), "");

						if (!textotooltip.isEmpty()) {

							setToolTip(nodo, children.get(0), textotooltip);
						}

					}

				}
			}

			if (componente instanceof MenuBar) {

				MenuBar menuBar = (MenuBar) componente;

				cargarIdiomaMenus(objetoFormulario, menuBar);

			}

			if (componente instanceof TableView<?>) {

				cargarIdiomaListViews(objetoFormulario, (TableView<?>) componente);
			}

			if (componente instanceof Label) {
				if (!texto.isEmpty())

					((Label) componente).setText(texto);
			}
			if (componente instanceof Button) {
				if (!texto.isEmpty())

					((Button) componente).setText(texto);
			}
			if (componente instanceof RadioButton) {
				if (!texto.isEmpty())

					((RadioButton) componente).setText(texto);
			}

			if (componente instanceof TextArea) {
				if (!texto.isEmpty())

					((TextArea) componente).setText(texto);
			}

			if (componente instanceof Hyperlink) {
				if (!texto.isEmpty())

					((Hyperlink) componente).setText(texto);
			}

			if (componente instanceof CheckBox) {
				if (!texto.isEmpty())

					((CheckBox) componente).setText(texto);
			}

			if (componente instanceof javafx.scene.Parent) {

				javafx.scene.Parent parentControl = (javafx.scene.Parent) componente;

				for (javafx.scene.Node subControl : parentControl.getChildrenUnmodifiable()) {
					cargarIdiomaControl(objetoFormulario, padre, subControl);

				}
				if (componente instanceof ScrollPane) {
					ScrollPane scroll = (ScrollPane) componente;
					javafx.scene.Node subControl = scroll.getContent();
					cargarIdiomaControl(objetoFormulario, padre, subControl);

				}

			}

		} catch (Exception ex) {

			ex.printStackTrace(); // Continuar o manejar la excepción según sea necesario
			// }
		}
	}

	// cargarIdiomaMenuItem
	public void cargarIdiomaToolStripItem(Stage objetoFormulario, Menu menu) {

		try {
			String texto = "";
			texto = obtenerValor(ObjetosIdioma.FORMULARIOS, objetoFormulario.getScene().getRoot().getId().toString(),
					ElementosIdiomaC.TEXT_CONTROLES, menu.getId(), "");

			if (!texto.isEmpty()) {
				menu.setText(texto);
			}

			for (MenuItem menuItem : menu.getItems()) {

				String aux = obtenerValor(ObjetosIdioma.FORMULARIOS,
						objetoFormulario.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,
						menu.getId().toString(), menuItem.getId());
				if (aux == null) {
					menuItem.setText("");
				} else {
					menuItem.setText(texto);
				}

			}

		} catch (Exception ex) {

			ex.printStackTrace(); // Continuar o manejar la excepción según sea necesario

		}
	}

	public static void setToolTip(javafx.scene.Node componente, javafx.scene.Node componentebtn, String texto) {

		Tooltip.install(componente, new Tooltip(texto));
		Tooltip.install(componentebtn, new Tooltip(texto));
	}

	public void cargarIdiomaListViews(Stage objetoFormulario, TableView<?> tabla) {

		try {
			String indice;
			String textoColumna;

			for (int i = 0; i < tabla.getColumns().size(); i++) {

				indice = "columna" + (i + 1);

				textoColumna = obtenerValor(ObjetosIdioma.FORMULARIOS,
						objetoFormulario.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,
						tabla.getId().toString(), indice);

				if (!textoColumna.isEmpty()) {

					TableColumn<?, ?> col = (TableColumn<?, ?>) tabla.getColumns().get(i);
					col.setText(textoColumna);

				}

			}

		} catch (Exception ex) { // Manejar la excepción según sea necesario
			ex.printStackTrace();
		}
	}

	public static String obtenerMensaje(EnumMensajes queMensaje, Object arg0, Object arg1, Object arg2) {

		String mensaje = "";

		try {
			StringBuilder cadenaMensaje = new StringBuilder();
			StringBuilder tituloMensaje = new StringBuilder();

			if (!obtenerDatosMensaje(queMensaje.ordinal(), cadenaMensaje, tituloMensaje)) {
				cadenaMensaje.append(queMensaje.name());
			}

			mensaje = String.format(cadenaMensaje.toString(), arg0, arg1, arg2);

		} catch (Exception ex) {
			mensaje = ex.getMessage();
			ex.printStackTrace();
		}

		return mensaje;
	}

	private boolean obtenerDatosMensajeReglas(String codigoIdioma, String tipoMensajesReglas, int codigoMensaje,
			StringBuilder cadenaMensaje, StringBuilder campoFoco, StringBuilder codigoRetorno) {

		listaNodos = _xmlDocument.getElementsByTagName(tipoMensajesReglas);
		try {

			cadenaMensaje.setLength(0);
			campoFoco.setLength(0);
			NodeList nodeList = listaNodos.item(0).getChildNodes();
			List<Node> nodeListAsList = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {

				nodeListAsList.add(nodeList.item(i));
			}

			for (Node xmlMensaje : nodeListAsList) {

				NamedNodeMap attributes = xmlMensaje.getAttributes();

				if (attributes != null) {
					Node codigoNode = attributes.getNamedItem("codigo");
					if (codigoNode != null) {
						// Obtener el valor del atributo "codigo"
						String codigoValue = codigoNode.getNodeValue();

						// Comparar el valor del atributo "codigo" con la variable de comparación
						if (Integer.toString(codigoMensaje).equals(codigoValue)) {
							try {
								cadenaMensaje.append(xmlMensaje.getTextContent());
							} catch (Exception e) {
							}
							try {
								campoFoco.append(xmlMensaje.getAttributes().getNamedItem("campofoco").getNodeValue());
							} catch (Exception e) {
							}
							try {
								codigoRetorno.append(
										xmlMensaje.getAttributes().getNamedItem("codigoretorno").getNodeValue());
							} catch (Exception e) {
							}
							return true;

						}
					}
				}

			}

			/*
			 * for (int i = 0; i < listaNodos.getLength(); i++) { Node xmlMensaje =
			 * listaNodos.item(i); if
			 * (xmlMensaje.getAttributes().getNamedItem("codigo").getNodeValue()
			 * .equals(Integer.toString(codigoMensaje))) { try {
			 * cadenaMensaje.append(xmlMensaje.getTextContent()); } catch (Exception e) { }
			 * try { campoFoco.append(xmlMensaje.getAttributes().getNamedItem("campofoco").
			 * getNodeValue()); } catch (Exception e) { } try {
			 * codigoRetorno.append(xmlMensaje.getAttributes().getNamedItem("codigoretorno")
			 * .getNodeValue()); } catch (Exception e) { } return true; } }
			 */

		} catch (Exception ex) {
			cadenaMensaje.append(ex.getMessage());
			ex.printStackTrace();
		}

		return false;
	}

	public boolean obtenerDatosMensajeReglas(String tipoMensajeReglas, int codigoMensaje, StringBuilder cadenaMensaje,
			StringBuilder campoFoco, StringBuilder codigoRetorno) {
		boolean encontrado = false;

		encontrado = obtenerDatosMensajeReglas(_idioma, tipoMensajeReglas, codigoMensaje, cadenaMensaje, campoFoco,
				codigoRetorno);

		if (!encontrado && !_idioma.equals(getIdiomaPredeterminado())) {
			encontrado = obtenerDatosMensajeReglas(_idiomaPredeterminado, tipoMensajeReglas, codigoMensaje,
					cadenaMensaje, campoFoco, codigoRetorno);
		}

		return encontrado;
	}

	public List<TipoLibro> ObtenerListaTiposLibro(String CodigoIdioma) {
		List<TipoLibro> _lst_lista_TiposLibro = new ArrayList<TipoLibro>();
//aqui
		int indice = 0;

		NodeList listanodos = _xmlDocument.getElementsByTagName("Tipo");

		try {
			enumTipoLibro[] valores = enumTipoLibro.values();

			for (int i = 0; i < listanodos.getLength(); i++) {

				Element _xml_Tipo_Libro = (Element) listanodos.item(i);

				TipoLibro _tipoLibro = new TipoLibro();

				_tipoLibro.setCodigo(valores[Integer
						.parseInt(_xml_Tipo_Libro.getElementsByTagName("Codigo").item(0).getTextContent()) - 1]);

				String descripcion = _xml_Tipo_Libro.getElementsByTagName("Descripcion").item(0).getTextContent();
				_tipoLibro.setDescripcion(descripcion);

				String nombreFichero = _xml_Tipo_Libro.getElementsByTagName("NombreFichero").item(0).getTextContent();
				_tipoLibro.setNombreFichero(nombreFichero);

				boolean comprobarSolapamientoFechasAperturaYCierre = Boolean.parseBoolean(_xml_Tipo_Libro
						.getElementsByTagName("ComprobarSolapamientoFechasAperturaYCierre").item(0).getTextContent());
				_tipoLibro.setComprobarSolapamientoFechasAperturaYCierre(comprobarSolapamientoFechasAperturaYCierre);

				boolean comprobarFechaApertura = Boolean.parseBoolean(
						_xml_Tipo_Libro.getElementsByTagName("ComprobarFechaApertura").item(0).getTextContent());
				_tipoLibro.setComprobarFechaApertura(comprobarFechaApertura);

				_lst_lista_TiposLibro.add(_tipoLibro);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		if (_lst_lista_TiposLibro != null) {
			// _lst_lista_TiposLibro.sort(null);
		}

		return _lst_lista_TiposLibro;
	}

	public List<TipoLibro> obtenerListaTiposLibro() {

		List<TipoLibro> resultado = ObtenerListaTiposLibro(_idioma);

		if (resultado != null) {
			return resultado;
		} else {
			if (!_idioma.equals(_idiomaPredeterminado)) {
				return ObtenerListaTiposLibro(_idiomaPredeterminado);
			}
		}

		return resultado;

	}

	public List<FormatoFichero> obtenerListaFormatosFichero(String codigoIdioma) {
		String rutaMaestro = "/Recursos/" + codigoIdioma + "/Maestros/";
		List<FormatoFichero> lista = new ArrayList<>();
		kLegalizacion.enumExtensionFichero[] enums = kLegalizacion.enumExtensionFichero.values();
		try {
			NodeList listaNodos = _xmlDocument.getElementsByTagName("Formato");
			for (int i = 0; i < listaNodos.getLength(); i++) {
				Node nodo = listaNodos.item(i);
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					FormatoFichero formato = new FormatoFichero();
					NodeList elementos = nodo.getChildNodes();
					for (int j = 0; j < elementos.getLength(); j++) {
						Node elemento = elementos.item(j);
						if (elemento.getNodeType() == Node.ELEMENT_NODE) {
							String etiqueta = elemento.getNodeName();
							String valor = elemento.getTextContent().trim();
							switch (etiqueta) {
							case "Codigo":
								formato.setCodigo(enums[Integer.parseInt(valor) - 1]);
								break;
							case "Descripcion":
								formato.setDescripcion(valor);
								break;
							case "Extension":

								formato.setExtension(valor);
								break;
							case "EsDeEncriptacion":
								formato.setEsDeEncriptacion(Boolean.parseBoolean(valor));
								break;
							}
						}
					}
					lista.add(formato);
				}
			}
		} catch (Exception ex) {
			// Manejo de la excepción
			ex.printStackTrace();
		}

		return lista;
	}

	/**
	 * Es el método público sobrecarga del método privado Obtiene la lista en el
	 * idioma actual (_Idioma) y si no puede en el idioma predeterminado
	 * 
	 * @return Lista de formatos de fichero
	 */

	public List<FormatoFichero> obtenerListaFormatosFichero() {
		List<FormatoFichero> resultado = obtenerListaFormatosFichero(_idioma);

		if (resultado != null) {
			return resultado;
		} else {
			if (!_idioma.equals(_idiomaPredeterminado)) {
				return obtenerListaFormatosFichero(_idiomaPredeterminado);
			}
		}

		return resultado;
	}

	public List<MaestroCodigoDescripcion> obtenerListaMaestro(String maestro) {

		List<MaestroCodigoDescripcion> resultado = obtenerListaMaestro(_idioma, maestro);

		if (resultado != null) {
			return resultado;
		} else {
			if (!_idioma.equals(_idiomaPredeterminado)) {
				return obtenerListaMaestro(_idiomaPredeterminado, maestro);
			}
		}

		return resultado;
	}

	private List<MaestroCodigoDescripcion> obtenerListaMaestro(String codigoIdioma, String maestro) {

		List<MaestroCodigoDescripcion> lista = new ArrayList<>();

		String rutaMaestro = "/Recursos/" + codigoIdioma + "/Maestros/" + maestro;

		NodeList listanodos = _xmlDocument.getElementsByTagName(maestro);

		try {
			if (listanodos.getLength() > 0) {
				for (int i = 0; i < listanodos.item(0).getChildNodes().getLength(); i++) {
					Node nodo = listanodos.item(0).getChildNodes().item(i);
					if (nodo.getNodeType() == Node.ELEMENT_NODE) {
						Element xmlNodo = (Element) nodo;

						MaestroCodigoDescripcion elemento = new MaestroCodigoDescripcion(maestro);
						elemento.setCodigo(xmlNodo.getElementsByTagName("Codigo").item(0).getTextContent());
						elemento.setDescripcion(xmlNodo.getElementsByTagName("Descripcion").item(0).getTextContent());

						lista.add(elemento);
					}
				}
			}
		} catch (Exception ex) {
			return null;
		}

		if (lista != null) {
			// Ordenar la lista si es necesario
			Collections.sort(lista);
		}

		return lista;
	}

	/**
	 * Es el método público sobrecarga del método privado Obtiene la lista en el
	 * idioma actual (_Idioma) y si no puede en el idioma predeterminado
	 * 
	 * @return Lista de elementos del maestro
	 */

	public List<String> obtenerMunicipiosDeProvincia(String codProvincia) {
		List<String> lista = new ArrayList<>();
		int index = 0;

		if (Formato.ValorNulo(codProvincia)) {
			return lista;
		}

		listaNodos = _xmlDocument.getElementsByTagName("Provincia" + codProvincia);

		try {
			if (listaNodos.getLength() > 0) {
				for (int i = 0; i < listaNodos.item(0).getChildNodes().getLength(); i++) {
					Node nodo = listaNodos.item(0).getChildNodes().item(i);
					if (nodo.getNodeType() == Node.ELEMENT_NODE) {
						lista.add(nodo.getTextContent());
					}
				}

				if (MGeneral.Configuracion.getListaMunicipiosCentral() != null) {
					for (sMunicipiosCentral municipio : MGeneral.Configuracion.getListaMunicipiosCentral()) {
						if (municipio.CPRO.equalsIgnoreCase(codProvincia)) {
							index = lista.indexOf(municipio.Nombre);
							switch (municipio.Operacion) {
							case "A":
								if (index == -1)
									lista.add(municipio.Nombre);
								break;
							case "B":
								if (index >= 0)
									lista.remove(index);
								break;
							case "M":
								if (index >= 0)
									lista.set(index, municipio.NombreModificado);
								break;
							}
						}
					}
				}

			}
		} catch (Exception ex) {
			// v1.5.6 Si hay alguna excepción se devuelve siempre lista
		}

		if (lista != null) {
			Collections.sort(lista);
			// Ordenar la lista si es necesario
			// Collections.sort(lista);
		}

		return lista;
	}

	public boolean existeMunicipioDeProvincia(String codProvincia, String descripcionMunicipio) {
		if (Formato.ValorNulo(codProvincia)) {
			return false;
		}
		if (Formato.ValorNulo(descripcionMunicipio)) {
			return false;
		}

		List<String> lista = obtenerMunicipiosDeProvincia(codProvincia);

		if (lista == null) {
			return false;
		}

		return lista.contains(descripcionMunicipio);
	}

	public static boolean obtenerDatosMensaje(String codigoIdioma, int codigoMensaje, StringBuilder cadenaMensaje,
			StringBuilder tituloMensaje) {

		String rutaMensajes = "Mensajes";

		NodeList listanodos = _xmlDocument.getElementsByTagName(rutaMensajes);

		try {
			cadenaMensaje.setLength(0);
			tituloMensaje.setLength(0);

			for (int i = 0; i < listanodos.getLength(); i++) {
				Node nodoMensajes = listanodos.item(i);

				if (nodoMensajes.getNodeType() == Node.ELEMENT_NODE) {
					Element elementoMensajes = (Element) nodoMensajes;
					NodeList listaMensajes = elementoMensajes.getChildNodes();

					for (int j = 0; j < listaMensajes.getLength(); j++) {
						Node nodoMensaje = listaMensajes.item(j);

						if (nodoMensaje.getNodeType() == Node.ELEMENT_NODE) {
							Element elementoMensaje = (Element) nodoMensaje;

							String codigo = elementoMensaje.getAttribute("codigo");

							if (codigo.equals(String.valueOf(codigoMensaje))) {

								cadenaMensaje.append(
										elementoMensaje.getTextContent().replaceAll("/crlf", System.lineSeparator()));

								String titulo = elementoMensaje.getAttribute("titulo");
								tituloMensaje.append(titulo);

								return true;
							}
						}
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			cadenaMensaje.setLength(0);
			cadenaMensaje.append(ex.getMessage());
		}

		return false;
	}

	public static boolean obtenerDatosMensaje(int codigoMensaje, StringBuilder cadenaMensaje,
			StringBuilder tituloMensaje) {
		boolean encontrado = false;

		encontrado = obtenerDatosMensaje(_idioma, codigoMensaje, cadenaMensaje, tituloMensaje);

		if (!encontrado && !_idioma.equals(getIdiomaPredeterminado())) {
			encontrado = obtenerDatosMensaje(_idiomaPredeterminado, codigoMensaje, cadenaMensaje, tituloMensaje);
		}

		return encontrado;
	}

	public static boolean MostrarMensaje(EnumMensajes queMensaje, String arg0, String arg1, String arg2) {
		// EN caso sea un CONFIRMATION necesito devolver la opcion que se escoja.
		try {
			StringBuilder cadenaMensaje = new StringBuilder();
			StringBuilder tituloMensaje = new StringBuilder();

			String mensaje = "";

			if (!obtenerDatosMensaje(queMensaje.ordinal(), cadenaMensaje, tituloMensaje)) {
				cadenaMensaje.append(queMensaje.name());
			}

			if (tituloMensaje.isEmpty()) {
				tituloMensaje.append(MGeneral.kgNombreAplicacion);
			}
			mensaje = String.format(cadenaMensaje.toString(), arg0, arg1, arg2);

			if (!MGeneral.ModoBatch) {

				Alert alert;

				switch (tituloMensaje.toString()) {
				case "Error":

					alert = new Alert(AlertType.ERROR, mensaje);
					break;
				case "Información":

					alert = new Alert(AlertType.INFORMATION, mensaje);
					break;
				case "Aviso":

					alert = new Alert(AlertType.CONFIRMATION, mensaje);
					break;
				default:

					alert = new Alert(AlertType.WARNING, mensaje);

				}
				alert.setResizable(true);
				alert.setTitle(tituloMensaje.toString());
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// stage.getIcons().clear();
						stage.getIcons()
								.add(new Image(getClass().getResource("/imagenes/LogoECommerce.png").toString()));

					}
				});

				// alert.setContentText(mensaje);
				if (alert.getAlertType() == Alert.AlertType.CONFIRMATION) {

					ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

					if (resultado == ButtonType.OK) {
						return true;
					} else {
						return false;
					}
				} else {
					alert.showAndWait();
				}
			} else {
				System.out.println(mensaje);
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			// return DialogResult.Abort;
		}
		return false;
	}

	public void cargarIdiomaMenus(Stage objetoFormulario, MenuBar objetoMenu) {
		try {
			for (Menu menu : objetoMenu.getMenus()) {
				String nombreMenu = obtenerValor(ObjetosIdioma.FORMULARIOS,
						objetoFormulario.getScene().getRoot().getId().toString(), objetoMenu.getId(), menu.getId(),
						ElementosIdiomaC.TEXT_FORMULARIO);
				menu.setText(nombreMenu);

				for (MenuItem subMenuItem : menu.getItems()) {
					String nombreSubMenu = obtenerValor(ObjetosIdioma.FORMULARIOS,
							objetoFormulario.getScene().getRoot().getId().toString(), objetoMenu.getId(), menu.getId(),
							subMenuItem.getId());
					subMenuItem.setText(nombreSubMenu);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// Manejar la excepción según sea necesario
		}
	}

	public boolean obtenerLiteral(String codigoIdioma, int codigoLiteral, StringBuilder cadenaLiteral) {

		String rutaLiterales = "Literales";
		String codigo = String.valueOf(codigoLiteral);
		listaNodos = _xmlDocument.getElementsByTagName(rutaLiterales);

		try {
			cadenaLiteral.setLength(0); // Limpiar el StringBuilder antes de usarlo
			NodeList nodeList = listaNodos.item(0).getChildNodes();
			List<Node> nodeListAsList = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {

				nodeListAsList.add(nodeList.item(i));
			}

			for (Node node : nodeListAsList) {

				NamedNodeMap attributes = node.getAttributes();

				if (attributes != null) {
					Node codigoNode = attributes.getNamedItem("codigo");
					if (codigoNode != null) {
						// Obtener el valor del atributo "codigo"
						String codigoValue = codigoNode.getNodeValue();

						// Comparar el valor del atributo "codigo" con la variable de comparación
						if (codigo.equals(codigoValue)) {
							cadenaLiteral.append(node.getTextContent());

							return true;

						}
					}
				}

			}
		} catch (Exception ex) {
			cadenaLiteral.append(ex.getMessage());
			ex.printStackTrace();
		}

		return false;
	}

	public StringBuilder obtenerLiteral(EnumLiterales codigoLiteral) {
		StringBuilder cadenaLiteral = new StringBuilder();

		boolean encontrado = obtenerLiteral(_idioma, codigoLiteral.ordinal(), cadenaLiteral);

		if (!encontrado && !_idioma.equals(getIdiomaPredeterminado())) {
			encontrado = obtenerLiteral(_idiomaPredeterminado, codigoLiteral.ordinal(), cadenaLiteral);
		}

		if (!encontrado) {
			cadenaLiteral.setLength(0);
		}

		return cadenaLiteral;
	}

	public class AyudaUtils {

		public String obtenerFicheroAyuda() {
			String fichero = "";
			fichero = obtenerFicheroAyuda(_idioma);
			return fichero;
		}

		public static String obtenerFicheroAyuda(String codigoIdioma) {
			String xPath = MGeneralRuta.DirectorioAyuda;
			xPath = Paths.get(xPath, kPrefijoFicheroAyuda + codigoIdioma + kFormatoFicheroAyuda).toString();

			return xPath;

		}
	}

	public void mostrarMensajeCadena(String cadenaMensaje, String arg0, String arg1) {
		try {
			String tituloMensaje = "";
			String mensaje;
			ButtonType resultado = null;

			tituloMensaje = MGeneral.kgNombreAplicacion;

			mensaje = String.format(cadenaMensaje, arg0, arg1);

			if (!MGeneral.ModoBatch) {
				Alert alerta = new Alert(AlertType.ERROR, mensaje);
				alerta.setHeaderText(null);
				alerta.setTitle(tituloMensaje);

				alerta.showAndWait();
				resultado = alerta.getResult();
			} else {
				System.out.println(mensaje);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
