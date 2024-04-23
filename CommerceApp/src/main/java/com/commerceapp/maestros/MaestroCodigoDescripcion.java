package com.commerceapp.maestros;
import java.util.List;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.util.Formato;

public class MaestroCodigoDescripcion implements Comparable {

    private String maestro;
    private String codigo;
    private String descripcion;
    
    public MaestroCodigoDescripcion(String nombreMaestro) {
        this.maestro = nombreMaestro;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String value) {
        this.codigo = value;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String value) {
        this.descripcion = value;
    }
    
    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
    
    @Override
    public int compareTo(Object elOtro) {
        MaestroCodigoDescripcion x = (MaestroCodigoDescripcion) elOtro;

        if (descripcion.compareTo(x.descripcion) > 0) {
            return 1;
        } else if (descripcion.compareTo(x.descripcion) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Obtiene la descripción correspondiente al código recibido La descripción se
     * obtiene en el idioma activo, y si no se puede, en el idioma por defecto
     * 
     * @param codigo Código de solicitud de retención
     * @return Descripción si la encuentra, cadena vacía si no
     */
    public String obtenerDescripcion(String codigo) {
       List<MaestroCodigoDescripcion> lista = MGeneral.Idioma.obtenerListaMaestro(maestro);

        for (MaestroCodigoDescripcion elemento : lista) {
            if (elemento.getCodigo().equals(codigo)) {
                return elemento.getDescripcion();
            }
        }

        return "";
    }

    /**
     * Obtiene el código correspondiente a la descripción recibida
     * 
     * @param descripcion Descripción
     * @return Código si lo encuentra, cadena vacía si no
     */
    public String obtenerCodigoDeDescripcion(String descripcion) {
        if (Formato.ValorNulo(descripcion)) {
            return "";
        }

       List<MaestroCodigoDescripcion> lista = MGeneral.Idioma.obtenerListaMaestro(maestro);

        for (MaestroCodigoDescripcion elemento : lista) {
            if (elemento.getDescripcion().toUpperCase().equals(descripcion.toUpperCase())) {
                return elemento.getCodigo();
            }
        }

        return "";
    }

    /**
     * Comprueba si existe el código recibido
     * 
     * @param codigo Código
     * @return Verdadero si el código existe, falso si no
     */
    public boolean existeCodigo(String codigo) {
       List<MaestroCodigoDescripcion> lista = MGeneral.Idioma.obtenerListaMaestro(maestro);

        for (MaestroCodigoDescripcion elemento : lista) {
            if (elemento.getCodigo().equals(codigo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprueba si existe la descripción recibida
     * 
     * @param descripcion Descripción
     * @return Verdadero si existe, falso en caso contrario
     */
    public boolean existeDescripcion(String descripcion) {
        if (Formato.ValorNulo(descripcion)) {
            return false;
        }

       List<MaestroCodigoDescripcion> lista = MGeneral.Idioma.obtenerListaMaestro(maestro);

        for (MaestroCodigoDescripcion elemento : lista) {
            if (elemento.getDescripcion().equals(descripcion)) {
                return true;
            }
        }

        return false;
    }
    
    
}
