package com.commerceapp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.service.LegalizacionService.EnumResultadoZip;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.ManejoXml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

enum EnumOpcionBatchParam {
	oa, ov, oh, oz, oe, ob
}

class SBatchParameter {
	public EnumOpcionBatchParam opcion;
	public String camino;
	public String log;
	public int ejercicio;
	public boolean raiz;
}

public class Batch {
	private Errores errorOcurrido = new Errores();

	public void manejaCommandLineArgs(boolean[] cancel, String[] args) {
		SBatchParameter param = new SBatchParameter();
		ObservableList<cDatos> arrayDatos = FXCollections.observableArrayList();
		Legalizaciones mLs = new Legalizaciones();
		LegalizacionService ml = new LegalizacionService(false, null);
		String commandLineArgs = "";
		int nl = 0;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document[] nXmlLog = new Document[1];
		Document[] nXmlLogOrigen = new Document[1];

		nXmlLogOrigen[0] = dBuilder.newDocument();
		nXmlLog[0] = dBuilder.newDocument();
		ManejoXml mManejoXml = new ManejoXml();

		Element nLegaliaLog = nXmlLog[0].createElement("LegaliaLog");

		nXmlLog[0].appendChild(nLegaliaLog);

		Node nNodoBatch;
		Node nNodoLegalizacion;

		nNodoBatch = creaNodoXml(nXmlLog[0], nLegaliaLog, "Batch", "");
		creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Inicio", new Date().toString());

		try {

			for (String s : args) {
				if (s.toLowerCase().startsWith("o=")) {
					switch (s.toLowerCase().substring(2)) {
					case "oa":
						param.opcion = EnumOpcionBatchParam.oa;
						break;
					case "ov":
						param.opcion = EnumOpcionBatchParam.ov;
						break;
					case "oh":
						param.opcion = EnumOpcionBatchParam.oh;
						break;
					case "oz":
						param.opcion = EnumOpcionBatchParam.oz;
						break;
					case "oe":
						param.opcion = EnumOpcionBatchParam.oe;
						break;
					case "ob":
						param.opcion = EnumOpcionBatchParam.ob;
						break;
					}
				} else if (s.toLowerCase().startsWith("c=")) {
					param.camino = s.substring(2);
				} else if (s.toLowerCase().startsWith("l=")) {
					param.log = s.substring(2);
				} else if (s.toLowerCase().startsWith("e=")) {
					param.ejercicio = Integer.parseInt(s.substring(2));
				} else if (s.toLowerCase().startsWith("r=")) {
					param.raiz = s.substring(2).equals("1") ? true : false;

				} else {
					creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "TextoResultado",
							"Parámetro no reconocido: " + s.toLowerCase());
				}
				commandLineArgs += s.toLowerCase() + " ";
			}
			creaNodoXml(nXmlLog[0], (Element) nNodoBatch, "Parametro", commandLineArgs.trim());

			if ((param.opcion != EnumOpcionBatchParam.oa && param.opcion != EnumOpcionBatchParam.ob)
					&& (param.opcion.toString().isEmpty() || param.camino.isEmpty() || param.log.isEmpty())) {
				creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Resultado", "0");
				creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "TextoResultado",
						"Faltan Parámetros o el camino termina en \\");

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Mensaje");
				alert.setHeaderText(null);
				alert.setContentText("Faltan Párámetros o camino termina en \\");
				alert.showAndWait();

				return;
			} else if (param.opcion == EnumOpcionBatchParam.oa && param.raiz) {
				creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Resultado", "0");
				creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "TextoResultado",
						"La opción abrir no es compatible con una ruta raíz.");
				return;
			} else {
				boolean isRoot = param.raiz;
				if (isRoot) {

					mLs.cargaDirectorio(param.camino, arrayDatos, param.ejercicio, true, true, true);
				} else {

					ml.cargaDatosLegalias(param.camino);
					arrayDatos.add(ml.Datos);
				}
			}

			nl = arrayDatos != null ? arrayDatos.size() : 0;
			creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Legalizaciones", Integer.toString(nl));

			if (arrayDatos != null) {

				for (int i = 0; i < arrayDatos.size(); i++) {
					nNodoLegalizacion = creaNodoXml(nXmlLog[0], (Element) nNodoBatch, "Legalizacion", "");

					creaAtributoNodoXml(nXmlLog[0], (Element) nNodoLegalizacion, "Camino",
							arrayDatos.get(i).get_PathDatos());

					switch (param.opcion) {
					case ov:
						creaAtributoNodoXml(nXmlLog[0], (Element) nNodoLegalizacion, "Opción", "Validar");
						batchValidar(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion);

						break;
					case oa:

						batchAbrir(arrayDatos.get(i).get_PathDatos());
						cancel[0] = false;
						break;
					case ob:

						MGeneral.batchAbrirForm = "frmProcesosBatch";
						cancel[0] = false;
						break;
					case oe:
						creaAtributoNodoXml(nXmlLog[0], (Element) nNodoLegalizacion, "Opción", "Eliminar");
						batchEliminar(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion);
						break;
					case oh:
						creaAtributoNodoXml(nXmlLog[0], (Element) nNodoLegalizacion, "Opción", "Generar Huellas");
						if (batchValidar(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion)) {
							batchGenerarHuellas(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion);
						}
						break;
					case oz:
						creaAtributoNodoXml(nXmlLog[0], (Element) nNodoLegalizacion, "Opción", "Generar Zip");
						// Primero valida antes de generar zip
						if (batchValidar(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion)) {
							batchGenerarZip(arrayDatos.get(i).get_PathDatos(), nXmlLog[0], nNodoLegalizacion);
						}
						break;
					}
				}
			}

		} catch (Exception ex) {
			errorOcurrido.generaError(false, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");

			// Agregar atributos al nodo del log XML
			creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Resultado", "0");
			creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "TextoResultado",
					"Proceso Batch Excepción: " + ex.getMessage());
		} finally {
			creaAtributoNodoXml(nXmlLog[0], (Element) nNodoBatch, "Fin",
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

			try {

				if (param.opcion != EnumOpcionBatchParam.oa && param.opcion != EnumOpcionBatchParam.ob) {

					if (!mManejoXml.AbrirArchivoXMLLOG(nXmlLogOrigen[0], param.log)) {

						if (Ficheros.FicheroExiste(param.log) == Ficheros.ExistFicheroEnum.Existe) {
							Ficheros.FicheroBorra(param.log);
						}
						mManejoXml.CrearXmlSiNoExiste(param.log, "LegaliaLog");
						mManejoXml.AbrirArchivoXMLLOG(nXmlLogOrigen[0], param.log);

					}

					insertaXmlenXml(nXmlLogOrigen[0], nXmlLog[0]);

					mManejoXml.xmlSave(param.log, nXmlLogOrigen[0]);
				}
			} catch (Exception ex) {
				errorOcurrido.generaError(false, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");
			}
		}
	}

	public boolean batchValidar(String directorioDatos, Document nXmlLog, Node nodoPadre) {
		boolean resultadoCarga = false;
		kLegalizacion.enumResultadoValidacion resultadoValida;
		String mensaje = "";
		String campofoco = "";
		boolean batchValidar = false;

		Node nNodoAccion = creaNodoXml(nXmlLog, (Element) nodoPadre, "Validar", "");
		creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Inicio", java.time.LocalDateTime.now().toString());

		try {
			LegalizacionService mL = new LegalizacionService(false, null);
			resultadoCarga = mL.carga(directorioDatos);
			resultadoValida = mL.valida();

			if (resultadoValida != kLegalizacion.enumResultadoValidacion.Valida) {

				if (resultadoValida == kLegalizacion.enumResultadoValidacion.NoValida) {
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "No Valida");
				} else if (resultadoValida == kLegalizacion.enumResultadoValidacion.ValidaConAvisos) {
					batchValidar = true;
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "1");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Valida con avisos");
				}

				for (int i = 0; i < mL.MensajesReglas.length; i++) {
					if (mL.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsError)
						creaNodoXml(nXmlLog, (Element) nNodoAccion, "Error", mL.MensajesReglas[i].getTextoMensaje());
					if (mL.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsAviso)
						creaNodoXml(nXmlLog, (Element) nNodoAccion, "Aviso", mL.MensajesReglas[i].getTextoMensaje());
				}

			} else {
				// Validación Correcta
				batchValidar = true;
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "1");
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Valida");
			}

		} catch (Exception ex) {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", errorOcurrido.getDescripcion());
		} finally {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Fin", java.time.LocalDateTime.now().toString());
		}

		return batchValidar;
	}

	public static Element creaNodoXml(Document doc, Element nodoPadre, String nombreNodo, String valor) {
		try {
			Element node = doc.createElement(nombreNodo);
			if (!valor.isEmpty()) {
				node.appendChild(doc.createTextNode(valor));
			}

			if (nodoPadre == null) {
				doc.appendChild(node);
			} else {
				nodoPadre.appendChild(node);
			}

			return node;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean creaAtributoNodoXml(Document doc, Element nodo, String nombreAttr, String valor) {
		try {
			Attr atributo = doc.createAttribute(nombreAttr);
			atributo.setValue(valor);
			nodo.setAttributeNode(atributo);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean insertaXmlenXml(Document xmlOrigen, Document xmlDestino) {

		try {

			for (int i = 0; i < xmlDestino.getDocumentElement().getChildNodes().getLength(); i++) {
				Node nNode = xmlOrigen.importNode(xmlDestino.getDocumentElement().getChildNodes().item(i), true);

				xmlOrigen.getDocumentElement().appendChild(nNode);
			}
			return true;
		} catch (Exception ex) {
		
			return false;
		}
	}

	public void batchAbrir(String directorioDatos) {
		try {
			LegalizacionService mL = new LegalizacionService(false, null);
			boolean resultadoCarga = mL.carga(directorioDatos);
			if (resultadoCarga) {

				MGeneral.batchAbrirDir = directorioDatos;
			} else {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Mensaje");
				alert.setHeaderText(null);
				alert.setContentText("Legalización No Carga");
				alert.showAndWait();

			}
		} catch (Exception ex) {
			errorOcurrido.generaError(false, IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Mensaje");
			alert.setHeaderText(null);
			alert.setContentText(ex.getMessage());
			alert.showAndWait();

		}
	}

	public void batchGenerarHuellas(String directorioDatos, Document nXmlLog, Node nodoPadre) {
		boolean resultadoCarga = false;
		boolean resultadoAsignaHuellas = false;

		Node nNodoAccion = creaNodoXml(nXmlLog, (Element) nodoPadre, "Huellas", "");

		creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Inicio", java.time.LocalDateTime.now().toString());

		Node nNodoFichero;

		try {
			LegalizacionService mL = new LegalizacionService(false, null);
			resultadoCarga = mL.carga(directorioDatos);
			if (resultadoCarga) {
				resultadoAsignaHuellas = mL.generarHuellas();
				if (resultadoAsignaHuellas) {
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "1");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Correcto");
					for (int i = 0; i < mL.getNumeroLibros(); i++) {
						for (int j = 0; j < mL.Libros.get(i).getNumeroFicheros(); j++) {
							nNodoFichero = creaNodoXml(nXmlLog, (Element) nNodoAccion, "Fichero", "");
							creaAtributoNodoXml(nXmlLog, (Element) nNodoFichero, "Nombre",
									mL.Libros.get(i).Ficheros[j].getNombreFichero());
							creaNodoXml(nXmlLog, (Element) nNodoFichero, "Huella",
									mL.Libros.get(i).Ficheros[j].getHuella());
						}
					}
				} else {

					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Incorrecto");
				}
			} else {

				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "No Carga");
			}
		} catch (Exception ex) {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Excepción: " + ex.getMessage());
		} finally {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Fin", java.time.LocalDateTime.now().toString());
		}
	}

	public void batchGenerarZip(String directorioDatos, Document nXmlLog, Node nodoPadre) {
		boolean resultadoCarga = false;
		EnumResultadoZip resultadoGenerarZip;

		Node nNodoAccion = creaNodoXml(nXmlLog, (Element) nodoPadre, "Zip", "");
		creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Inicio", java.time.LocalDateTime.now().toString());

		try {
			LegalizacionService mlform = new LegalizacionService(false, null);
			resultadoCarga = mlform.carga(directorioDatos);
			if (resultadoCarga) {
				resultadoGenerarZip = mlform.generarZip("");
				switch (resultadoGenerarZip) {
				case Correcto:
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "1");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Correcto");
					creaNodoXml(nXmlLog, (Element) nNodoAccion, "FicheroZip", mlform.Datos.get_NombreZip());
					break;
				case NoValida:
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "No Valida");
					break;
				default:
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado",
							mlform.ErrorOcurrido.getDescripcion());
					break;
				}
			} else {
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "No Carga");
			}
		} catch (Exception ex) {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Excepción: " + ex.getMessage());
		} finally {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Fin", java.time.LocalDateTime.now().toString());
		}
	}

	public void batchEliminar(String directorioDatos, Document nXmlLog, Node nodoPadre) {
		Node nNodoAccion = creaNodoXml(nXmlLog, (Element) nodoPadre, "Eliminar", "");
		creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Inicio", java.time.LocalDateTime.now().toString());
		boolean resultadoCarga;
		StringBuilder textoError = new StringBuilder();

		try {
			LegalizacionService mL = new LegalizacionService(false, null);
			resultadoCarga = mL.carga(directorioDatos);

			if (resultadoCarga) {
				if (Ficheros.DirectorioBorra(directorioDatos, true, true, textoError)) {
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "1");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Realizado");
				} else {
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
					creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Error: " + textoError);
				}
			} else {
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
				creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado",
						"Error: no se puede realizar la carga de datos de la legalización");
			}
		} catch (Exception ex) {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Resultado", "0");
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "TextoResultado", "Excepción: " + ex.getMessage());
		} finally {
			creaAtributoNodoXml(nXmlLog, (Element) nNodoAccion, "Fin", java.time.LocalDateTime.now().toString());
		}
	}

}
