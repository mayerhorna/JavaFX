package com.commerceapp.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlTexto {
	private String[][] _arrayCaracteresSustituir = { { "&", "&amp;" }, { "<", "&lt;" }, { ">", "&gt;" },
			{ "\"", "&quot;" }, { "'", "&apos;" } };

	private enum eOperacion {
		Escribir, Leer
	}

	private String _NombreCodificacion;
	private StringBuilder _Cad;

	public StringBuilder getContenido() {
		return _Cad;
	}

	public void setContenido(StringBuilder value) {
		_Cad = value;
	}

	public boolean leeNodo(StringBuilder cad, String PathNodo, StringBuilder ValorNodo, boolean QuitarLoLeido,
			StringBuilder ValorAtributos) {
		boolean leeNodo = false;

		ValorNodo = new StringBuilder();
		ValorAtributos = new StringBuilder();

		int[] posiciones = new int[2];
		int[] posicionesFin = new int[2];
		String NODO;
		StringBuilder CadNodo = new StringBuilder();

		if (Formato.ValorNulo(_Cad))
			return false;
		if (PathNodo.trim().length() == 0)
			return false;

		CadNodo.append(_Cad.toString());

		do {
			if (!PathNodo.startsWith("/"))
				break;
			posiciones[0] = PathNodo.indexOf("/", 1);
			if (posiciones[0] == -1) {
				PathNodo = PathNodo.substring(1);
				break;
			}
			NODO = PathNodo.substring(1, posiciones[0] - 1);
			PathNodo = PathNodo.substring(posiciones[0]);
			if (!leeNodoAux(posiciones, CadNodo, NODO, posicionesFin))
				return false;
		} while (true);

		leeAtributos(_Cad.toString(), PathNodo, ValorAtributos);

		NODO = PathNodo;
		if (!leeNodoAux(posiciones, CadNodo, NODO, posicionesFin))
			return false;

		if (CadNodo.indexOf("<") == -1) {
			ValorNodo.append(sustituirCaracteres(CadNodo.toString(), eOperacion.Leer));
		} else {
			ValorNodo.append(CadNodo);
		}
		StringBuilder nodoValor = new StringBuilder();
		nodoValor.append(valorSiNulo(ValorNodo.toString()));
		ValorNodo = nodoValor;

		if (QuitarLoLeido) {
			// _Cad = _Cad.substring(0, Pos1 - 1) + _Cad.substring(POS2FIN);
		}

		if (!Formato.ValorNulo(ValorNodo)) {
			leeNodo = true;
		}

		return leeNodo;
	}

	public String valorSiNulo(String valor) {
		if (Formato.ValorNulo(valor)) {
			return "";
		} else {
			return valor;
		}
	}

	private boolean leeNodoAux(int[] posiciones, StringBuilder cadNodo, String nodo, int[] posicionesFin) {
		boolean leeNodoAux = false;
//revisar posiciones
		int pos1Aux;
		int pos2Aux;

		posiciones[0] = cadNodo.toString().indexOf("<" + nodo + ">", 0);
		if (posiciones[0] == -1) {
			posiciones[0] = cadNodo.toString().indexOf("<" + nodo + " ", 0);
		}
		if (posiciones[0] == -1)
			return false;
		posicionesFin[0] = cadNodo.toString().indexOf(">", posiciones[0] + 1);
		if (posicionesFin[0] == -1)
			return false;

		pos1Aux = posicionesFin[0];
		pos2Aux = posicionesFin[0] + 1;
		do {
			posiciones[1] = cadNodo.toString().indexOf("</" + nodo + ">", pos2Aux);
			if (posiciones[1] == -1)
				return false;
			posicionesFin[1] = cadNodo.toString().indexOf(">", posiciones[1] + 1);
			if (existeNodoMismoNombre(cadNodo.toString(), nodo, pos1Aux, posicionesFin[1])) {
				pos2Aux = posicionesFin[1] + 1;
			} else {
				break;
			}
		} while (true);
		StringBuilder aux = new StringBuilder();
		aux.append(cadNodo.toString().substring(posicionesFin[0] + 1, posiciones[1] - posicionesFin[0] - 1));
		cadNodo = aux;

		leeNodoAux = true;

		return leeNodoAux;
	}

	private boolean existeNodoMismoNombre(String cadNodo, String nodo, int pos1Fin, int pos2Fin) {
		// Si en leeNodo, se ha localizado el contenido del NODO, pero en ese contenido,
		// se detecta que existe una nueva apertura de un nodo que se llame igual que
		// NODO,
		// entonces del cierre de nodo correspondería a ese y habría que buscar el
		// siguiente (y así recursivamente)

		boolean existeNodoMismoNombre = false;

		int pos1 = pos1Fin + 1;
		int pos2 = pos2Fin + 1;

		// Si en la cadena a partir de pos1Fin no existe ninguna apertura del nodo
		pos1 = cadNodo.indexOf("<" + nodo + ">", pos1Fin);
		if (pos1 == -1) {
			pos1 = cadNodo.indexOf("<" + nodo + " ", pos1Fin);
		}
		if (pos1 == -1)
			return false;

		// Si existe, pero es después de pos2Fin
		if (pos1 >= pos2Fin)
			return false;

		pos1Fin = cadNodo.indexOf(">", pos1 + 1); // Modifica para que la siguiente iteración comience a partir de ahí.

		existeNodoMismoNombre = true;

		return existeNodoMismoNombre;
	}

	private String sustituirCaracteres(String texto, eOperacion operacion) {
		if (Formato.ValorNulo(texto))
			return texto;
		if (texto.trim().length() == 0)
			return texto;

		String resul = "";

		for (int i = 0; i < texto.length(); i++) {
			String caracter = texto.substring(i, i + 1);
			int ascCar = (int) caracter.charAt(0);
			boolean valido = true;

			// Caracteres de control
			if (ascCar <= 31)
				valido = false;
			// Escape (127), < (60), > (62)
			if (ascCar == 127)
				valido = false;

			if (valido)
				resul = resul + caracter;
		}

		for (int i = 0; i < _arrayCaracteresSustituir.length; i++) {
			if (operacion == eOperacion.Escribir) {
				resul = resul.replace(_arrayCaracteresSustituir[i][0], _arrayCaracteresSustituir[i][1]);
			}
			if (operacion == eOperacion.Leer) {
				resul = resul.replace(_arrayCaracteresSustituir[i][1], _arrayCaracteresSustituir[i][0]);
			}
		}

		return resul;
	}

	public boolean leeAtributo(String cad, String nombreAtributo, StringBuilder valorAtributo) {
		int posI;
		String cadenaAtributo;

		valorAtributo.setLength(0);
		boolean leeAtributo = false;

		if (Formato.ValorNulo(nombreAtributo))
			return false;

		cadenaAtributo = nombreAtributo + "=\"";

		posI = cad.indexOf(cadenaAtributo);
		if (posI > 0) {
			posI = posI + cadenaAtributo.length();
			do {
				if (cad.charAt(posI) == '\"')
					break;
				valorAtributo.append(cad.charAt(posI));
				posI = posI + 1;
			} while (true);
		}

		if (!Formato.ValorNulo(valorAtributo.toString()))
			leeAtributo = true;

		return leeAtributo;
	}

	public boolean leeAtributos(String cad, String nombreNodo, StringBuilder valor) {
		int posI, posF;

		valor.setLength(0);
		boolean leeAtributos = false;

		if (nombreNodo.trim().equals(""))
			return false;

		posI = cad.indexOf("<" + nombreNodo);
		if (posI > 0) {
			posI = posI + ("<" + nombreNodo).length() + 1;
			posF = cad.indexOf(">", posI);
			if (posF > 0)
				valor.append(cad, posI, posF - posI);
		}

		if (!Formato.ValorNulo(valor.toString()))
			leeAtributos = true;

		return leeAtributos;
	}

	public void escribeAtributo(String nombreAtributo, String valorAtributo, boolean cerrarNombreNodo) {
		_Cad.append(" ").append(nombreAtributo.trim()).append("=").append("\"").append(valorAtributo.trim())
				.append("\"");
		if (cerrarNombreNodo)
			_Cad.append(">");
	}

	public void escribeTexto(String valor, boolean esContenidoSimple) {
		if (esContenidoSimple)
			valor = sustituirCaracteres(valor, eOperacion.Escribir);
		_Cad.append(valor);
	}

	public void escribeFinNombreNodo() {
		_Cad.append(">");
	}

	public void escribeFinNodo(String nombreNodo) {
		escribeTexto("</" + nombreNodo + ">", false);
	}

	public void escribeInicioNodo(String nombreNodo, boolean cerrarNombreNodo) {
		_Cad.append("<").append(nombreNodo);
		if (cerrarNombreNodo)
			_Cad.append(">");
	}

	public void escribeNodo(String nombreNodo, String valorNodo, boolean esContenidoSimple) {
		escribeInicioNodo(nombreNodo, true);
		escribeTexto(valorNodo, esContenidoSimple);
		escribeFinNodo(nombreNodo);
	}

	public void escribeInicioXml(String nombreCodificacion) {
		escribeTexto("<?xml", false);
		escribeAtributo("version", "1.0", false);
		escribeAtributo("encoding", nombreCodificacion, false);
		escribeTexto("?", false);
		escribeFinNombreNodo();
		_NombreCodificacion = nombreCodificacion;
	}

	public void escribeSalto() {
		_Cad.append(System.lineSeparator());
	}

	public String normalizar() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(_Cad.toString()));
			Document xml = builder.parse(is);

			xml.normalize();

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(xml), new StreamResult(writer));

			return writer.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean guardarEnFichero(String pathFichero, String nombreCodificacion) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(pathFichero), nombreCodificacion))) {
			writer.write(_Cad.toString());
			writer.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
}
