package com.commerceapp.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.commerceapp.gui.custom.combobox.CustomCombobox;
import com.commerceapp.maestros.MaestroCodigoDescripcion;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Formato {
	private static final Logger logger = Logger.getLogger(Formato.class.getName());

	public enum TipoTransfNumeroALetraEnum {
		NumeroCorriente, Importe
	}

	public enum FormatoEnum {
		NumeroCerosIzquierdaConDecimales, NombreEspacioDerecha, FechaDDMMAAAAA, NumeroCerosIzquierda,
		NombreEspacioIzquierda
	}

	public Formato() {

	}

	public static String obtenerDetalleExcepcion(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		pw.println(ex.toString());

		// Descomenta el siguiente bloque si deseas agregar información adicional
		/*
		 * StackTraceElement[] stackTrace = ex.getStackTrace(); if (stackTrace.length >
		 * 1) { StackTraceElement caller = stackTrace[1]; pw.println("En el método: " +
		 * caller.getMethodName()); }
		 * 
		 * if (ex.getCause() != null) { pw.println("Tipo de excepción: " +
		 * ex.getCause().getClass().toString()); pw.println(ex.getMessage() + ". " +
		 * ex.getCause().getMessage()); pw.println(ex.getCause().getSource());
		 * pw.println(ex.getCause().getStackTrace()); } else {
		 * pw.println("Tipo de excepción: " + ex.getClass().toString());
		 * pw.println(ex.getMessage()); pw.println(ex.getSource());
		 * pw.println(ex.getStackTrace()); }
		 */

		return sw.toString();
	}

	public static String formatearXml(Document docXml) {
		try {
			// Creamos un stringWriter donde escribiremos el XML formateado
			StringWriter sw = new StringWriter();

			// Creamos un objeto TransformerFactory
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// Especificamos que el transformer escriba aplicando un formato humano legible
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			// Volcamos el contenido del Document en el stringWriter usando el transformer
			transformer.transform(new DOMSource(docXml), new StreamResult(sw));

			// Devolvemos el XML formateado
			return sw.toString();

		} catch (Exception ex) {
			// Manejar la excepción según tus necesidades
			ex.printStackTrace();
			return null;
		}
	}

	public static String formatearXml(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Crear un documento XML a partir de una cadena (xml)
			// Reemplaza con tu propia cadena XML
			InputSource inputSource = new InputSource(new java.io.StringReader(xml));

			// Cargamos la cadena XML en un Document
			Document docXml = builder.parse(inputSource);

			// Si hay algún problema al cargar el XML, devolvemos la cadena original
			if (docXml == null) {
				return xml;
			}

			// Creamos un stringWriter donde escribiremos el XML formateado
			StringWriter sw = new StringWriter();

			// Creamos un objeto TransformerFactory
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// Especificamos que el transformer escriba aplicando un formato humano legible
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			// Volcamos el contenido del Document en el stringWriter usando el transformer
			transformer.transform(new DOMSource(docXml), new StreamResult(sw));

			// Devolvemos el XML formateado
			return sw.toString();

		} catch (Exception ex) {
			// Manejar la excepción según tus necesidades
			ex.printStackTrace();
			return null;
		}
	}

	public static String cadenaMayusculaUnSoloEspacio(String cadenaOriginal) {
		String devuelta = cadenaOriginal.toUpperCase().trim();
		int i = 0;
		while (i < devuelta.length() - 1) {
			if (devuelta.charAt(i) == ' ' && devuelta.charAt(i + 1) == ' ') {
				devuelta = devuelta.substring(0, i) + devuelta.substring(i + 1);
			} else {
				i++;
			}
		}
		return devuelta;
	}

	public static long convLng(Object objToLng, long longSiConversionNoPosible) {
		try {
			long longSalida;

			if (ValorNulo(objToLng)) {
				return longSiConversionNoPosible;
			}

			if (objToLng instanceof Boolean) {
				if ((Boolean) objToLng) {
					longSalida = 1;
				} else {
					longSalida = 0;
				}
			} else {
				longSalida = Long.parseLong(objToLng.toString());
			}

			return longSalida;

		} catch (Exception ex) {
			return longSiConversionNoPosible;
		}
	}

	public static byte convByte(Object objToByte, byte byteSiConversionNoPosible) {
		try {
			return Byte.parseByte(objToByte.toString());
		} catch (Exception ex) {
			return byteSiConversionNoPosible;
		}
	}

	public static String convStr(Object objToStr, String cadenaSiConversionNoPosible, String formatoFecha) {
		try {
			if (ValorNulo(objToStr)) {
				return cadenaSiConversionNoPosible;
			}

			if (objToStr instanceof Date) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(formatoFecha);
				return dateFormat.format((Date) objToStr);
			} else {
				return objToStr.toString();
			}

		} catch (Exception ex) {
			return cadenaSiConversionNoPosible;
		}
	}

	public static char convChr(Object objToChr, char charSiConversionNoPosible) {
		try {
			return objToChr.toString().charAt(0);
		} catch (Exception ex) {
			return charSiConversionNoPosible;
		}
	}

	public static short convShort(Object objToShort, short shortSiConversionNoPosible) {
		try {
			short shortSalida;

			if (ValorNulo(objToShort)) {
				return shortSiConversionNoPosible;
			}

			if (objToShort instanceof Boolean) {
				if ((Boolean) objToShort) {
					shortSalida = 1;
				} else {
					shortSalida = 0;
				}
			} else {
				shortSalida = Short.parseShort(objToShort.toString());
			}

			return shortSalida;

		} catch (Exception ex) {
			return shortSiConversionNoPosible;
		}
	}

	public static int convInt(Object objToInt, int enteroSiConversionNoPosible) {
		try {
			int enteroSalida;

			if (ValorNulo(objToInt)) {
				return enteroSiConversionNoPosible;
			}

			if (objToInt instanceof Boolean) {
				if ((Boolean) objToInt) {
					enteroSalida = 1;
				} else {
					enteroSalida = 0;
				}
			} else {
				enteroSalida = Integer.parseInt(objToInt.toString());
			}

			return enteroSalida;

		} catch (Exception ex) {
			return enteroSiConversionNoPosible;
		}
	}

	public static double convDbl(Object objToDbl, double doubleSiConversionNoPosible) {
		try {
			double doubleSalida;

			if (ValorNulo(objToDbl)) {
				return doubleSiConversionNoPosible;
			}

			if (objToDbl instanceof Boolean) {
				if ((Boolean) objToDbl) {
					doubleSalida = 1;
				} else {
					doubleSalida = 0;
				}
			} else {
				doubleSalida = Double.parseDouble(objToDbl.toString());
			}

			return doubleSalida;

		} catch (Exception ex) {
			return doubleSiConversionNoPosible;
		}
	}

	public static BigDecimal convDec(Object objToDec, BigDecimal decimalSiConversionNoPosible) {
		try {
			BigDecimal decimalSalida;

			if (ValorNulo(objToDec)) {
				return decimalSiConversionNoPosible;
			}

			if (objToDec instanceof Boolean) {
				if ((Boolean) objToDec) {
					decimalSalida = BigDecimal.ONE;
				} else {
					decimalSalida = BigDecimal.ZERO;
				}
			} else {
				decimalSalida = new BigDecimal(objToDec.toString());
			}

			return decimalSalida;

		} catch (Exception ex) {
			return decimalSiConversionNoPosible;
		}
	}

	public static Date convDate(Object objToDate, Date fechaSiConversionNoPosible) {
		try {
			Date fechaSalida;

			if (ValorNulo(objToDate)) {
				return fechaSiConversionNoPosible;
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			fechaSalida = dateFormat.parse(objToDate.toString());

			return fechaSalida;

		} catch (ParseException | NullPointerException ex) {
			return fechaSiConversionNoPosible;
		}
	}

	public static boolean convBool(Object objToBool, boolean boolSiConversionNoPosible) {
		try {
			boolean boolSalida;

			if (ValorNulo(objToBool)) {
				return boolSiConversionNoPosible;
			}

			boolSalida = Boolean.parseBoolean(objToBool.toString());
			return boolSalida;

		} catch (Exception ex) {
			return boolSiConversionNoPosible;
		}
	}

	public static boolean validaIp(String ip) {
		try {
			Pattern expValida = Pattern.compile("^\\s*\\d+\\.\\d+\\.\\d+\\.\\d+\\s*$");

			Matcher matcher = expValida.matcher(ip);

			if (matcher.matches()) {
				// Compruebo que los números de la IP estén comprendidos entre 0 y 255
				String[] partesIp = ip.split("\\.");

				for (String parte : partesIp) {
					int valor = convInt(parte, -1);
					if (valor < 0 || valor > 255) {
						return false;
					}
				}

				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean validaPuerto(int puerto) {
		try {
			return 0 <= puerto && puerto <= 65535;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean validaEmail(String email) {
		try {
			// Expresión regular simplificada para validar email
			Pattern expReg = Pattern.compile("[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}");

			return expReg.matcher(email).matches();

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean validaTfnoFax(String tfnoFax) {
		try {
			// Verifico que se han introducido 9 números
			Pattern expReg = Pattern.compile("^[0-9]{9}$");

			Matcher matcher = expReg.matcher(tfnoFax.trim().replace(" ", ""));

			return matcher.matches();

		} catch (Exception ex) {
			return false;
		}
	}

	public static String ponComillas(String cadena) {
		try {
			return "\"" + cadena + "\"";
		} catch (Exception ex) {
			return cadena;
		}
	}

	public static boolean validaNif(String nif) {
		try {
			
			String numerosNIF, letraNIF, aux;

			if (nif.trim().length() == 8) {
				nif = nif.trim() + calcularLetraNIF(nif);
			}

			if (nif.trim().length() != 9) {
				return false;
			}

			nif = nif.trim();
			numerosNIF = nif.substring(0, 8);
			letraNIF = calcularLetraNIF(numerosNIF);

			if (letraNIF.length() == 1) {
				aux = numerosNIF + letraNIF;

				return aux.trim().equals(nif.trim());
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean validaCif(String cif) {
		try {
			int suma, control;
			boolean validacion = false;

		
			cif = cif.trim();

			
			if (cif.length() == 9) {

			
				if ("ABCDEFGHPQSKLMXNJUVRWYZ".indexOf(cif.substring(0, 1)) != -1) {

					
					if ("XYZ".indexOf(cif.substring(0, 1)) != -1) {
						if (cif.substring(0, 1).equals("X")) {
							validacion = validaNif("0" + cif.substring(1, 8)); 
						} else if (cif.substring(0, 1).equals("Y")) {
							validacion = validaNif("1" + cif.substring(1, 8)); 
						} else if (cif.substring(0, 1).equals("Z")) {
							validacion = validaNif("2" + cif.substring(1, 8)); 
						}
					} else {
						try {
							suma = Integer.parseInt(cif.substring(2, 3)) + Integer.parseInt(cif.substring(4, 5))
									+ Integer.parseInt(cif.substring(6, 7));

							for (int n = 0; n < 4; n++) {
								suma = suma + 2 * Integer.parseInt(cif.substring(2 * n + 1, 2 * n + 2)) % 10
										+ (int) Math
												.floor(2 * Integer.parseInt(cif.substring(2 * n + 1, 2 * n + 2)) / 10);
							}
						} catch (NumberFormatException e) {
							return false;
						}

						control = 10 - suma % 10;

						if ("KPQSNRW".indexOf(cif.substring(0, 1)) != -1) {
							validacion = cif.substring(8, 9).equals(String.valueOf((char) (64 + control)));
						} else {
							if (control == 10) {
								control = 0;
							}

							try {
								validacion = Integer.parseInt(cif.substring(8, 9)) == control;
							} catch (NumberFormatException e) {
								return false;
							}
						}
					}
				}
			}

			return validacion;

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean validaCifNif(String cifNif) {
		try {
			// En primer lugar, compruebo si me han pasado un CIF, un NIF o ninguno de ellos
			Pattern expRegCif = Pattern.compile("^[a-zA-Z][0-9]{7}[a-zA-Z0-9]$");
			Pattern expRegNif = Pattern.compile("^[0-9]{8}[a-zA-Z]{0,1}$");

			Matcher cifMatcher = expRegCif.matcher(cifNif);
			Matcher nifMatcher = expRegNif.matcher(cifNif);

			if (cifMatcher.matches()) {
				return validaCif(cifNif);
			}

			if (nifMatcher.matches()) {
				return validaNif(cifNif);
			}

			return false;

		} catch (Exception ex) {
			return false;
		}
	}

	public static String calcularLetraNIF(String nif) {
		try {
			if (nif.length() != 8) {
				return "";
			} else {
				return "TRWAGMYFPDXBNJZSQVHLCKET".substring(Integer.parseInt(nif) % 23, Integer.parseInt(nif) % 23 + 1);
			}
		} catch (Exception ex) {
			return "";
		}
	}

	public static boolean esCifDeEmpresa(String cifNif) {
		try {
			Pattern expRegCif = Pattern.compile("^[a-zA-Z][0-9]{7}[a-zA-Z0-9]$");
			Matcher cifMatcher = expRegCif.matcher(cifNif);

			return cifMatcher.matches();

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean verificaProvinciaYCodigoPostal(TextField txtCodigoPostal,
			CustomCombobox<MaestroCodigoDescripcion> cboProvincia) {
		
		try {
			

			// cambiar
			String codigoProvinciaCP = "";

			if (Formato.ValorNulo(txtCodigoPostal.getText()) || cboProvincia.getValue() == null
					|| Formato.ValorNulo(cboProvincia.getValue().getCodigo())) {

			
				return false;
			}

			codigoProvinciaCP = txtCodigoPostal.getText().substring(0, 2);

			return cboProvincia.getValue().getCodigo().toString().equals(codigoProvinciaCP);

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean validaSoloNumeros(String texto) {
		try {
			Pattern expReg = Pattern.compile("^[0-9]*$");
			Matcher matcher = expReg.matcher(texto);

			return matcher.matches();

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean validaSoloLetrasYNumeros(String texto) {
		try {
			Pattern expReg = Pattern.compile("^[A-Za-z0-9]*$");
			Matcher matcher = expReg.matcher(texto);

			return matcher.matches();

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean ValorNulo(Object valor) {
		try {
			// Compruebo si es DBNull o null
			if (valor == null) {
				return true;
			}

			// Si es un string, compruebo si es ""
			if (valor instanceof String) {
				if (((String) valor).isEmpty()) {
					return true;
				} else {
					return false;
				}
			}

			// Si es una fecha, compruebo si es dateTime.MinValue
			if (valor instanceof Date) {
				if ((Date) valor == new Date(Long.MIN_VALUE)) {
					return true;
				} else {
					return false;
				}
			}

			return false;

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isNumeric(String strNum) {

		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;

	}

	public static boolean EsNifDePersona(String CifNif) {
		try {
			String expRegNif = "^[0-9]{8}[a-zA-Z]{0,1}$";

			if (CifNif.matches(expRegNif)) {
				return true;
			}

			return false;

		} catch (Exception ex) {
			// Manejar la excepción según sea necesario
			return false;
		}
	}

}
