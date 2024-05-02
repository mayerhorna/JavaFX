package com.commerceapp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.commerceapp.domain.ConfiguracionC;

@Component
public class ConfiguracionService {
	private static final Logger logger = Logger.getLogger(ConfiguracionService.class.getName());

	/* XML */
	private static void escribirXML(Document doc, String rutaArchivoXML) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "ISO-8859-1");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "html");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rutaArchivoXML));

			// Formatear la salida
			

			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Metodo para escribir XML
	public static void escribirXML(ConfiguracionC datos, String rutaArchivoXML) {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Crear un elemento raíz
			Element configuracion = doc.createElement("Configuracion");
			configuracion.setAttribute("version", "1.5.6");
			doc.appendChild(configuracion);

			// elemento Legalia2

			Element legalia2 = doc.createElement("Legalia2");
			
			configuracion.appendChild(legalia2); // elemento ValoresDefectoPresentante

			// Agregar elementos con datos
			/*agregarElemento(doc, legalia2, "Legalia2", "Juan");
			agregarElemento(doc, legalia2, "Apellido", "Pérez");

			// Agregar más elementos según sea necesario...
			Element valoresDefectoPresentante = doc.createElement("ValoresDefectoPresentante");
			legalia2.appendChild(valoresDefectoPresentante);
			agregarElemento(doc, valoresDefectoPresentante, "test", "test");*/

			// Escribir el documento XML en un archivo
			escribirXML(doc, "testeo.xml");

			

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static void agregarElemento(Document doc, Element parent, String nombreElemento, String valor) {
		Element elemento = doc.createElement(nombreElemento);
		elemento.appendChild(doc.createTextNode(valor));
		parent.appendChild(elemento);
	}

	

	public String obtenerTexto() {
		return "Configuracion";
	}

}
