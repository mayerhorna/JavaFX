package com.commerceapp.maestros;

import com.commerceapp.domain.legalizacion.kLegalizacion;

public class TipoLibro implements Comparable<TipoLibro> {

    private kLegalizacion.enumTipoLibro codigo;

    public kLegalizacion.enumTipoLibro getCodigo() {
        return codigo;
    }

    public void setCodigo(kLegalizacion.enumTipoLibro value) {
        this.codigo = value;
    }

    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    private String nombreFichero;

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String value) {
        this.nombreFichero = value;
    }

    private boolean comprobarSolapamientoFechasAperturaYCierre;

    public boolean isComprobarSolapamientoFechasAperturaYCierre() {
        return comprobarSolapamientoFechasAperturaYCierre;
    }

    public void setComprobarSolapamientoFechasAperturaYCierre(boolean value) {
        this.comprobarSolapamientoFechasAperturaYCierre = value;
    }

    private boolean comprobarFechaApertura;

    public boolean isComprobarFechaApertura() {
        return comprobarFechaApertura;
    }

    public void setComprobarFechaApertura(boolean value) {
        this.comprobarFechaApertura = value;
    }

    @Override
    public int compareTo(TipoLibro otro) {
        if (descripcion.compareTo(otro.descripcion) > 0) {
            return 1;
        } else if (descripcion.compareTo(otro.descripcion) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}