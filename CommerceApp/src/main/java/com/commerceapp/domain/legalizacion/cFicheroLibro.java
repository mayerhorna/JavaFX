package com.commerceapp.domain.legalizacion;

import java.io.File;

import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesPrimariasFichero;

public class cFicheroLibro {
	private String _Descripcion;
	private String _PathFichero;
	private String _NombreFichero;
	private String _ExtensionFichero;
	private kLegalizacion.enumExtensionFichero _TipoExtensionFichero;
	private int _Numero;
	private String _FechaApertura;
	private String _FechaCierre;
	private String _Huella;
	private String _HuellaCalculada;
	private String _HuellaManual;
	private String _HuellaImprimir1;
	private String _HuellaImprimir2;
	private boolean _VisualizacionCorrecta;
	private long _Bytes;
	private String _PrefijoNombre;
	private int _NumeroSegunNombre;
	private kLegalizacion.enumTipoLibro _TipoLibroSegunNombre;
	private boolean _EsEncriptado;
	public EnumValidacionesPrimariasFichero[] ValidacionesPrimariasNoCumple;

	// Constructor
	public cFicheroLibro() {
		inicializa();
	}

	// Inicialización de las variables
	private void inicializa() {
		_Descripcion = "";
		_PathFichero = "";
		_NombreFichero = "";
		_ExtensionFichero = "";
		_TipoExtensionFichero = null; // Ajusta el valor según tu lógica de inicialización
		_Numero = 0;
		_FechaApertura = "";
		_FechaCierre = "";
		_Huella = "";
		_HuellaCalculada = "";
		_HuellaManual = "";
		_HuellaImprimir1 = "";
		_HuellaImprimir2 = "";
		_VisualizacionCorrecta = true;
		_Bytes = 0;
		_PrefijoNombre = "";
		_NumeroSegunNombre = 0;
		_TipoLibroSegunNombre = null; // Ajusta el valor según tu lógica de inicialización
		_EsEncriptado = false;
		ValidacionesPrimariasNoCumple = null;
	}

	// Método para la propiedad VisualizacionCorrecta
	public boolean getVisualizacionCorrecta() {
		return _VisualizacionCorrecta;
	}

	public String getDescripcion() {
		return _Descripcion;
	}

	public void setDescripcion(String value) {
		_Descripcion = value;
	}

	// Propiedad PathFichero
	public String getPathFichero() {
		return _PathFichero;
	}

	public void setPathFichero(String value) {
		_PathFichero = value;
	}

	// Propiedad NombreFichero
	public String getNombreFichero() {
		return _NombreFichero;
	}

	public void setNombreFichero(String value) {
		_NombreFichero = value;
		obtenerDatosFicheroSegunNombreFichero();
	}

	// Propiedad Numero
	public int getNumero() {
		return _Numero;
	}

	public void setNumero(int value) {
		_Numero = value;
	}

	// Propiedad FechaApertura
	public String getFechaApertura() {
		return _FechaApertura;
	}

	public void setFechaApertura(String value) {
		_FechaApertura = value;
	}

	// Propiedad FechaCierre
	public String getFechaCierre() {
		return _FechaCierre;
	}

	public void setFechaCierre(String value) {
		_FechaCierre = value;
	}

	// Propiedad Huella
	public String getHuella() {
		return _Huella;
	}

	public void setHuella(String value) {
		_Huella = value;
	}

	// Propiedad HuellaCalculada
	public String getHuellaCalculada() {
		return _HuellaCalculada;
	}

	public void setHuellaCalculada(String value) {
		_HuellaCalculada = value;
	}

	// Propiedad HuellaManual
	public String getHuellaManual() {
		return _HuellaManual;
	}

	public void setHuellaManual(String value) {
		_HuellaManual = value;
	}

	// Propiedad HuellaImprimir1
	public String getHuellaImprimir1() {
		return _HuellaImprimir1;
	}

	public void setHuellaImprimir1(String value) {
		_HuellaImprimir1 = value;
	}

	// Propiedad HuellaImprimir2
	public String getHuellaImprimir2() {
		return _HuellaImprimir2;
	}

	public void setHuellaImprimir2(String value) {
		_HuellaImprimir2 = value;
	}

	// Resto de tu código...

	// Propiedad TipoExtensionFichero
	public kLegalizacion.enumExtensionFichero getTipoExtensionFichero() {
		return _TipoExtensionFichero;
	}

	// Propiedad ExtensionFichero
	public String getExtensionFichero() {
		return _ExtensionFichero;
	}

	// Propiedad Bytes
	public long getBytes() {

		if (new File(_PathFichero).exists()) {
			return new File(_PathFichero).length();
		} else {
			return 0;
		}
	}

	public void setBytes(long value) {
		_Bytes = value;
	}

	// Propiedad PrefijoNombre
	public String getPrefijoNombre() {
		return _PrefijoNombre;
	}

	// Propiedad NumeroSegunNombre
	public int getNumeroSegunNombre() {
		return _NumeroSegunNombre;
	}

	// Propiedad TipoLibroSegunNombre
	public kLegalizacion.enumTipoLibro getTipoLibroSegunNombre() {
		return _TipoLibroSegunNombre;
	}

	// Propiedad VisualizacionCorrecta
	public boolean isVisualizacionCorrecta() {
		return _VisualizacionCorrecta;
	}

	public void setVisualizacionCorrecta(boolean value) {
		_VisualizacionCorrecta = value;
	}

	// Propiedad EsEncriptado
	public boolean isEsEncriptado() {
		return _EsEncriptado;
	}

	public String getInformacion() {
		StringBuilder cad = new StringBuilder();

		cad.append("Path:").append(_PathFichero).append("\n");
		cad.append("Nombre fichero: ").append(_NombreFichero).append("\n");

		cad.append("Prefijo nombre: ").append(_PrefijoNombre).append("\n");
		cad.append("Tipo libro según nombre fichero: ").append(_TipoLibroSegunNombre.toString()).append("\n");

		cad.append("Extensión: ").append(_ExtensionFichero).append("\n");
		cad.append("Tipo extensión: ").append(_TipoExtensionFichero.toString()).append("\n");

		cad.append("Número: ").append(_Numero).append("\n");
		cad.append("Número según el nombre: ").append(_NumeroSegunNombre).append("\n");

		cad.append("Bytes: ").append(_Bytes).append("\n");

		cad.append("Huella: ").append(_Huella).append("\n");
		cad.append("Huella calculada: ").append(_HuellaCalculada).append("\n");
		cad.append("Huella manual: ").append(_HuellaManual).append("\n");
		cad.append("Visualización correcta: ").append(_VisualizacionCorrecta).append("\n");

		cad.append("Fecha apertura: ").append(_FechaApertura).append("\n");
		cad.append("Fecha cierre: ").append(_FechaCierre).append("\n");

		return cad.toString();
	}

	// Propiedad ValidaPrimarias
	public boolean isValidaPrimarias() {
		return ValidacionesPrimariasNoCumple == null;
	}

	private void obtenerDatosFicheroSegunNombreFichero() {
		String nombre;
		int posGuionBajo;
		String sufijoNombre;
		int posPrimerPunto;

		try {
			_ExtensionFichero = "";
			_TipoExtensionFichero = null;
			_PrefijoNombre = "";
			_NumeroSegunNombre = 0;
			_TipoLibroSegunNombre = null;
			_EsEncriptado = false;

			if (_NombreFichero.isEmpty())
				return;

			_ExtensionFichero = new File(_NombreFichero).getName();
			int posExtension = _ExtensionFichero.lastIndexOf(".");
			if (posExtension != -1) {
				_ExtensionFichero = _ExtensionFichero.substring(posExtension + 1);
			}
			_TipoExtensionFichero = kLegalizacion.dameCodigoDeExtensionFicheroSegunExtension(_ExtensionFichero);

			int indiceExtension = kLegalizacion.dameIndiceDeExtensionFichero(_TipoExtensionFichero);
			if (indiceExtension > -1) {
				_EsEncriptado = kLegalizacion.ExtensionesFicheros.get(indiceExtension).isEsDeEncriptacion();
			}

			nombre = new File(_NombreFichero).getName();

			posGuionBajo = nombre.lastIndexOf("_");

			if (posGuionBajo == -1)
				return;

			posPrimerPunto = nombre.indexOf(".", posGuionBajo);

			_PrefijoNombre = nombre.substring(0, posGuionBajo);

			sufijoNombre = nombre.substring(posGuionBajo + 1, posGuionBajo + posPrimerPunto - posGuionBajo);

			if (sufijoNombre.matches("\\d+")) {
				_NumeroSegunNombre = Integer.parseInt(sufijoNombre);
			}

			_TipoLibroSegunNombre = cLibro.obtenerTipoLibroSegunNombreFichero(_NombreFichero);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage(), ex.getCause());
		}
	}

	public Object getValidacionesPrimariasNoCumple() {
		// TODO Auto-generated method stub
		return ValidacionesPrimariasNoCumple;
	}

}
