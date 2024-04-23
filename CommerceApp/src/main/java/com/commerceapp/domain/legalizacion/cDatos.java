package com.commerceapp.domain.legalizacion;

import java.util.Objects;
import java.util.logging.Logger;

import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Formato;

public class cDatos {
	private static final Logger logger = Logger.getLogger(cDatos.class.getName());
	private String _PathDatos;
	private kLegalizacion.enumFormato _Formato;
	private String _Descripcion;
	private int _Ejercicio;
	private String _Enviado;
	private String _NombreZip;
	private String _IdentificadorEntrada;
	private String _TipoPersona;
	private String _VersionLegalia2Generacion;
	private String _eDocNumeroDocumento;
	private String _eDocIdTramite;
	private String _eDocNombreFicheroEnviado;
	private String _eDocNombreFicheroAcuseEntrada;
	private String _eDocNombreFicheroNE;
	private String _eDocEntradaTipo;
	private String _eDocEntradaSubsanada;

	private String _EnvioReintentable;
	private String _CodAccesoNif;
	private String _PresentanteNombreConfirmado;
	private String _PresentanteApellidosConfirmados;
	private String _PresentanteCorreoElectronicoConfirmado;

	public enum eEstadoEnvio {
		SinEnviar, EnviadaPorServicioCorrectamente, EnviadaPorPortal, EnviadaPorServicioErrorReintentable,
		EnviadaPorServicioErrorNoReintentable
	}
	

//Metodos
	public cDatos() {
		inicializa();
	}

	public cDatos(String nombreDirectorio, String get_Descripcion, int get_Ejercicio) {
		
		inicializa();
		this._PathDatos = nombreDirectorio;
		this._Descripcion = get_Descripcion;
		this._Ejercicio = get_Ejercicio;
		
	}

	public void inicializa() {
		this._PathDatos = "";
		 this._Formato = null; // Ajusta según la enumeración		
		this._Descripcion = "";
		this._Ejercicio = 0;
		this._Enviado = "";
		this._NombreZip = "";
		this._IdentificadorEntrada = "";
		this._TipoPersona = "";
		this._VersionLegalia2Generacion = "";
		this._eDocNumeroDocumento = "";
		this._eDocIdTramite = "";
		this._eDocNombreFicheroEnviado = "";
		this._eDocNombreFicheroAcuseEntrada = "";
		this._eDocNombreFicheroNE = "";
		this._eDocEntradaTipo = "";
		this._eDocEntradaSubsanada = "";
		this._EnvioReintentable = "";
		this._CodAccesoNif = "";
		this._PresentanteNombreConfirmado = "";
		this._PresentanteApellidosConfirmados = "";
		this._PresentanteCorreoElectronicoConfirmado = "";
	}

//Getters y setters

	public String get_PathDatos() {
		return _PathDatos;
	}

	public void set_PathDatos(String _PathDatos) {
		this._PathDatos = _PathDatos;
	}

	public String get_Descripcion() {
		return _Descripcion;
	}

	public void set_Descripcion(String _Descripcion) {
		this._Descripcion = _Descripcion;
	}

	public int get_Ejercicio() {
		return _Ejercicio;
	}

	public void set_Ejercicio(int _Ejercicio) {
		this._Ejercicio = _Ejercicio;
	}

	public String get_Enviado() {
		return _Enviado;
	}

	public void set_Enviado(String _Enviado) {

		if ("False".equals(_Enviado)) {
			this._Enviado = ""; // Inicialmente el campo Enviado era un booleano y se pasó a fecha/hora de envío
		}
		this._Enviado = _Enviado;
		
	}

	public kLegalizacion.enumFormato getFormato() {
		return _Formato;
	}

	public void setFormato(kLegalizacion.enumFormato _Formato) {
		this._Formato = _Formato;
	}

	public String get_NombreZip() {
		return _NombreZip;
	}

	public void set_NombreZip(String _NombreZip) {
		this._NombreZip = _NombreZip;
	}

	public String get_IdentificadorEntrada() {
		return _IdentificadorEntrada;
	}

	public void set_IdentificadorEntrada(String _IdentificadorEntrada) {
		this._IdentificadorEntrada = _IdentificadorEntrada;
	}

	public String get_TipoPersona() {
		return _TipoPersona;
	}

	public void set_TipoPersona(String _TipoPersona) {
		this._TipoPersona = _TipoPersona;
	}

	public String get_VersionLegalia2Generacion() {
		return _VersionLegalia2Generacion;
	}

	public void set_VersionLegalia2Generacion(String _VersionLegalia2Generacion) {
		this._VersionLegalia2Generacion = _VersionLegalia2Generacion;
	}

	public String get_eDocNumeroDocumento() {
		return _eDocNumeroDocumento;
	}

	public void set_eDocNumeroDocumento(String _eDocNumeroDocumento) {
		this._eDocNumeroDocumento = _eDocNumeroDocumento;
	}

	public String get_eDocIdTramite() {
		return _eDocIdTramite;
	}

	public void set_eDocIdTramite(String _eDocIdTramite) {
		this._eDocIdTramite = _eDocIdTramite;
	}

	public String get_eDocNombreFicheroEnviado() {
		return _eDocNombreFicheroEnviado;
	}

	public void set_eDocNombreFicheroEnviado(String _eDocNombreFicheroEnviado) {
		this._eDocNombreFicheroEnviado = _eDocNombreFicheroEnviado;
	}

	public String get_eDocNombreFicheroAcuseEntrada() {
		return _PathDatos+"\\"+_eDocNombreFicheroAcuseEntrada;
	}

	public void set_eDocNombreFicheroAcuseEntrada(String _eDocNombreFicheroAcuseEntrada) {
		this._eDocNombreFicheroAcuseEntrada = _eDocNombreFicheroAcuseEntrada;
	}

	public String get_eDocNombreFicheroNE() {
		return _eDocNombreFicheroNE;
	}

	public void set_eDocNombreFicheroNE(String _eDocNombreFicheroNE) {
		this._eDocNombreFicheroNE = _eDocNombreFicheroNE;
	}

	public String get_eDocEntradaTipo() {
		return _eDocEntradaTipo;
	}

	public void set_eDocEntradaTipo(String _eDocEntradaTipo) {
		this._eDocEntradaTipo = _eDocEntradaTipo;
	}

	public String get_eDocEntradaSubsanada() {
		return _eDocEntradaSubsanada;
	}

	public void set_eDocEntradaSubsanada(String _eDocEntradaSubsanada) {
		this._eDocEntradaSubsanada = _eDocEntradaSubsanada;
	}

	public String get_EnvioReintentable() {
		return _EnvioReintentable;
	}

	public void set_EnvioReintentable(String _EnvioReintentable) {
		
		this._EnvioReintentable = _EnvioReintentable;
	}

	public String get_CodAccesoNif() {
		return _CodAccesoNif;
	}

	public void set_CodAccesoNif(String _CodAccesoNif) {
		this._CodAccesoNif = _CodAccesoNif;
	}

	public String get_PresentanteNombreConfirmado() {
		return _PresentanteNombreConfirmado;
	}

	public void set_PresentanteNombreConfirmado(String _PresentanteNombreConfirmado) {
		this._PresentanteNombreConfirmado = _PresentanteNombreConfirmado;
	}

	public String get_PresentanteApellidosConfirmados() {
		return _PresentanteApellidosConfirmados;
	}

	public void set_PresentanteApellidosConfirmados(String _PresentanteApellidosConfirmados) {
		this._PresentanteApellidosConfirmados = _PresentanteApellidosConfirmados;
	}

	public String get_PresentanteCorreoElectronicoConfirmado() {
		return _PresentanteCorreoElectronicoConfirmado;
	}

	public void set_PresentanteCorreoElectronicoConfirmado(String _PresentanteCorreoElectronicoConfirmado) {
		this._PresentanteCorreoElectronicoConfirmado = _PresentanteCorreoElectronicoConfirmado;
	}

	@Override
	public String toString() {
		return "CargaFicheroDesc [_PathDatos=" + _PathDatos + ", _Descripcion=" + _Descripcion + ", _Ejercicio="
				+ _Ejercicio + ", _Enviado=" + _Enviado + ", _NombreZip=" + _NombreZip + ", _IdentificadorEntrada="
				+ _IdentificadorEntrada + ", _TipoPersona=" + _TipoPersona + ", _VersionLegalia2Generacion="
				+ _VersionLegalia2Generacion + ", _eDocNumeroDocumento=" + _eDocNumeroDocumento + ", _eDocIdTramite="
				+ _eDocIdTramite + ", _eDocNombreFicheroEnviado=" + _eDocNombreFicheroEnviado
				+ ", _eDocNombreFicheroAcuseEntrada=" + _eDocNombreFicheroAcuseEntrada + ", _eDocNombreFicheroNE="
				+ _eDocNombreFicheroNE + ", _eDocEntradaTipo=" + _eDocEntradaTipo + ", _eDocEntradaSubsanada="
				+ _eDocEntradaSubsanada + ", _EnvioReintentable=" + _EnvioReintentable + ", _CodAccesoNif="
				+ _CodAccesoNif + ", _PresentanteNombreConfirmado=" + _PresentanteNombreConfirmado
				+ ", _PresentanteApellidosConfirmados=" + _PresentanteApellidosConfirmados
				+ ", _PresentanteCorreoElectronicoConfirmado=" + _PresentanteCorreoElectronicoConfirmado + "]";
	}

	public String getNombreDirectorio() {
		if (_PathDatos.isEmpty()) {
			return "";
		}
		return _PathDatos.substring(_PathDatos.lastIndexOf("\\") + 1);
	}

	public eEstadoEnvio getEstadoEnvio() {
		
		if (Formato.ValorNulo(_Enviado)) {
			
			switch (_EnvioReintentable) {
			case "True":
			
				return eEstadoEnvio.EnviadaPorServicioErrorReintentable;
			case "False":
				
				return eEstadoEnvio.EnviadaPorServicioErrorNoReintentable;
			default:
			
				return eEstadoEnvio.SinEnviar;
			}
		}
		
		if (_eDocIdTramite != null && !_eDocIdTramite.isEmpty()) {
			
			return eEstadoEnvio.EnviadaPorServicioCorrectamente;
		}
		
		return eEstadoEnvio.EnviadaPorPortal;
	}

}
