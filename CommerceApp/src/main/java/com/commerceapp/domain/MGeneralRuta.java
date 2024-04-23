package com.commerceapp.domain;

public class MGeneralRuta {

    public static final String RecursosApp = MGeneral.class.getPackage().getName() + ".RecursosApp.xml";
    public static final String rptPresentacionDatos = MGeneral.class.getPackage().getName() + ".rptPresentacionDatos.rdlc";
    public static final String rptHojaDiagnostico = MGeneral.class.getPackage().getName() + ".rptHojaDiagnostico.rdlc";
    public static final String DirectorioAyuda = System.getProperty("user.dir") + "/Ayuda";
   
}