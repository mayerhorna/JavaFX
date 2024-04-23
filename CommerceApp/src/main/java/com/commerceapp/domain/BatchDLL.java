package com.commerceapp.domain;

import java.io.FileWriter;
import java.io.IOException;

import com.commerceapp.service.LegalizacionService;

public class BatchDLL {
	public enum EnumOpcionBatchParam {
		C, V, H, Z, E
	}

	public class SBatchParameter {
		EnumOpcionBatchParam opcion;
		String ruta;
		String log;
		String ejercicio;
		boolean raiz;
	}

	public void manejaCommandLineArgs(String[] args) {
		SBatchParameter param = new SBatchParameter();
		try {
			for (String s : args) {
				if (s.toLowerCase().startsWith("opcion=")) {
					String option = s.toLowerCase().substring("opcion=".length());
					switch (option) {
					case "c":
						param.opcion = EnumOpcionBatchParam.C;
						break;
					case "e":
						param.opcion = EnumOpcionBatchParam.E;
						break;
					case "h":
						param.opcion = EnumOpcionBatchParam.H;
						break;
					case "v":
						param.opcion = EnumOpcionBatchParam.V;
						break;
					case "z":
						param.opcion = EnumOpcionBatchParam.Z;
						break;
					}
				} else if (s.toLowerCase().startsWith("ruta=")) {
					param.ruta = s.substring("ruta=".length());
				} else if (s.toLowerCase().startsWith("log=")) {
					param.log = s.substring("log=".length());
				} else if (s.toLowerCase().startsWith("ejercicio=")) {
					param.ejercicio = s.substring("ejercicio=".length());
				} else if (s.toLowerCase().startsWith("raiz=")) {
					param.raiz = Boolean.parseBoolean(s.substring("raiz=".length()));
				} else {
					System.out.println("Parámetro no Reconocido");
				}
			}

			if (param.opcion == null || param.ruta == null || param.log == null) {
				System.out.println("Faltan Párámetros");
			} else {
				switch (param.opcion) {
				case V:
					if (!param.raiz) {
						creaFicheroLog(param.log, batchValidar(param.ruta));
					}
					break;
				default:
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String batchValidar(String directorioDatos) {
		boolean resultado;
		StringBuilder cad = new StringBuilder();
		try {

			LegalizacionService mL = new LegalizacionService();

			resultado = mL.carga(directorioDatos);

			cad.append("Carga: ").append(resultado).append("\n");

			if (!resultado) {
				cad.append("No carga el Directorio ").append(directorioDatos);
				return cad.toString();
			}

			resultado = mL.valida() != null;
			cad.append("Valida: ").append(resultado).append("\n");

			if (!resultado) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cad.toString();
	}

	private void creaFicheroLog(String ficheroLog, String textoLog) {
		try {
			synchronized (this) {
				FileWriter fw = new FileWriter(ficheroLog, true);
				String cad = java.time.LocalDateTime.now().toString() + " " + textoLog;
				fw.write(cad + "\n");
				fw.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
