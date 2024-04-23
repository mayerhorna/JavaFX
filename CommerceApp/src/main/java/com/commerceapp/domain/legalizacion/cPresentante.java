package com.commerceapp.domain.legalizacion;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.service.LegalizacionService;

public class cPresentante {
	private String _Nombre;
	private String _Apellido1;
	private String _Apellido2;
	private String _Nif;
	private String _Domicilio;
	private String _Ciudad;
	private String _CodigoPostal;
	private String _ProvinciaCodigo;
	private String _Fax;
	private String _Telefono;
	private String _Email;
	private String _SolicitaRetencion;
	
	public cPresentante() {
		inicializa();
	}
	
	 public String get_Nombre() {
		return _Nombre;
	}

	public void set_Nombre(String _Nombre) {
		this._Nombre = _Nombre;
	}

	public String get_Apellido1() {
		return _Apellido1;
	}

	public void set_Apellido1(String _Apellido1) {
		this._Apellido1 = _Apellido1;
	}

	public String get_Apellido2() {
		return _Apellido2;
	}

	public void set_Apellido2(String _Apellido2) {
		this._Apellido2 = _Apellido2;
	}

	public String get_Nif() {
		return _Nif;
	}

	public void set_Nif(String _Nif) {
		this._Nif = _Nif;
	}

	public String get_Domicilio() {
		return _Domicilio;
	}

	public void set_Domicilio(String _Domicilio) {
		this._Domicilio = _Domicilio;
	}

	public String get_Ciudad() {
		return _Ciudad;
	}

	public void set_Ciudad(String _Ciudad) {
		this._Ciudad = _Ciudad;
	}

	public String get_CodigoPostal() {
		return _CodigoPostal;
	}

	public void set_CodigoPostal(String _CodigoPostal) {
		this._CodigoPostal = _CodigoPostal;
	}

	public String get_ProvinciaCodigo() {
		return _ProvinciaCodigo;
	}

	public void set_ProvinciaCodigo(String _ProvinciaCodigo) {
		this._ProvinciaCodigo = _ProvinciaCodigo;
	}

	public String get_Fax() {
		return _Fax;
	}

	public void set_Fax(String _Fax) {
		this._Fax = _Fax;
	}

	public String get_Telefono() {
		return _Telefono;
	}

	public void set_Telefono(String _Telefono) {
		this._Telefono = _Telefono;
	}

	public String get_Email() {
		return _Email;
	}

	public void set_Email(String _Email) {
		this._Email = _Email;
	}

	public String get_SolicitaRetencion() {
		return _SolicitaRetencion;
	}

	public void set_SolicitaRetencion(String _SolicitaRetencion) {
		this._SolicitaRetencion = _SolicitaRetencion;
	}

	public void inicializa() {
	        this._Nombre = "";
	        this._Apellido1 = "";
	        this._Apellido2 = "";
	        this._Nif = "";
	        this._Domicilio = "";
	        this._Ciudad = "";
	        this._CodigoPostal = "";
	        this._ProvinciaCodigo ="";
	        this._Fax = "";
	        this._Telefono ="" ;
	        this._Email ="";
	        this._SolicitaRetencion = "";
	    }
	 
	 public String getInformacion() {
		    StringBuilder cad = new StringBuilder();

		    cad.append("Nombre presentante: ").append(_Nombre).append("\n");

		    cad.append("Apellido 1: ").append(_Apellido1).append("\n");
		    cad.append("Apellido 2: ").append(_Apellido2).append("\n");
		    cad.append("Nif: ").append(_Nif).append("\n");

		    cad.append("Domicilio: ").append(_Domicilio).append("\n");

		    cad.append("Ciudad: ").append(_Ciudad).append("\n");
		    cad.append("Código postal: ").append(_CodigoPostal).append("\n");
		    cad.append("Código de provincia: ").append(_ProvinciaCodigo).append("\n");

		    cad.append("Fax: ").append(_Fax).append("\n");
		    cad.append("Telefono: ").append(_Telefono).append("\n");
		    cad.append("Email: ").append(_Email).append("\n");

		    cad.append("Solicita retención: ").append(_SolicitaRetencion).append("\n");

		    return cad.toString();
		}
	 
	 public String bloquePresentacion() {
		    String linea;
		    String resul = "";

		    try {
		        if (_Nombre != null ) {
		            linea = kLegalizacion.kPresentanteNombreCodigoCampo + _Nombre;
		            resul += linea + "\n";
		        }

		        if (_Apellido1 != null ) {
		            linea = kLegalizacion.kPresentanteApellido1CodigoCampo + _Apellido1;
		            resul += linea + "\n";
		        }

		        if (_Apellido2 != null ) {
		            linea = kLegalizacion.kPresentanteApellido2CodigoCampo + _Apellido2;
		            resul += linea + "\n";
		        }

		        if (_Nif != null ) {
		            linea = kLegalizacion.kPresentanteNifCodigoCampo + _Nif;
		            resul += linea + "\n";
		        }

		        if (_Domicilio != null ) {
		            linea = kLegalizacion.kPresentanteDomicilioCodigoCampo + _Domicilio;
		            resul += linea + "\n";
		        }

		        if (_Ciudad != null ) {
		            linea = kLegalizacion.kPresentanteCiudadCodigoCampo + _Ciudad;
		            resul += linea + "\n";
		        }

		        if (_CodigoPostal != null ) {
		            linea = kLegalizacion.kPresentanteCodigoPostalCodigoCampo + _CodigoPostal;
		            resul += linea + "\n";
		        }

		        if (_ProvinciaCodigo != null ) {
		            linea = kLegalizacion.kPresentanteProvinciaCodigoCodigoCampo + _ProvinciaCodigo;
		            resul += linea + "\n";
		        }

		        if (_Fax != null ) {
		            linea = kLegalizacion.kPresentanteFaxCodigoCampo + _Fax;
		            resul += linea + "\n";
		        }

		        if (_Telefono != null ) {
		            linea = kLegalizacion.kPresentanteTelefonoCodigoCampo + _Telefono;
		            resul += linea + "\n";
		        }

		        if (_Email != null ) {
		            linea = kLegalizacion.kPresentanteEmailCodigoCampo + _Email;
		            resul += linea + "\n";
		        }

		        if (_SolicitaRetencion != null) {
		            linea = kLegalizacion.kPresentanteSolicitaRetencionCodigoCampo + _SolicitaRetencion;
		            resul += linea + "\n";
		        }

		        return resul;
		    } catch (Exception ex) {
		        return "";
		    }
		}
	public void ponDatosPresentantePorDefecto() {
		    try {
		        if ("LEGALIA2".equals(LegalizacionService.K_COMPILACION)) {
		            _Nombre = MGeneral.Configuracion.getValorDefectoPresentanteNombre();
		            _Apellido1 = MGeneral.Configuracion.getValorDefectoPresentanteApellido1();
		            _Apellido2 = MGeneral.Configuracion.getValorDefectoPresentanteApellido2();
		            _Nif = MGeneral.Configuracion.getValorDefectoPresentanteNif();
		            _Domicilio = MGeneral.Configuracion.getValorDefectoPresentanteDomicilio();
		            _Ciudad = MGeneral.Configuracion.getValorDefectoPresentanteCiudad();
		            _CodigoPostal = MGeneral.Configuracion.getValorDefectoPresentanteCodigoPostal();
		            _ProvinciaCodigo = MGeneral.Configuracion.getValorDefectoPresentanteCodigoProvincia();
		            
		            // Verificar si existe el municipio de la provincia
		            if (!MGeneral.Idioma.existeMunicipioDeProvincia(_ProvinciaCodigo, _Ciudad)) {
		                _Ciudad = "";
		            }
		            
		            _Fax = MGeneral.Configuracion.getValorDefectoPresentanteFax();
		            _Telefono = MGeneral.Configuracion.getValorDefectoPresentanteTelefono();
		            _Email = MGeneral.Configuracion.getValorDefectoPresentanteEmail();
		            _SolicitaRetencion = MGeneral.Configuracion.getValorDefectoPresentanteSolicitaRetencion();
		        }
		    } catch (Exception ex) {
		        // Manejar la excepción según tus necesidades
		    }
		}
}
