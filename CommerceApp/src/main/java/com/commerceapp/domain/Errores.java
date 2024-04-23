package com.commerceapp.domain;

import java.util.logging.Logger;

import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.service.StageInitializer;

public class Errores {
	private static final Logger logger = Logger.getLogger(Errores.class.getName());
    private EnumMensajes _Codigo;
    private String _Descripcion;

    // Constructor
    public Errores() {
        Inicializa();
    }

    /**
     * Codigo del error (o código de mensaje), es uno de la enumeración de mensajes indicada en IdiomaC.enumMensajes
     * @return el código del error
     */
    public EnumMensajes getCodigo() {
        return _Codigo;
    }

    /**
     * Descripción del mensaje
     * @return la descripción del mensaje
     */
    public String getDescripcion() {
        return _Descripcion;
    }

    private void Inicializa() {
        _Codigo = EnumMensajes.Excepcion; // Ajusta el valor según tu lógica de inicialización
        _Descripcion = "";
    }

    /**
     * Obtiene la descripción del mensaje correspondiente al QueError recibido.
     * Si MostrarMensaje es true, muestra un messagebox al usuario con el mensaje.
     * @param MostrarMensaje indica si se debe mostrar o no el mensaje
     * @param QueError código del error
     * @param arg0 primer argumento opcional
     * @param arg1 segundo argumento opcional
     */
    public void generaError(boolean MostrarMensaje, EnumMensajes QueError, Object arg0, Object arg1) {
        _Codigo = QueError;
        _Descripcion = IdiomaC.obtenerMensaje(QueError, arg0, arg1,null);

        if (MostrarMensaje) {
        	String argumento0="";
        	String argumento1="";
        	if(arg0!=null) {
        		argumento0=arg0.toString();
        	}
        		
        	if(arg1!=null) {
        		argumento1=arg1.toString();
        	}
           // IdiomaC.MostrarMensaje(QueError, argumento0, argumento1,null);
        }
    }
}