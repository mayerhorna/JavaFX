package com.commerceapp.maestros;

//import javax.crypto.CipherMode;
import javax.crypto.Cipher;

import com.commerceapp.util.Formato;

import java.util.ArrayList;
import java.util.List;

public class MaestroModoEncriptacion {

	// private static final CipherMode kModoPorDefecto = CipherMode.ECB;
	public enum CipherMode {
		ECB, CBC, CFB

	}

	public static CipherMode kModoPorDefecto = CipherMode.ECB;

	private CipherMode codigo;
	private String descripcion;
	private boolean usaVectorDeInicializacion;

	public MaestroModoEncriptacion() {
	}

	public CipherMode getCodigo() {
		//ordinalmeda la posicion
		//name me da el nombre
		return codigo;
		
	}

	public void setCodigo(CipherMode value) {
		this.codigo = value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String value) {
		this.descripcion = value;
	}

	public CipherMode getModoPorDefecto() {
		return kModoPorDefecto;
	}

	public boolean isUsaVectorDeInicializacion() {
		return usaVectorDeInicializacion;
	}

	public void setUsaVectorDeInicializacion(boolean value) {
		this.usaVectorDeInicializacion = value;
	}

	// Métodos
	/*
	 * public String obtenerDescripcion(CipherMode codigo) { for
	 * (MaestroModoEncriptacion elemento : obtenerListaMaestro()) { if
	 * (elemento.getCodigo() == codigo) { cargarDatosMaestros(elemento); return
	 * elemento.getDescripcion(); } } return ""; }
	 * 
	 * public CipherMode obtenerCodigoDeDescripcion(String descripcion) { if
	 * (Util.Formato.ValorNulo(descripcion)) { return null; }
	 * 
	 * for (MaestroModoEncriptacion elemento : obtenerListaMaestro()) { if
	 * (elemento.getDescripcion().equalsIgnoreCase(descripcion)) {
	 * cargarDatosMaestros(elemento); return elemento.getCodigo(); } }
	 * 
	 * return null; }
	 * 
	 * public boolean existeCodigo(CipherMode codigo) { for (MaestroModoEncriptacion
	 * elemento : obtenerListaMaestro()) { if (elemento.getCodigo() == codigo) {
	 * return true; } } return false; }
	 * 
	 * public boolean existeDescripcion(String descripcion) { if
	 * (Util.Formato.ValorNulo(descripcion)) { return false; }
	 * 
	 * for (MaestroModoEncriptacion elemento : obtenerListaMaestro()) { if
	 * (elemento.getDescripcion().equalsIgnoreCase(descripcion)) { return true; } }
	 * 
	 * return false; }
	 * 
	 * private void cargarDatosMaestros(MaestroModoEncriptacion elemento) {
	 * this.descripcion = elemento.getDescripcion(); this.usaVectorDeInicializacion
	 * = elemento.isUsaVectorDeInicializacion(); }
	 */
	/*
	 * private List<MaestroModoEncriptacion> obtenerListaMaestro() {
	 * List<MaestroModoEncriptacion> lstLista = new ArrayList<>();
	 * 
	 * try { MaestroModoEncriptacion elemento1 = new MaestroModoEncriptacion();
	 * elemento1.setCodigo(CipherMode.CBC); elemento1.setDescripcion("CBC");
	 * elemento1.setUsaVectorDeInicializacion(true); lstLista.add(elemento1);
	 * 
	 * MaestroModoEncriptacion elemento2 = new MaestroModoEncriptacion();
	 * elemento2.setCodigo(CipherMode.CFB); elemento2.setDescripcion("CFB");
	 * elemento2.setUsaVectorDeInicializacion(true); lstLista.add(elemento2);
	 * 
	 * MaestroModoEncriptacion elemento4 = new MaestroModoEncriptacion();
	 * elemento4.setCodigo(CipherMode.ECB); elemento4.setDescripcion("ECB");
	 * elemento4.setUsaVectorDeInicializacion(false); lstLista.add(elemento4);
	 * 
	 * } catch (Exception ex) { return null; }
	 * 
	 * return lstLista; }
	 */

	public static List<MaestroModoEncriptacion> obtenerListaMaestro() {
		List<MaestroModoEncriptacion> lista = new ArrayList<>();

		try {
			MaestroModoEncriptacion elemento1 = new MaestroModoEncriptacion();
			elemento1.setCodigo(CipherMode.CBC);
			elemento1.setDescripcion("CBC");
			elemento1.setUsaVectorDeInicializacion(true);
			lista.add(elemento1);

			MaestroModoEncriptacion elemento2 = new MaestroModoEncriptacion();
			elemento2.setCodigo(CipherMode.CFB);
			elemento2.setDescripcion("CFB");
			elemento2.setUsaVectorDeInicializacion(true);
			lista.add(elemento2);

			// Modo no compatible ni con AES ni con 3DES
			// MaestroModoEncriptacion elemento3 = new MaestroModoEncriptacion();
			// elemento3.setCodigo(ModoCifrado.CTS);
			// elemento3.setDescripcion("CTS");
			// lista.add(elemento3);

			MaestroModoEncriptacion elemento4 = new MaestroModoEncriptacion();
			elemento4.setCodigo(CipherMode.ECB);
			elemento4.setDescripcion("ECB");
			elemento4.setUsaVectorDeInicializacion(false);
			lista.add(elemento4);

			// Modo no compatible ni con AES ni con 3DES
			// MaestroModoEncriptacion elemento5 = new MaestroModoEncriptacion();
			// elemento5.setCodigo(ModoCifrado.OFB);
			// elemento5.setDescripcion("OFB");
			// lista.add(elemento5);
		} catch (Exception ex) {
			return null;
		}

		return lista;
	}

	public CipherMode obtenerCodigoDeDescripcion(String descripcion) {
		if (Formato.ValorNulo(descripcion)) {
			return null;
		}

		List<MaestroModoEncriptacion> lista = obtenerListaMaestro();

		for (MaestroModoEncriptacion elemento : lista) {
			if (elemento.getDescripcion().equalsIgnoreCase(descripcion)) {
				cargarDatosMaestros(elemento);
				return elemento.getCodigo();
			}
		}

		return null; // O retorna el valor predeterminado que desees para este caso
	}

	private void cargarDatosMaestros(MaestroModoEncriptacion elemento) {
		descripcion = elemento.getDescripcion();
		usaVectorDeInicializacion = elemento.isUsaVectorDeInicializacion();
	}
}
