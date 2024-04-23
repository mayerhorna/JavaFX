package com.commerceapp.domain.legalizacion;

import java.util.Objects;

import com.commerceapp.util.Formato;

public class cPresentacion {

	private String _RegistroMercantilDestinoDescripcion;//cboxRegistroMercantil
	private String _RegistroMercantilDestinoCodigo;

	// Identificacion del empresario o entidad (todo lo que termina en soli)
	private String _FechaSolicitud;
	private String _NombreSociedadoEmpresario;
	private String _Apellido1;
	private String _Apellido2;
	private String _NifCif;
	private String _Domicilio;
	private String _Ciudad;//cboxMunicipio
	private String _CodigoPostal;
	private String _ProvinciaCodigo;
	private String _Fax;
	private String _Telefono;

	// datos registrales
	private String _DatosRegistralesTomo;
	private String _DatosRegistralesFolio;
	private String _DatosRegistralesHoja;
	private String _DatosRegistralesOtros;
	private String _TipoRegistroPublico;

	private String _DatosRegistralesLibro;
	private String _DatosRegistralesSeccion;
	//Presentante
	public cPresentante _Presentante = new cPresentante();

	public cPresentacion() {
		Inicializa();
	}
	
	public void Inicializa() {
		this._RegistroMercantilDestinoDescripcion = "";
		this._RegistroMercantilDestinoCodigo = "";

		// Identificacion del empresario o sociedad
		this._FechaSolicitud = "";
		this._NombreSociedadoEmpresario = "";
		this._Apellido1 = "";
		this._Apellido2 = "";
		this._NifCif = "";
		this._Domicilio = "";
		this._Ciudad = "";
		this._CodigoPostal = "";
		this._ProvinciaCodigo = "";
		this._Fax = "";
		this._Telefono = "";

		// Datos Registrales
		this._DatosRegistralesTomo = "";
		this._DatosRegistralesFolio = "";
		this._DatosRegistralesHoja = "";
		this._DatosRegistralesOtros = "";
		this._TipoRegistroPublico = "";

		this._DatosRegistralesLibro = "";
		this._DatosRegistralesSeccion = "";

	}
	//Getters y Setters
	
	public String getRegistroMercantilDestinoDescripcion() {
        return _RegistroMercantilDestinoDescripcion;
    }

    public void setRegistroMercantilDestinoDescripcion(String value) {
        this._RegistroMercantilDestinoDescripcion = value;
    }

    /**
     * Código del registro mercantil de destino
     * 
     * @return Código del registro mercantil de destino
     */
    public String getRegistroMercantilDestinoCodigo() {
        return _RegistroMercantilDestinoCodigo;
    }

    public void setRegistroMercantilDestinoCodigo(String value) {
        this._RegistroMercantilDestinoCodigo = value;
    }

    /**
     * Fecha de la solicitud, en formato DDMMAAAA
     * 
     * @return Fecha de la solicitud
     */
    public String getFechaSolicitud() {
        return _FechaSolicitud;
    }

    public void setFechaSolicitud(String value) {
        this._FechaSolicitud = value;
    }

    /**
     * Nombre de la sociedad o empresario
     * 
     * @return Nombre de la sociedad o empresario
     */
    public String getNombreSociedadoEmpresario() {
        return _NombreSociedadoEmpresario;
    }

    public void setNombreSociedadoEmpresario(String value) {
        this._NombreSociedadoEmpresario = value;
    }

    /**
     * Apellido 1 del empresario
     * 
     * @return Apellido 1 del empresario
     */
    public String getApellido1() {
        return _Apellido1;
    }

    public void setApellido1(String value) {
        this._Apellido1 = value;
    }

    /**
     * Apellido 2 del empresario
     * 
     * @return Apellido 2 del empresario
     */
    public String getApellido2() {
        return _Apellido2;
    }

    public void setApellido2(String value) {
        this._Apellido2 = value;
    }

    /**
     * Nif/Cif de la sociedad
     * 
     * @return Nif/Cif de la sociedad
     */
    public String getNifCif() {
        return _NifCif;
    }

    public void setNifCif(String value) {
        this._NifCif = value;
    }

    /**
     * Domicilio de la sociedad
     * 
     * @return Domicilio de la sociedad
     */
    public String getDomicilio() {
        return _Domicilio;
    }

    public void setDomicilio(String value) {
        this._Domicilio = value;
    }

    /**
     * Ciudad de la sociedad
     * 
     * @return Ciudad de la sociedad
     */
    public String getCiudad() {
        return _Ciudad;
    }

    public void setCiudad(String value) {
        this._Ciudad = value;
    }

    /**
     * Código postal de la sociedad
     * 
     * @return Código postal de la sociedad
     */
    public String getCodigoPostal() {
        return _CodigoPostal;
    }

    public void setCodigoPostal(String value) {
        this._CodigoPostal = value;
    }

    /**
     * Código de la provincia de la sociedad
     * 
     * @return Código de la provincia de la sociedad
     */
    public String getProvinciaCodigo() {
        return _ProvinciaCodigo;
    }

    public void setProvinciaCodigo(String value) {
        this._ProvinciaCodigo = value;
    }

    /**
     * Fax
     * 
     * @return Fax
     */
    public String getFax() {
        return _Fax;
    }

    public void setFax(String value) {
        this._Fax = value;
    }

    /**
     * Tfno
     * 
     * @return Tfno
     */
    public String getTelefono() {
        return _Telefono;
    }

    public void setTelefono(String value) {
        this._Telefono = value;
    }
    
    public String getDatosRegistralesTomo() {
        return _DatosRegistralesTomo;
    }

    public void setDatosRegistralesTomo(String datosRegistralesTomo) {
        this._DatosRegistralesTomo = datosRegistralesTomo;
    }

    public String getDatosRegistralesLibro() {
        return _DatosRegistralesLibro;
    }

    public void setDatosRegistralesLibro(String datosRegistralesLibro) {
        this._DatosRegistralesLibro = datosRegistralesLibro;
    }

    public String getDatosRegistralesSeccion() {
        return _DatosRegistralesSeccion;
    }

    public void setDatosRegistralesSeccion(String datosRegistralesSeccion) {
        this._DatosRegistralesSeccion = datosRegistralesSeccion;
    }

    public String getDatosRegistralesFolio() {
        return _DatosRegistralesFolio;
    }

    public void setDatosRegistralesFolio(String datosRegistralesFolio) {
        this._DatosRegistralesFolio = datosRegistralesFolio;
    }

    public String getDatosRegistralesHoja() {
        return _DatosRegistralesHoja;
    }

    public void setDatosRegistralesHoja(String datosRegistralesHoja) {
        this._DatosRegistralesHoja = datosRegistralesHoja;
    }

    public String getDatosRegistralesOtros() {
        return _DatosRegistralesOtros;
    }

    public void setDatosRegistralesOtros(String datosRegistralesOtros) {
        this._DatosRegistralesOtros = datosRegistralesOtros;
    }

    public String getTipoRegistroPublico() {
        return _TipoRegistroPublico;
    }

    public void setTipoRegistroPublico(String tipoRegistroPublico) {
        this._TipoRegistroPublico = tipoRegistroPublico;
    }

    public cPresentante getPresentante() {
        return _Presentante;
    }

    public void setPresentante(cPresentante presentante) {
        this._Presentante = presentante;
    }

    public String getInformacion() {
        String cad = "";

        cad += "Código Registro destino: " + _RegistroMercantilDestinoCodigo + "\n";
        cad += "Descripcion Registro destino: " + _RegistroMercantilDestinoDescripcion + "\n";

        cad += "Nombre sociedad o empresario: " + _NombreSociedadoEmpresario + "\n";

        cad += "Apellido 1: " + _Apellido1 + "\n";
        cad += "Apellido 2: " + _Apellido2 + "\n";
        cad += "Nif/Cif: " + _NifCif + "\n";

        cad += "Domicilio: " + _Domicilio + "\n";

        cad += "Ciudad: " + _Ciudad + "\n";
        cad += "Código postal: " + _CodigoPostal + "\n";
        cad += "Código de provincia: " + _ProvinciaCodigo + "\n";

        cad += "Fax: " + _Fax + "\n";
        cad += "Telefono: " + _Telefono + "\n";

        cad += "Fecha de la solicitud: " + _FechaSolicitud + "\n";

        cad += "Tomo: " + _DatosRegistralesTomo + "\n";
        cad += "Folio: " + _DatosRegistralesFolio + "\n";
        cad += "Hoja: " + _DatosRegistralesHoja + "\n";
        cad += "Otros: " + _DatosRegistralesOtros + "\n";

        cad += "Tipo registro público: " + _TipoRegistroPublico + "\n";

        cad += _Presentante.getInformacion();

        return cad;
    }
	
	public String bloquePresentacion() {
        String linea;
        StringBuilder resul = new StringBuilder();

        try {
            // BloquePresentacion = String.Empty; // No es necesario en Java

            if (!Formato.ValorNulo(_RegistroMercantilDestinoDescripcion)) {
                linea = kLegalizacion.kRegistroMercantilDestinoDescripcionCodigoCampo + _RegistroMercantilDestinoDescripcion;
                // En el fichero, las descripciones de los Registros van con / (se cambian para los combos por el problema de éstos con el carácter /)
                linea = linea.replace("-", "/");
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_FechaSolicitud)) {
                linea = kLegalizacion.kFechaSolicitudCodigoCampo + _FechaSolicitud;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_NombreSociedadoEmpresario)) {
                linea = kLegalizacion.kNombreSociedadoEmpresarioCodigoCampo + _NombreSociedadoEmpresario;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Apellido1)) {
                linea = kLegalizacion.kApellido1CodigoCampo + _Apellido1;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Apellido2)) {
                linea = kLegalizacion.kApellido2CodigoCampo + _Apellido2;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_NifCif)) {
                linea = kLegalizacion.kNifCifCodigoCampo + _NifCif;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Domicilio)) {
                linea = kLegalizacion.kDomicilioCodigoCampo + _Domicilio;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Ciudad)) {
                linea = kLegalizacion.kCiudadCodigoCampo + _Ciudad;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_CodigoPostal)) {
                linea = kLegalizacion.kCodigoPostalCodigoCampo + _CodigoPostal;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_ProvinciaCodigo)) {
                linea = kLegalizacion.kProvinciaCodigoCodigoCampo + _ProvinciaCodigo;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Fax)) {
                linea = kLegalizacion.kFaxCodigoCampo + _Fax;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_Telefono)) {
                linea = kLegalizacion.kTelefonoCodigoCampo + _Telefono;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_RegistroMercantilDestinoCodigo)) {
                linea = kLegalizacion.kRegistroMercantilDestinoCodigoCodigoCampo + _RegistroMercantilDestinoCodigo;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_DatosRegistralesTomo)) {
                linea = kLegalizacion.kDatosRegistralesTomoCodigoCampo + _DatosRegistralesTomo;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_DatosRegistralesLibro)) {
                linea = kLegalizacion.kDatosRegistralesLibroCodigoCampo + _DatosRegistralesLibro;
                resul.append(linea).append(System.lineSeparator());
            }


            if (!Formato.ValorNulo(_DatosRegistralesFolio)) {
                linea = kLegalizacion.kDatosRegistralesFolioCodigoCampo + _DatosRegistralesFolio;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_TipoRegistroPublico)) {
                linea = kLegalizacion.kTipoRegistroPublicoCodigoCampo + _TipoRegistroPublico;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_DatosRegistralesHoja)) {
                linea = kLegalizacion.kDatosRegistralesHojaCodigoCampo + _DatosRegistralesHoja;
                resul.append(linea).append(System.lineSeparator());
            }

            if (!Formato.ValorNulo(_DatosRegistralesOtros)) {
                linea = kLegalizacion.kDatosRegistralesOtrosCodigoCampo + _DatosRegistralesOtros;
                resul.append(linea).append(System.lineSeparator());
            }

            resul.append(Objects.requireNonNull(_Presentante).bloquePresentacion());

            return resul.toString();

        } catch (Exception ex) {
            return "";
        }
    }
	
	
}
