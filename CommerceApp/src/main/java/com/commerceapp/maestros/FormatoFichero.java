package com.commerceapp.maestros;

import com.commerceapp.domain.legalizacion.kLegalizacion;

public class FormatoFichero implements Comparable<FormatoFichero> {

    private kLegalizacion.enumExtensionFichero codigo;

    public kLegalizacion.enumExtensionFichero getCodigo() {
        return codigo;
    }

    public void setCodigo(kLegalizacion.enumExtensionFichero value) {
        this.codigo = value;
    }

    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String value) {
        this.extension = value;
    }

    private boolean esDeEncriptacion;

    public boolean isEsDeEncriptacion() {
        return esDeEncriptacion;
    }

    public void setEsDeEncriptacion(boolean value) {
        this.esDeEncriptacion = value;
    }

    @Override
    public int compareTo(FormatoFichero otro) {
        // Se utiliza el prefijo EsDeEncriptacion para que aparezcan primero todos los formatos que no son de encriptación,
        // y al final los de encriptación
        if ((Boolean.toString(esDeEncriptacion) + descripcion).compareTo(Boolean.toString(otro.esDeEncriptacion) + otro.descripcion) > 0) {
            return 1;
        } else if ((Boolean.toString(esDeEncriptacion) + descripcion).compareTo(Boolean.toString(otro.esDeEncriptacion) + otro.descripcion) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}

