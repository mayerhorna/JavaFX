package com.commerceapp.domain.legalizacion;

public class MensajesReglasC {

    public enum Grado {
        EsError,
        EsAviso
    }

    public enum Origen {
        Estructura,
        PrimariasCuestionario,
        PrimariasLibros,
        ImplicitasCuestionario
    }

    private Grado grado;
    private String textoMensaje = "";
    private String campoFoco = "";
    private String codigoRetorno = "";
    private Origen origen;

    public MensajesReglasC() {
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getCampoFoco() {
        return campoFoco;
    }

    public void setCampoFoco(String campoFoco) {
        this.campoFoco = campoFoco;
    }

    public String getCodigoRetorno() {
        return codigoRetorno;
    }

    public void setCodigoRetorno(String codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }
}

