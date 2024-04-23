package com.commerceapp.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.service.LegalizacionService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Legalizaciones {
	private static final Logger logger = Logger.getLogger(Legalizaciones.class.getName());

	public boolean cargaDirectorio(String pathDirectorio, ObservableList<cDatos> arrayDatos, int queEjercicio,
			boolean incluirNoEnviados, boolean incluirEnviadosDirecto, boolean incluirEnviadosPortal) {
		
		
		List<cDatos> datosList = new ArrayList<>();		

		int contDirectorios = 0;

		try {
			
			File directorio = new File(pathDirectorio);
			
			
			
			if (!directorio.exists() || !directorio.isDirectory()) {
				
				return false;
			}

			File[] directorios = directorio.listFiles(File::isDirectory);

			for (File subDirectorio : directorios) {
				
				
				
				LegalizacionService miLegalizacion = new LegalizacionService(false, null);

				if (miLegalizacion.cargaDatosLegalias(subDirectorio.getAbsolutePath())) {
					
					boolean incluir = true;

					if (queEjercicio > 0 && miLegalizacion.Datos.get_Ejercicio() != queEjercicio) {
						
						incluir = false;
					}

					if (!incluirNoEnviados) {
						
						switch (miLegalizacion.Datos.getEstadoEnvio()) {						
						case SinEnviar,EnviadaPorServicioErrorNoReintentable,EnviadaPorServicioErrorReintentable:
							incluir = false;
							break;
						default:
							break;
						}
					}

					if (!incluirEnviadosDirecto && miLegalizacion.Datos
							.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioCorrectamente) {
						
						incluir = false;
					}

					if (!incluirEnviadosPortal
							&& miLegalizacion.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorPortal) {
						
						incluir = false;
					}
					
					if (incluir) {
						
						contDirectorios++;
						datosList.add(miLegalizacion.Datos);
					}
				}
			}
			
			arrayDatos.clear();
			arrayDatos.addAll(datosList);
			
			
			
			return contDirectorios > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
}