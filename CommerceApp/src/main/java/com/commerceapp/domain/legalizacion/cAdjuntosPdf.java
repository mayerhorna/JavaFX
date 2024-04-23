package com.commerceapp.domain.legalizacion;

import java.io.File;

import java.io.IOException;

import java.nio.file.DirectoryStream;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.StandardCopyOption;

import java.util.Arrays;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.util.Ficheros;

public class cAdjuntosPdf {

	public class SFicheroPpf {

		public String nombreFichero;

		public String pathFichero;

	}

	private String pathDatosLegalizacion;

	private String pathAdjuntos;

	private SFicheroPpf[] ficheros;

	private int numeroFicheros;

	public cAdjuntosPdf(String pPathDatosLegalizacion) {

		pathDatosLegalizacion = pPathDatosLegalizacion;

		if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

			pathAdjuntos = pathDatosLegalizacion + "\\" + kLegalizacion.kDirectorioAdjuntos;

		} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

			pathAdjuntos = pathDatosLegalizacion + "/" + kLegalizacion.kDirectorioAdjuntos;

		}

		inicializa();

	}

	private void inicializa() {

		ficheros = null;

		numeroFicheros = 0;

	}

	public SFicheroPpf[] getFicheros() {

		return ficheros;

	}

	public void setFicheros(SFicheroPpf[] value) {

		ficheros = value;

	}

	public int getNumeroFicheros() {

		return numeroFicheros;

	}

	public void setNumeroFicheros(int value) {

		numeroFicheros = value;

	}

	public boolean aniadeFichero(String pathFicheroOrigen, boolean copiaConProgreso) {

		try {

			String nombreFichero;

			int indiceFichero;

			String extensionFichero;

			String nombreFicheroOrigen;

			if (!Files.exists(Path.of(pathFicheroOrigen))) {

				throw new IOException("El fichero " + pathFicheroOrigen + " no existe.");

			}

			extensionFichero = new File(pathFicheroOrigen).getName();

			int dotIndex = extensionFichero.lastIndexOf('.');

			if (dotIndex > 0) {

				extensionFichero = extensionFichero.substring(dotIndex + 1);

			}

			if (!extensionFichero.equalsIgnoreCase("pdf")) {

				throw new IOException("La extensión del fichero no es admitida.");

			}

			nombreFicheroOrigen = new File(pathFicheroOrigen).getName();

			// TODO PATH URI: no será necesario cuando el portal admita URI

			nombreFichero = nombreFicheroOrigen.replace(" ", "_"); // Los espacios en blanco no están permitidos para

			// envío por el portal

			indiceFichero = dameIndiceFicheroPorNombre(nombreFichero);

			if (indiceFichero >= 0) {

				throw new IOException("El fichero ya existe.");

			}

			String pathFicheroDestino = "";

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				pathFicheroDestino = pathAdjuntos + "\\" + nombreFichero;

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				pathFicheroDestino = pathAdjuntos + "/" + nombreFichero;

			}

			if (!Files.exists(Path.of(pathAdjuntos))) {

				Files.createDirectories(Path.of(pathAdjuntos)); // Crear directorio destino si no existe

			}

			if (Files.exists(Path.of(pathFicheroDestino))) {

				throw new IOException("El fichero destino ya existe.");

			}

			if (pathFicheroOrigen.equals(pathFicheroDestino)) {

				throw new IOException("La ruta de origen y destino son iguales.");

			}

			if (copiaConProgreso) {

				Files.copy(Path.of(pathFicheroOrigen), Path.of(pathFicheroDestino),

						StandardCopyOption.REPLACE_EXISTING);

			} else {

				Files.copy(Path.of(pathFicheroOrigen), Path.of(pathFicheroDestino));

			}

			numeroFicheros++;

			SFicheroPpf nuevoFichero = new SFicheroPpf();

			nuevoFichero.pathFichero = pathFicheroDestino;

			nuevoFichero.nombreFichero = nombreFichero;

			ficheros = Arrays.copyOf(ficheros, numeroFicheros);

			ficheros[numeroFicheros - 1] = nuevoFichero;

			return true;

		} catch (IOException ex) {

			ex.printStackTrace();

			return false;

		}

	}

	public boolean quitaFichero(String nombreFichero) {

		try {

			int indiceFichero;

			int j;

			indiceFichero = dameIndiceFicheroPorNombre(nombreFichero);

			if (indiceFichero < 0) {

				return false;

			}

			if (!Ficheros.FicheroBorra(ficheros[indiceFichero].pathFichero)) {

				throw new IOException("Error al borrar el fichero " + ficheros[indiceFichero].pathFichero);

			}

			for (j = indiceFichero; j < numeroFicheros - 1; j++) {

				ficheros[j] = ficheros[j + 1];

			}

			ficheros[numeroFicheros - 1] = null; // Último fichero

			numeroFicheros--;

			if (numeroFicheros == 0) {

				try {

					new File(pathAdjuntos).delete();

				} catch (Exception ex) {

				}

			}

			return true;

		} catch (IOException ex) {

			ex.printStackTrace();

			return false;

		}

	}

	private int dameIndiceFicheroPorNombre(String nombreFichero) {

		int indiceFichero = -1;

		for (int i = 0; i < numeroFicheros; i++) {

			if (ficheros[i].nombreFichero.equals(nombreFichero)) {

				return i;

			}

		}

		return indiceFichero;

	}

	public void cargaFicheros() {

		try {

			numeroFicheros = 0;

			ficheros = new SFicheroPpf[0];

			if (!Files.exists(Path.of(pathAdjuntos))) {

				return;

			}

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(pathAdjuntos), "*.pdf")) {

				for (Path path : stream) {

					numeroFicheros++;

					SFicheroPpf nuevoFichero = new SFicheroPpf();

					nuevoFichero.pathFichero = path.toString();

					nuevoFichero.nombreFichero = path.getFileName().toString();

					ficheros = Arrays.copyOf(ficheros, numeroFicheros);

					ficheros[numeroFicheros - 1] = nuevoFichero;

				}

			}

		} catch (IOException ex) {

			ex.printStackTrace();

		}

	}

	public boolean valida() {

		try {

			for (int i = 0; i < numeroFicheros; i++) {

				if (!Files.exists(Path.of(ficheros[i].pathFichero))) {

					throw new IOException("El fichero " + ficheros[i].pathFichero + " no existe.");

				}

			}

			if (numeroFicheros > MGeneral.Configuracion.getAdjuntosMaximos()) {

				throw new IOException(

						"Se ha superado el número máximo de adjuntos: " + MGeneral.Configuracion.getAdjuntosMaximos());

			}

			return true;

		} catch (IOException ex) {

			ex.printStackTrace();

			return false;

		}

	}

	public String cadenaEnvio() {

		String cadena = "";

		try {

			for (int i = 0; i < numeroFicheros; i++) {

				if (!Files.exists(Path.of(ficheros[i].pathFichero))) {

					throw new IOException("El fichero " + ficheros[i].pathFichero + " no existe.");

				}

				cadena += "&ficheroPdf=" + Ficheros.CodificaPathLocal(ficheros[i].pathFichero);

			}

			if (numeroFicheros > MGeneral.Configuracion.getAdjuntosMaximos()) {

				throw new IOException(

						"Se ha superado el número máximo de adjuntos: " + MGeneral.Configuracion.getAdjuntosMaximos());

			}

			return cadena;

		} catch (IOException ex) {

			ex.printStackTrace();

			return "";

		}

	}

}
