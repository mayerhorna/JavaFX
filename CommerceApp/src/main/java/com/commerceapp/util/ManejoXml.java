package com.commerceapp.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.commerceapp.service.ConfiguracionService;
import com.commerceapp.util.Ficheros.ExistFicheroEnum;

public class ManejoXml {
	private static final Logger logger = Logger.getLogger(ManejoXml.class.getName());
	private static sResulValidaPeticion _resulValidaXml;

	/**
	 * Tipo que almacena el resultado de validar un XML
	 */
	public static class sResulValidaPeticion {
		public boolean esValida;
		public String mensajeError;
	}

	public static String extraerCodificacionXml(String rutaFichero) {
		byte[] xmlBinario;
		try {
			xmlBinario = Ficheros.LeerFichero(rutaFichero, 500);
			return extraerCodificacionXml(xmlBinario);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}

	}

	/**
	 * Intenta extraer de un XML en formato binario la codificación indicada en la
	 * declaración.
	 *
	 * @param xmlBinario XML en formato binario.
	 * @return Codificación del XML.
	 */
	public static String extraerCodificacionXml(byte[] xmlBinario) {
		String codificacion = "ISO-8859-1";
		if (comprobarCodificacionXml(xmlBinario, codificacion)) {
			return codificacion;
		}

		codificacion = "UTF-8";
		if (comprobarCodificacionXml(xmlBinario, codificacion)) {
			return codificacion;
		}

		return "";
	}

	/**
	 * Comprueba si la codificación del XML coincide con la especificada.
	 *
	 * @param xmlBinario   XML en formato binario.
	 * @param codificacion Codificación a comprobar.
	 * @return true si la codificación coincide, false en caso contrario.
	 */
	private static boolean comprobarCodificacionXml(byte[] xmlBinario, String codificacion) {
		try {
			String xmlCadena = new String(xmlBinario, codificacion);
			int posIni = xmlCadena.indexOf("encoding=\"", 0);
			if (posIni >= 0) {
				posIni = posIni + "encoding=\"".length();
				int posFin = xmlCadena.indexOf("\"", posIni);
				if (posFin >= 0) {
					String resultado = xmlCadena.substring(posIni, posFin);
					return resultado.equalsIgnoreCase(codificacion);
				}
			}
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	public boolean AbrirArchivoXML(Document xmlDoc, String nombreArchivoXML) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			xmlDoc = dBuilder.parse(nombreArchivoXML);
			xmlDoc.getDocumentElement().normalize();

			return true;

		} catch (Exception ex) {

			return false;
		}
	}

	public boolean AbrirArchivoXMLLOG(Document xmlDoc, String nombreArchivoXML) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document parsedDoc = dBuilder.parse(nombreArchivoXML);
			Element rootElement = parsedDoc.getDocumentElement();
			Node importedNode = xmlDoc.importNode(rootElement, true);
			xmlDoc.appendChild(importedNode);

			xmlDoc.normalize();
			return true;
		} catch (Exception ex) {
		
			return false;
		}
	}

	private static void agregarElemento(Document doc, Element parent, String nombreElemento, String valor) {
		Element elemento = doc.createElement(nombreElemento);
		elemento.appendChild(doc.createTextNode(valor));
		parent.appendChild(elemento);
	}

	public static boolean xmlSave(String nombreArchivoXML, Document xmlDoc) {
		long ti = System.currentTimeMillis();

		do {
			try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(xmlDoc);
				StreamResult result = new StreamResult(new File(nombreArchivoXML));
				transformer.transform(source, result);
				return true;
			} catch (TransformerException | RuntimeException ex) {
				if (System.currentTimeMillis() - ti > 2000) {
					return false;
				}
			}
		} while (true);
	}

	public boolean creaNodoSiNoExiste(Document archivoXML, Element nodoPadre, String nombreNodoNuevo,
			String valorDelNodo) {

		boolean nodoCreado = false;
		Element nodoNuevo = archivoXML.createElement(nombreNodoNuevo);

		try {
			if (nodoPadre == null) {
				if (!existeNodo(archivoXML, null, nombreNodoNuevo)) {
					archivoXML.appendChild(nodoNuevo);
				}
				nodoCreado = true;
				return nodoCreado;
			} else {
				if (!existeNodo(archivoXML, nodoPadre, nombreNodoNuevo)) {
					// Crear un nuevo elemento (nodo) y agregarlo al documento
					nodoPadre.appendChild(nodoNuevo);
					if (valorDelNodo != "") {
						nodoNuevo.appendChild(archivoXML.createTextNode(valorDelNodo));
					}

				} else {

				}
			}

			/*
			 * if (nodoPadre == null) { if (!nombreNodoNuevo.startsWith("/")) {
			 * //archivoXML.appendChild(nodoNuevo); } if
			 * (archivoXML.getElementsByTagName(nombreNodoNuevo) == null) {
			 * archivoXML.appendChild(nodoNuevo); nodoCreado = true; } } else { if
			 * (nodoPadre.getElementsByTagName(nombreNodoNuevo) == null) {
			 * nodoPadre.appendChild(nodoNuevo); if (!valorDelNodo.isEmpty()) {
			 * nodoNuevo.setTextContent(valorDelNodo); } nodoCreado = true; } }
			 */

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return nodoCreado;
	}

	public static boolean creaAtributoNodoSiNoExiste(Document archivoXml, Element nodo, String nombreAtributo,
			String valorAtributo) {

		boolean creado = false;

		try {
			// Obtener el mapa de atributos del nodo
			NamedNodeMap atributos = nodo.getAttributes();

			// Buscar si el atributo ya existe
			Node nodoAtributo = atributos.getNamedItem(nombreAtributo);

			if (nodoAtributo == null) {
				// Si no existe, crear un nuevo atributo y agregarlo al nodo
				Attr atributo = archivoXml.createAttribute(nombreAtributo);
				atributo.setValue(valorAtributo);
				atributos.setNamedItem(atributo);
				creado = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace(); // Manejo básico de excepciones
		}

		return creado;
	}

	public static String ficheroXmlACadena(String pathFichero, String nombreCodificacionSiNoSeLee) {
		String cad = "";
		try {
			// Obtener la codificación
			String codificacion = extraerCodificacionXml(Files.readAllBytes(Paths.get(pathFichero)));
			if (codificacion.isEmpty()) {
				codificacion = nombreCodificacionSiNoSeLee;
			}
			byte[] bytes = Files.readAllBytes(Paths.get(pathFichero));
			cad = new String(bytes, Charset.forName(codificacion));
		} catch (IOException ex) {
			cad = "";
		}
		return cad;
	}

	public static Document xmlLoad(String fichero) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			long ti = System.currentTimeMillis();

			String cad;
			do {
				cad = ficheroXmlACadena(fichero, "ISO-8859-1");
				if (!cad.isEmpty()) {
					doc = dBuilder.parse(Files.newInputStream(Paths.get(fichero)));
					break;
				}
				if (System.currentTimeMillis() - ti > 2000) {
					break;
				}
			} while (true);

		} catch (Exception ex) {
			doc = null;
		}
		return doc;
	}

	public boolean CrearXmlSiNoExiste(String nombreArchivoXML, String nodoRaiz) {

		try {

			File archivoXML = new File(nombreArchivoXML);

			if (Ficheros.FicheroExiste(nombreArchivoXML) == ExistFicheroEnum.NoExiste) {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document xmlDoc = dBuilder.newDocument();

				// createXmlDeclaration
				/*
				 * DOMImplementation domImpl = xmlDoc.getImplementation(); DocumentType doctype
				 * = domImpl.createDocumentType("0.1", "ISO-8859-1", null);
				 * xmlDoc.appendChild(doctype);
				 */

				Element nuevoNodo = xmlDoc.createElement(nodoRaiz);
				// nuevoNodo.appendChild(xmlDoc.createTextNode(""));//texto vacio por cuestiones
				// de formato
				xmlDoc.appendChild(nuevoNodo);

				saveXML(xmlDoc, nombreArchivoXML);

			}

			return true;
		} catch (Exception ex) {
		
			return false;
		}
	}

	private static String nodeToString(Node node) {
		try {
			Document doc = node.getOwnerDocument();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean LeeNodoCentralXMLContenidoVersiones(Document xmlDoc, String nodo, StringBuilder valorNodo) {
		try {
			valorNodo.setLength(0);
			NodeList contenidoVersionesList = xmlDoc.getElementsByTagName(nodo);
			Node contenidoVersionesNode = contenidoVersionesList.item(0);

			String contenidoVersionesString = nodeToString(contenidoVersionesNode);

			valorNodo.append(contenidoVersionesString);
			return true;
		} catch (Exception e) {
			return false;// TODO: handle exception
		}
	}

	public boolean LeeNodoCentralXML(Document xmlDoc, String nodo, StringBuilder valorNodo) {
		valorNodo.setLength(0);
		try {
			String[] partesNodo = nodo.split("/");

			Node nodoActual = xmlDoc.getDocumentElement();

			// Iterar sobre las partes del nodo
			for (String parteNodo : partesNodo) {
				NodeList elementos = ((Element) nodoActual).getElementsByTagName(parteNodo);

				if (elementos.getLength() > 0) {

					nodoActual = elementos.item(0);
					break;
				}
			}

			valorNodo.append(nodoActual.getTextContent());

			return true;
		} catch (Exception ex) {
			valorNodo.setLength(0);
			ex.printStackTrace();
			return false;
		}
	}

	public boolean LeeNodo(Document xmlDoc, String nodo, StringBuilder valorNodo) {
		valorNodo.setLength(0);
		try {
			NodeList elementos = xmlDoc.getElementsByTagName(nodo);
			// Verificar si se encontraron nodos y obtener el contenido del primer nodo
			if (elementos.getLength() > 0) {
				Node Nodo = elementos.item(0);
				String var = Nodo.getTextContent();
				valorNodo.append(var);
			} else {

			}

			return true;
		} catch (Exception ex) {
			valorNodo.setLength(0); // Establecer el valor en blanco en caso de excepción
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean leeNodoXml(Document xmlDoc, String nodo, StringBuilder valor) {
		try {
			Node nodoXml = xmlDoc.getElementsByTagName(nodo).item(0);
			if (nodoXml != null) {
				valor.setLength(0);
				valor.append(nodoXml.getTextContent());
				return true;
			}
		} catch (Exception ex) {
			valor.setLength(0);
		}
		return false;
	}

	public void saveXML(Document doc, String rutaArchivoXML) {

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rutaArchivoXML));

			// Formatear la salida
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "ISO-8859-1");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");// Cualquier cosa cambiar a html
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean existeNodo(Document doc, Element Nodo, String nombreNodo) {

		NodeList nodos;

		if (Nodo == null) {

			nodos = doc.getElementsByTagName(nombreNodo);
		} else {

			nodos = Nodo.getElementsByTagName(nombreNodo);
		}

		boolean confirma = nodos.getLength() > 0;

		return confirma;
	}

}
