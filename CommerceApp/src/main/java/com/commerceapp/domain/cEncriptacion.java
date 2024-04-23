package com.commerceapp.domain;

import java.awt.RenderingHints.Key;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;

import java.util.Arrays;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import javax.crypto.Cipher;

import javax.crypto.CipherInputStream;

import javax.crypto.CipherOutputStream;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;

import com.commerceapp.maestros.MaestroAlgoritmoEncriptacion;
import com.commerceapp.maestros.MaestroModoEncriptacion;
import com.commerceapp.util.Formato;

public class cEncriptacion {

	private static final String kCOMPILACION = "LEGALIA2";

	public static String filtroDes = "*.3DES";

	public enum eAccion {

		Encriptar(1), Desencriptar(2);

		private final int valor;

		eAccion(int valor) {

			this.valor = valor;

		}

		public int getValor() {

			return valor;

		}

	}

	// Variables

	private String _Clave;

	private MaestroAlgoritmoEncriptacion _Algoritmo = new MaestroAlgoritmoEncriptacion();

	private MaestroModoEncriptacion _Modo = new MaestroModoEncriptacion();

	private static String _FicheroEntrada;

	private static String _FicheroSalida;

	private eAccion _Accion;

	private byte[] _bytKey;

	private String _strHexKey;

	private byte[] _bytIV;

	private String _strIV;

	private String _strHexIV;

	private boolean _UltimaAccionCorrecta = false;

	// Constructor

	public cEncriptacion() {

	}

	public String getClave() {

		return _Clave;

	}

	public MaestroAlgoritmoEncriptacion getAlgoritmo() {

		return _Algoritmo;

	}

	public MaestroModoEncriptacion getModo() {

		return _Modo;

	}

	public static String getFicheroEntrada() {

		return _FicheroEntrada;

	}

	public static String getFicheroSalida() {

		return _FicheroSalida;

	}

	public void setFicheroSalida(String value) {

		_FicheroSalida = value;

	}

	public void setFicheroEntrada(String value) {

		_FicheroEntrada = value;

	}

	public eAccion getAccion() {

		return _Accion;

	}

	public String getClaveHexadecimal() {

		return _strHexKey;

	}

	public String getVectorInicializacionHexadecimal() {

		return _strHexIV;

	}

	public boolean getUltimaAccionCorrecta() {

		return _UltimaAccionCorrecta;

	}

	private String toHex(byte[] ba) {

		if (ba == null || ba.length == 0) {

			return "";

		}

		StringBuilder sb = new StringBuilder();

		for (byte b : ba) {

			sb.append(String.format("%02X", b));

		}

		return sb.toString();

	}

	// Método CrearKey

	private void crearKey() {

		byte[] key = _Clave.getBytes(StandardCharsets.UTF_8);

		int keySize = _Algoritmo.getTamanioKey();

		key = Arrays.copyOf(key, keySize);

		_bytKey = key;

		_strHexKey = toHex(_bytKey);

	}

	private boolean crearIV(StringBuilder cadError) {

		try {

			cadError.setLength(0);

			byte[] bytIV = null;

			MessageDigest sha512 = MessageDigest.getInstance("SHA-512");

			byte[] bytResult = sha512.digest(_Clave.getBytes(StandardCharsets.UTF_8));

			int byteInicial = 0;

			bytIV = Arrays.copyOfRange(bytResult, byteInicial, byteInicial + _Algoritmo.getTamanioIv());

			_bytIV = bytIV;

			_strHexIV = toHex(_bytIV);

			_strIV = new String(_bytIV, StandardCharsets.UTF_8);

			return true;

		} catch (NoSuchAlgorithmException ex) {

			cadError.append(ex.getMessage());

			ex.printStackTrace();

			return false;

		}

	}

	private static byte[] leerArchivo(String nombreArchivo) throws IOException {

		FileInputStream fis = new FileInputStream(nombreArchivo);

		byte[] contenido = fis.readAllBytes();

		fis.close();

		return contenido;

	}

	private static void guardarArchivo(byte[] contenido, String nombreArchivo) throws IOException {

		FileOutputStream fos = new FileOutputStream(nombreArchivo);

		fos.write(contenido);

		fos.close();

	}

	public boolean encriptarOdesencriptarArchivo(String error, Progreso progreso, int tamanioTotal, int tamanioActual) {

		FileInputStream fis = null;

		FileOutputStream fos = null;

		CipherOutputStream cos = null;

		boolean progresoParaUnSoloArchivo = true;

		try {

			if (kCOMPILACION.equals("LEGALIA2")) {

				error = "";

				fis = new FileInputStream(_FicheroEntrada);

				fos = new FileOutputStream(_FicheroSalida);

				fos.getChannel().truncate(0);

				long longitudArchivo = new File(_FicheroEntrada).length();

				byte[] contenido = null;

				byte[] bytesMensaje = null;

				if (tamanioTotal == 0) {

					tamanioTotal = (int) longitudArchivo;

					tamanioActual = 0;

				} else {

					progresoParaUnSoloArchivo = false;

				}

				switch (_Algoritmo.getCodigo()) {

				case Aes128:

					switch (_Accion) {

					case Encriptar:

						Cipher cipherEncrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherEncrypt);

						break;

					case Desencriptar:

						Cipher cipherDecrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherDecrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherDecrypt);

						break;

					default:

						break;

					}

					break;

				case Aes192:

					switch (_Accion) {

					case Encriptar:

						Cipher cipherEncrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherEncrypt);

						break;

					case Desencriptar:

						Cipher cipherDecrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherDecrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherDecrypt);

						break;

					default:

						break;

					}

					break;

				case Aes256:

					switch (_Accion) {

					case Encriptar:

						Cipher cipherEncrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherEncrypt);

						break;

					case Desencriptar:

						Cipher cipherDecrypt = Cipher

								.getInstance("AES/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherDecrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(_bytKey, "AES"));

						cos = new CipherOutputStream(fos, cipherDecrypt);

						break;

					default:

						break;

					}

					break;

				case TripleDES:

					switch (_Accion) {

					case Encriptar:

						Cipher cipherEncrypt = Cipher

								.getInstance("DESede/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(_bytKey, "DESede"));

						cos = new CipherOutputStream(fos, cipherEncrypt);

						break;

					case Desencriptar:

						Cipher cipherDecrypt = Cipher

								.getInstance("DESede/" + _Modo.getCodigo().toString() + "/PKCS5Padding");

						cipherDecrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(_bytKey, "DESede"));

						cos = new CipherOutputStream(fos, cipherDecrypt);

						break;

					default:

						break;

					}

					break;

				default:

					break;

				}

				contenido = fis.readAllBytes();

				fis.close();

				bytesMensaje = contenido;

				cos.write(bytesMensaje);

				cos.close();

				_UltimaAccionCorrecta = true;

				return true;

			} else {

				return false;

			}

		} catch (Exception ex) {

			error = ex.getMessage();

			ex.printStackTrace();

			return false;

		} finally {

			if (progresoParaUnSoloArchivo && progreso != null) {

				progreso.procesoTerminado();

			}

			try {

				if (fis != null)

					fis.close();

				if (fos != null)

					fos.close();

				if (cos != null)

					cos.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	private void cancelaAccion(InputStream fsInput, OutputStream fsOutput, CipherOutputStream csCryptoStream) {

		try {

			if (csCryptoStream != null) {

				csCryptoStream.close();

			}

			if (fsInput != null) {

				fsInput.close();

			}

			if (fsOutput != null) {

				fsOutput.close();

			}

		} catch (IOException ex) {

			// Manejar la excepción según sea necesario

		}

	}

	public boolean encriptarFichero(String pFicheroEntrada, String pDirectorioSalida, String pNombreFicheroSalida,

			String pClave, String pAlgoritmoDescripcion, String pModoDescripcion, StringBuilder cadError,

			Progreso vProgreso, int sizeTotal, int sizeActual) {

		try {

			cadError.setLength(0);

			_UltimaAccionCorrecta = false;

			if (!new File(pFicheroEntrada).exists()) {

				return false;

			}

			_Clave = pClave;

			_Algoritmo.setCodigo(_Algoritmo.obtenerCodigoDeDescripcion(pAlgoritmoDescripcion));

		

			if (_Algoritmo.getCodigo() == null) {

				return false;

			}

			_Modo.setCodigo(_Modo.obtenerCodigoDeDescripcion(pModoDescripcion));

			if (_Modo.getCodigo() == null) {

				return false;

			}

		

			_FicheroEntrada = pFicheroEntrada;

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				_FicheroSalida = pDirectorioSalida + "\\" + pNombreFicheroSalida;

			} else if (os.contains("nux")) {

				_FicheroSalida = pDirectorioSalida + "/" + pNombreFicheroSalida;

			}

			_FicheroSalida = _FicheroSalida + "." + _Modo.getDescripcion() + "." + _Algoritmo.getDescripcion();

			_Accion = eAccion.Encriptar;

			crearKey();

			if (!crearIV(cadError)) {

				return false;

			}

			return encriptarOdesencriptarArchivo(pAlgoritmoDescripcion, vProgreso, sizeTotal, sizeActual);

		} catch (Exception ex) {

			cadError.append(ex.getMessage());

			ex.printStackTrace();

			return false;

		}

	}

	public boolean desencriptarFichero(String pFicheroEntrada, String pDirectorioSalida,

			String pPrefijoNombreFicheroSalida, String pClave, StringBuilder cadError, Progreso vProgreso) {

		try {

			cadError.setLength(0);

			_UltimaAccionCorrecta = false;

			if (!new File(pFicheroEntrada).exists()) {

				return false;

			}

			StringBuilder nombreFichero = new StringBuilder();

			if (!obtenerDatosEncriptacionSegunNombreFichero(pFicheroEntrada, nombreFichero)) {

				return false;

			}

			_Clave = pClave;

			_FicheroEntrada = pFicheroEntrada;

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				_FicheroSalida = pDirectorioSalida + "\\" + pPrefijoNombreFicheroSalida

						+ nombreFichero.toString();

			} else if ((os.contains("nux"))) {

				_FicheroSalida = pDirectorioSalida + "/" + pPrefijoNombreFicheroSalida + nombreFichero.toString();

			}

			_Accion = eAccion.Desencriptar;

			crearKey();

			if (!crearIV(cadError)) {

				return false;

			}

			return encriptarOdesencriptarArchivo(pClave, vProgreso, 0, 0);

		} catch (Exception ex) {

			cadError.append(ex.getMessage());

			ex.printStackTrace();

			return false;

		}

	}

	private static String obtenerExtension(String nombreArchivo) {

		int puntoIndex = nombreArchivo.lastIndexOf('.');

		if (puntoIndex > 0 && puntoIndex < nombreArchivo.length() - 1) {

			return nombreArchivo.substring(puntoIndex);

		} else {

			return null;

		}

	}

	private static String obtenerExtensionSegundo(String nombreArchivo) {

		int ultimoPuntoIndex = nombreArchivo.lastIndexOf('.');

		// Asegurarse de que el punto está presente y no es el último carácter en el

		// nombre del archivo

		if (ultimoPuntoIndex > 0 && ultimoPuntoIndex < nombreArchivo.length() - 1) {

			int penultimoPuntoIndex = nombreArchivo.lastIndexOf('.', ultimoPuntoIndex - 1);

			if (penultimoPuntoIndex >= 0) {

				return nombreArchivo.substring(penultimoPuntoIndex, ultimoPuntoIndex);

			}

		}

		return null;

	}

	private static String obtenerNombreArchivo(String cadena) {

		// Encuentra la última aparición del punto para identificar la extensión

		int ultimaAparicionPunto = cadena.lastIndexOf(".");

		if (ultimaAparicionPunto != -1) {

			// Extrae el nombre del archivo hasta la última aparición del punto

			return cadena.substring(0, ultimaAparicionPunto);

		} else {

			// En caso de no encontrar un punto (sin extensión)

			return cadena;

		}

	}

	public boolean obtenerDatosEncriptacionSegunNombreFichero(String pPathFichero, StringBuilder nombreFichero) {

		try {

			String extension, extensionSinPunto, nombre;

			nombreFichero.setLength(0);

			if (pPathFichero.isEmpty()) {

				return false;

			}

			nombre = new File(pPathFichero).getName();

			extension = obtenerExtension(nombre);

			if (extension.isEmpty()) {

				return false;

			}

			extensionSinPunto = extension.substring(1);

			_Algoritmo.setCodigo(_Algoritmo.obtenerCodigoDeDescripcion(extensionSinPunto));

			if (_Algoritmo.getCodigo().ordinal() == -1) {

				return false;

			}

			nombre = new File(pPathFichero).getName();

			extension = obtenerExtensionSegundo(nombre);

			if (extension.isEmpty()) {

				return false;

			}

			extensionSinPunto = extension.substring(1);

			_Modo.setCodigo(_Modo.obtenerCodigoDeDescripcion(extensionSinPunto));

			if (_Modo.getCodigo() == null) {

				return false;

			}

			nombre = new File(nombre).getName();

			nombre = obtenerNombreArchivo(obtenerNombreArchivo(nombre));

			nombreFichero.append(nombre);

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public String obtenerExtensionesEncriptacion(String pAlgoritmoDescripcion, String pModoDescripcion,

			StringBuilder cadError) {

		try {

			cadError.setLength(0);

			_Algoritmo.setCodigo(_Algoritmo.obtenerCodigoDeDescripcion(pAlgoritmoDescripcion));

			if (_Algoritmo.getCodigo().ordinal() == 0) {

				return "";

			}

			/*
			 * 
			 * _Modo.setCodigo(_Modo.obtenerCodigoDeDescripcion(pModoDescripcion)); if
			 * 
			 * (_Modo.getCodigo() == 0) { return ""; }
			 * 
			 */

			return "." + _Modo.getDescripcion() + "." + _Algoritmo.getDescripcion();

		} catch (Exception ex) {

			cadError.append(ex.getMessage());

			ex.printStackTrace();

			return "";

		}

	}

	public boolean asignarParametrosEncriptacion(String pClave, String pAlgoritmoDescripcion, String pModoDescripcion,

			StringBuilder cadError) {

		try {

			_Clave = pClave;

			_Algoritmo.setCodigo(_Algoritmo.obtenerCodigoDeDescripcion(pAlgoritmoDescripcion));

			if (_Algoritmo.getCodigo().ordinal() == 0) {

				return false;

			}

		
			crearKey();

			if (!crearIV(cadError)) {

				return false;

			}

			return true;

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean tamanioClaveValido(String pClave, String pAlgoritmoDescripcion) {

		try {

			int tamanioClave = pClave.length();

			_Clave = pClave;

			_Algoritmo.setCodigo(_Algoritmo.obtenerCodigoDeDescripcion(pAlgoritmoDescripcion));

			if (_Algoritmo.getCodigo() == null) {

				return false;

			}

			if (tamanioClave >= _Algoritmo.getTamanioMinimoKey() && tamanioClave <= _Algoritmo.getTamanioKey()) {

				System.out.print(true);

			} else {

				System.out.print(false);

			}

			return tamanioClave >= _Algoritmo.getTamanioMinimoKey() && tamanioClave <= _Algoritmo.getTamanioKey();

		} catch (Exception ex) {

			return false;

		}

	}

	public boolean reglasClaveValidas(String pClave) {

		try {

			if (Formato.ValorNulo(pClave)) {

				return false;

			}

			Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$");

			Matcher matcher = pattern.matcher(pClave);

			return matcher.matches();

		} catch (Exception ex) {

			return false;

		}

	}

	public String datosEncriptacionDesarrollo() {

		try {

			String cad = "Algoritmo: " + _Algoritmo.getDescripcion() + System.lineSeparator();

			cad += "Modo: " + _Modo.getDescripcion() + System.lineSeparator();

			cad += "Clave Hex: " + _strHexKey + System.lineSeparator();

			cad += "VI str: " + _strIV + System.lineSeparator();

			cad += "VI Hex: " + _strHexIV + System.lineSeparator();

			return cad;

		} catch (Exception ex) {

			ex.printStackTrace();

			return ex.getMessage();

		}

	}

	public String obtenerResultadoEncriptacion() {

		String cad;

		String cadvi = "";

		if (_Modo.isUsaVectorDeInicializacion()) {

			cadvi = MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.VectorInicializacion, _strHexIV, null, null);

		}

		if (!cadvi.isEmpty()) {

			cadvi = System.lineSeparator() + cadvi;

			cad = MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.ResultadoEncriptacion,

					_Algoritmo.getDescripcion(), _Modo.getDescripcion(), cadvi);

		} else {

			cad = MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.ResultadoEncriptacion,

					_Algoritmo.getDescripcion(), _Modo.getDescripcion(), null);

		}

		return cad;

	}

}
