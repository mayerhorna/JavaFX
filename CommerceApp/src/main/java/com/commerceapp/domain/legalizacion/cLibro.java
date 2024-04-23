package com.commerceapp.domain.legalizacion;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.legalizacion.kLegalizacion.EnumValidacionesPrimariasLibro;

public class cLibro {
	private kLegalizacion.enumTipoLibro _TipoLibro;
	private String _FechaCierreUltimoLegalizado;
	private int _NumeroFicheros;
	public cFicheroLibro[] Ficheros;
	public EnumValidacionesPrimariasLibro[] ValidacionesPrimariasNoCumple;

	// Constructor
	public cLibro() {
		Inicializa();
	}

	// Inicialización de las variables
	private void Inicializa() {
		_TipoLibro = null; // Ajusta el valor según tu lógica de inicialización
		_FechaCierreUltimoLegalizado = "";
		_NumeroFicheros = 0;
		Ficheros = null;
		ValidacionesPrimariasNoCumple = null;
	}

	// Tipo de libro
	public kLegalizacion.enumTipoLibro getTipoLibro() {
		return _TipoLibro;
	}

	public void setTipoLibro(kLegalizacion.enumTipoLibro value) {
		_TipoLibro = value;
	}

	// Fecha de cierre del último legalizado
	public String getFechaCierreUltimoLegalizado() {
		return _FechaCierreUltimoLegalizado;
	}

	public void setFechaCierreUltimoLegalizado(String value) {
		_FechaCierreUltimoLegalizado = value;
	}

	// Número total de ficheros que tiene el libro
	public int getNumeroFicheros() {
		return _NumeroFicheros;
	}

	public void setNumeroFicheros(int value) {
		_NumeroFicheros = value;
	}

	// Indica si el libro tiene alguna legalización primaria que no cumple
	public boolean validaPrimarias() {
		return ValidacionesPrimariasNoCumple == null;
	}

	// Devuelve si para el libro hay que comprobar o no el solapamiento de fechas
	// de apertura y cierre de sus ficheros
	public boolean comprobarSolapamientoFechasAperturaYCierre() {
		int x = kLegalizacion.dameIndiceDeTipoLibro(_TipoLibro);
		return kLegalizacion.Tiposlibros.get(x).isComprobarSolapamientoFechasAperturaYCierre();
	}

	// Devuelve si para el libro hay que comprobar o no la fecha de apertura
	public boolean comprobarFechaApertura() {
		int x = kLegalizacion.dameIndiceDeTipoLibro(_TipoLibro);
		return kLegalizacion.Tiposlibros.get(x).isComprobarFechaApertura();
	}

	// Añade un nuevo fichero (tipo cFicheroLibro) al libro
	public void aniadeFichero(cFicheroLibro QueFichero) {
		_NumeroFicheros++;
		if (Ficheros == null) {			
			Ficheros = new cFicheroLibro[1];
		} else {
			cFicheroLibro[] temp = new cFicheroLibro[_NumeroFicheros];
			System.arraycopy(Ficheros, 0, temp, 0, _NumeroFicheros - 1);
			Ficheros = temp;
		}
		Ficheros[_NumeroFicheros - 1] = new cFicheroLibro();
		Ficheros[_NumeroFicheros - 1] = QueFichero;
	}

	// Quita del libro el fichero de índice indicefichero
	// Retorna true si se realiza la operación correctamente y false en caso
	// contrario
	public boolean quitaFichero(int indicefichero) {
		//aqui
		boolean result = false;
		if (indicefichero >= 0 && indicefichero <= _NumeroFicheros - 1) {
			if (com.commerceapp.util.Ficheros
					.FicheroBorra(Ficheros[indicefichero].getPathFichero()) == false) {
				throw new RuntimeException(IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.ErrorAlBorrarFichero,
						Ficheros[indicefichero].getPathFichero(), null, null));
			}
			
//Deprecado
			// Si es el primer fichero, se pasa su número al siguiente
		/*	if (indicefichero == 0 && _NumeroFicheros > 1) {
				Ficheros[1].setNumero(Ficheros[0].getNumero());
			}

			for (int j = indicefichero; j <= _NumeroFicheros - 2; j++) {
				Ficheros[j] = Ficheros[j + 1];
			}

			Ficheros[_NumeroFicheros - 1] = null;*/	// Último fichero
	//testeo
			cFicheroLibro[] ficherosNuevo = new cFicheroLibro[Ficheros.length - 1];

	            // Copiar los elementos antes del elemento a eliminar
	            System.arraycopy(Ficheros, 0, ficherosNuevo, 0, indicefichero);

	            // Copiar los elementos después del elemento a eliminar
	            System.arraycopy(Ficheros, indicefichero + 1, ficherosNuevo, indicefichero, Ficheros.length - indicefichero - 1);
			
			Ficheros=ficherosNuevo;
			
			_NumeroFicheros--;
			result = true;
		}
		return result;
	}

	// Obtiene el tipo de libro según el nombre de fichero que recibe
	public static kLegalizacion.enumTipoLibro obtenerTipoLibroSegunNombreFichero(String NombreFichero) {
		int posguionbajo = NombreFichero.lastIndexOf("_");
		if (posguionbajo != -1) {
			String prefijonombre = NombreFichero.substring(0, posguionbajo);
			for (int I = 0; I < kLegalizacion.Tiposlibros.size(); I++) {
				if (kLegalizacion.Tiposlibros.get(I).getNombreFichero().equals(prefijonombre)) {
					return kLegalizacion.Tiposlibros.get(I).getCodigo();
				}
			}
		}
		return null;
	}

	public cFicheroLibro[] getFicheros() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValidacionesPrimariasNoCumple() {
		// TODO Auto-generated method stub
		return null;
	}
}
