package com.commerceapp.maestros;

import java.util.ArrayList;
import java.util.List;

import com.commerceapp.util.Formato;

public class MaestroAlgoritmoEncriptacion {


	    // Constructor
	    public MaestroAlgoritmoEncriptacion() {
	    }

	    // Enumeración del los algoritmos utilizados
	    public enum eAlgoritmoEncriptacion {
	        Aes128, Aes192, Aes256, TripleDES
	    }

	    // Constantes
	    private static final eAlgoritmoEncriptacion kAlgoritmoPorDefecto = eAlgoritmoEncriptacion.Aes128;

	    // Tamaños en bytes de la clave, dependiendo del algoritmo
	    private static final int kTamanioKeyAES128 = 16;
	    private static final int kTamanioKeyAES192 = 24;
	    private static final int kTamanioKeyAES256 = 32;
	    private static final int kTamanioKeyTripleDES = 24;

	    // Tamaños en bytes del vector de inicialización, dependiendo del algoritmo (todos los AES tienen el mismo)
	    private static final int kTamanioIvAES = 16;
	    private static final int kTamanioIvDES = 8;

	    // Tamaños en bytes mínimos de clave, que se van a exigir dependiendo del algoritmo
	    private static final int kTamanioMinimoKeyAES128 = 6;
	    private static final int kTamanioMinimoKeyAES192 = 12;
	    private static final int kTamanioMinimoKeyAES256 = 16;
	    private static final int kTamanioMinimoKeyTripleDES = 12;
	
	    private eAlgoritmoEncriptacion _codigo;
	    private String _descripcion;
	    private int _tamanioKey;
	    private int _tamanioIv;
	    private int _tamanioMinimoKey;
	    
	    public eAlgoritmoEncriptacion getCodigo() {
	        return _codigo;
	    }

	    public void setCodigo(eAlgoritmoEncriptacion value) {
	        _codigo = value;
	    }	    

	    public String getDescripcion() {
	        return _descripcion;
	    }

	    public void setDescripcion(String value) {
	        _descripcion = value;
	    }	    

	    public int getTamanioKey() {
	        return _tamanioKey;
	    }

	    public void setTamanioKey(int value) {
	        _tamanioKey = value;
	    }   

	    public int getTamanioIv() {
	        return _tamanioIv;
	    }

	    public void setTamanioIv(int value) {
	        _tamanioIv = value;
	    }
	    

	    public int getTamanioMinimoKey() {
	        return _tamanioMinimoKey;
	    }

	    public void setTamanioMinimoKey(int value) {
	        _tamanioMinimoKey = value;
	    }

	    // Propiedad de solo lectura
	    public eAlgoritmoEncriptacion getAlgoritmoPorDefecto() {
	        return kAlgoritmoPorDefecto;
	    }
	
	    public String obtenerDescripcion(eAlgoritmoEncriptacion codigo) {
	        List<MaestroAlgoritmoEncriptacion> lista = obtenerListaMaestro();

	        for (MaestroAlgoritmoEncriptacion elemento : lista) {
	            if (elemento.getCodigo() == codigo) {
	                cargarDatosMaestros(elemento);
	                return elemento.getDescripcion();
	            }
	        }

	        return "";
	    }
	    public eAlgoritmoEncriptacion obtenerCodigoDeDescripcion(String descripcion) {
	        
	    	if (Formato.ValorNulo(descripcion)) {
	            return null; // Asegúrate de tener un valor nulo definido en tu enumeración
	        }

	        List<MaestroAlgoritmoEncriptacion> lista = obtenerListaMaestro();

	        for (MaestroAlgoritmoEncriptacion elemento : lista) {
	            if (elemento.getDescripcion().equalsIgnoreCase(descripcion)) {
	                cargarDatosMaestros(elemento);
	                return elemento.getCodigo();
	            }
	        }

	        return null; // Otra vez, asegúrate de tener un valor nulo definido
	    }
	    
	    public boolean existeCodigo(eAlgoritmoEncriptacion codigo) {
	        List<MaestroAlgoritmoEncriptacion> lista = obtenerListaMaestro();

	        for (MaestroAlgoritmoEncriptacion elemento : lista) {
	            if (elemento.getCodigo() == codigo) {
	                return true;
	            }
	        }

	        return false;
	    }
	    
	    public boolean existeDescripcion(String descripcion) {
	        if (Formato.ValorNulo(descripcion)) {
	            return false;
	        }

	        List<MaestroAlgoritmoEncriptacion> lista = obtenerListaMaestro();

	        for (MaestroAlgoritmoEncriptacion elemento : lista) {
	            if (elemento.getDescripcion().equals(descripcion)) {
	                return true;
	            }
	        }

	        return false;
	    }
	    
	    public List<MaestroAlgoritmoEncriptacion> obtenerListaMaestro() {
	        List<MaestroAlgoritmoEncriptacion> lista = new ArrayList<>();

	        try {
	            MaestroAlgoritmoEncriptacion elemento1 = new MaestroAlgoritmoEncriptacion();
	            elemento1.setCodigo(eAlgoritmoEncriptacion.Aes128);
	            elemento1.setDescripcion("AES128");
	            elemento1.setTamanioKey(kTamanioKeyAES128);
	            elemento1.setTamanioIv(kTamanioIvAES);
	            elemento1.setTamanioMinimoKey(kTamanioMinimoKeyAES128);
	            lista.add(elemento1);

	            MaestroAlgoritmoEncriptacion elemento2 = new MaestroAlgoritmoEncriptacion();
	            elemento2.setCodigo(eAlgoritmoEncriptacion.Aes192);
	            elemento2.setDescripcion("AES192");
	            elemento2.setTamanioKey(kTamanioKeyAES192);
	            elemento2.setTamanioIv(kTamanioIvAES);
	            elemento2.setTamanioMinimoKey(kTamanioMinimoKeyAES192);
	            lista.add(elemento2);

	            MaestroAlgoritmoEncriptacion elemento3 = new MaestroAlgoritmoEncriptacion();
	            elemento3.setCodigo(eAlgoritmoEncriptacion.Aes256);
	            elemento3.setDescripcion("AES256");
	            elemento3.setTamanioKey(kTamanioKeyAES256);
	            elemento3.setTamanioIv(kTamanioIvAES);
	            elemento3.setTamanioMinimoKey(kTamanioMinimoKeyAES256);
	            lista.add(elemento3);

	            MaestroAlgoritmoEncriptacion elemento4 = new MaestroAlgoritmoEncriptacion();
	            elemento4.setCodigo(eAlgoritmoEncriptacion.TripleDES);
	            elemento4.setDescripcion("3DES");
	            elemento4.setTamanioKey(kTamanioKeyTripleDES);
	            elemento4.setTamanioIv(kTamanioIvDES);
	            elemento4.setTamanioMinimoKey(kTamanioMinimoKeyTripleDES);
	            lista.add(elemento4);

	        } catch (Exception ex) {
	            return null;
	        }

	        return lista;
	    }
	    private void cargarDatosMaestros(MaestroAlgoritmoEncriptacion elemento) {
	        _descripcion = elemento.getDescripcion();
	        _tamanioKey = elemento.getTamanioKey();
	        _tamanioIv = elemento.getTamanioIv();
	        _tamanioMinimoKey = elemento.getTamanioMinimoKey();
	    }
	    
	    
}
