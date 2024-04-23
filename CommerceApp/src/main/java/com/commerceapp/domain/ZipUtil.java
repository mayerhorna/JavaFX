package com.commerceapp.domain;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.zip.Deflater;

import java.util.zip.ZipEntry;

import java.util.zip.ZipInputStream;

import java.util.zip.ZipOutputStream;

import com.commerceapp.domain.legalizacion.kLegalizacion;

public class ZipUtil {

	public boolean descomprimir(String zipFic, String directorioDestino, StringBuilder cadError, Progreso vProgreso) {

		boolean exito = false;

		ZipInputStream z = null;

		File fi;

		long sizeTotal = 0;

		long sizeActual = 0;

		boolean indicarProgreso;

		try {

			cadError.setLength(0);

			if (!Files.exists(Paths.get(zipFic))) {

				cadError.append("El archivo especificado no existe: ").append(zipFic);

				return false;

			}

			if (!Files.exists(Paths.get(directorioDestino))) {

				Files.createDirectories(Paths.get(directorioDestino));

			}

			indicarProgreso = vProgreso != null;

			ZipEntry theEntry;

			if (indicarProgreso) {

				try {

					z = new ZipInputStream(new FileInputStream(zipFic));

					while ((theEntry = z.getNextEntry()) != null) {

						sizeTotal += theEntry.getSize();

					}

				} catch (Exception ex) {

					indicarProgreso = false;

				} finally {

					if (z != null) {

						z.close();

					}

				}

			}

			if (!indicarProgreso) {

				sizeTotal = 0;

			}

			if (vProgreso != null) {

				vProgreso.siguienteProceso((int) sizeTotal, "Descomprimiendo Zip");

			}

			z = new ZipInputStream(new FileInputStream(zipFic));

			byte[] data = new byte[1024 * 1024];

			int size;

			while ((theEntry = z.getNextEntry()) != null) {

				String fileName = Paths.get(directorioDestino, Paths.get(theEntry.getName()).getFileName().toString())

						.toString();

				FileOutputStream fos = null;

				try {

					fos = new FileOutputStream(fileName);

					while ((size = z.read(data)) != -1) {

						fos.write(data, 0, size);

						sizeActual += size;

						if (vProgreso != null) {

							vProgreso.mostrarProgreso((int) sizeActual);

						}

					}

				} catch (FileNotFoundException ex) {

					Files.createDirectories(Paths.get(fileName).getParent());

					fos = new FileOutputStream(fileName);

				} finally {

					if (fos != null) {

						fos.close();

					}

				}

				fi = new File(fileName);

				fi.setLastModified(theEntry.getTime());

			}

			exito = true;

		} catch (Exception ex) {

			ex.printStackTrace();

			cadError.append(ex.getMessage());

		} finally {

			try {

				if (z != null) {

					z.close();

				}

				if (vProgreso != null) {

					vProgreso.procesoTerminado();

				}

			} catch (IOException ex) {

			}

		}

		return exito;

	}

	public static ZipEntry[] contenido(String zipFic) {

		ZipInputStream z = null;

		ZipEntry[] files = null;

		int n = -1;

		try {

			z = new ZipInputStream(new FileInputStream(zipFic));

			ZipEntry theEntry;

			while ((theEntry = z.getNextEntry()) != null) {

				n++;

				if (files == null) {

					files = new ZipEntry[1];

				} else {

					ZipEntry[] temp = new ZipEntry[n + 1];

					System.arraycopy(files, 0, temp, 0, files.length);

					files = temp;

				}

				files[n] = theEntry;

				byte[] data = new byte[1024 * 1024];

				int size;

				while ((size = z.read(data, 0, data.length)) != -1) {

					// Leer los datos para avanzar al siguiente archivo en el zip

				}

			}

		} catch (Exception ex) {

			files = null;

		} finally {

			try {

				if (z != null) {

					z.close();

				}

			} catch (IOException ex) {

			}

		}

		return files;

	}

	public static boolean compruebaContenido(String zipFic) {

		boolean tieneDatosTxt = false;

		boolean tieneNombresTxt = false;

		ZipEntry[] files = contenido(zipFic);

		if (files == null)

			return false;

		for (ZipEntry file : files) {

			if (file.getName().toUpperCase().equals(kLegalizacion.kNombreFicheroDatos.toUpperCase()))

				tieneDatosTxt = true;

			if (file.getName().toUpperCase().equals(kLegalizacion.kNombreFicheroNombres.toUpperCase()))

				tieneNombresTxt = true;

			if (tieneDatosTxt && tieneNombresTxt)

				break;

		}

		return tieneDatosTxt && tieneNombresTxt;

	}

	public static boolean creaZip(String[] fileNames, String zipFic, StringBuilder cadError, Progreso vProgreso) {

		File fi;

		String strFile;

		int tamaniobuffer = 4096;

		byte[] abyBuffer = new byte[tamaniobuffer];

		long sizetotal = 0;

		long sizeactual = 0;

		int sizetemporal;

		try {

			cadError.setLength(0);

			if (zipFic.isEmpty())

				return false;

			for (String fileName : fileNames) {

				fi = new File(fileName);

				sizetotal += fi.length();

			}

			if (vProgreso != null)

				vProgreso.siguienteProceso((int) sizetotal,

						MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.GenerandoZip).toString());

			try (ZipOutputStream strmZipOutputStream = new ZipOutputStream(new FileOutputStream(zipFic))) {

				strmZipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

				for (String fileName : fileNames) {

					try (FileInputStream strmFile = new FileInputStream(fileName)) {

						String sFile = Paths.get(fileName).getFileName().toString();

						ZipEntry theEntry = new ZipEntry(sFile);

						fi = new File(fileName);

						theEntry.setTime(fi.lastModified());

						theEntry.setSize(fi.length());

						theEntry.setMethod(ZipEntry.DEFLATED);

						strmZipOutputStream.putNextEntry(theEntry);

						while ((sizetemporal = strmFile.read(abyBuffer)) > 0) {

							sizeactual += sizetemporal;

							strmZipOutputStream.write(abyBuffer, 0, sizetemporal);

							if (vProgreso != null) {

								// vProgreso.mostrarProgreso((int) sizeactual);

							}

						}

						strmZipOutputStream.closeEntry();

					}

				}

			}

			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

			cadError.append(ex.getMessage());

			return false;

		} finally {

			if (vProgreso != null)

				vProgreso.procesoTerminado();

		}

	}

	public boolean descomprimirSelectivo(String zipFic, String directorioDestino, String[] nombresADescomprimir,

			StringBuilder cadError, Progreso vProgreso) {

		cadError = new StringBuilder();

		ZipInputStream z = null;

		long sizeTotal = 0;

		long sizeActual = 0;

		boolean indicarProgreso;

		try {

			if (!new File(zipFic).exists()) {

				cadError.append("Fichero inexistente: ").append(zipFic);

				return false;

			}

			if (!new File(directorioDestino).exists()) {

				new File(directorioDestino).mkdirs();

			}

			indicarProgreso = vProgreso != null;

			ZipEntry theEntry;

			if (indicarProgreso) {

				try {

					z = new ZipInputStream(new FileInputStream(zipFic));

					while ((theEntry = z.getNextEntry()) != null) {

						if (estaNombreEnArray(nombresADescomprimir, theEntry.getName())) {

							try {

								sizeTotal += z.available();

							} catch (IOException e) {

								indicarProgreso = false;

								break;

							}

						}

					}

					z.close();

				} catch (IOException e) {

					indicarProgreso = false;

					try {

						if (z != null)

							z.close();

					} catch (IOException ignored) {

					}

				}

			}

			if (!indicarProgreso)

				sizeTotal = 0;

			if (vProgreso != null)

				vProgreso.siguienteProceso((int) sizeTotal, "Descomprimiendo ZIP");

			z = new ZipInputStream(new FileInputStream(zipFic));

			byte[] buffer = new byte[1024 * 1024];

			int size;

			while ((theEntry = z.getNextEntry()) != null) {

				if (estaNombreEnArray(nombresADescomprimir, theEntry.getName())) {

					String fileName = "";

					if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

						fileName = directorioDestino + "\\" + new File(theEntry.getName()).getName();

					} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

						fileName = directorioDestino + "/" + new File(theEntry.getName()).getName();

					}

					FileOutputStream fos = new FileOutputStream(fileName);

					while ((size = z.read(buffer)) > 0) {

						fos.write(buffer, 0, size);

						sizeActual += size;

						if (vProgreso != null)

							vProgreso.mostrarProgreso((int) sizeActual);

					}

					fos.close();

					File fi = new File(fileName);

					fi.setLastModified(theEntry.getTime());

				}

			}

			return true;

		} catch (IOException ex) {

			ex.printStackTrace();

			cadError.append(ex.getMessage());

			return false;

		} finally {

			try {

				if (z != null)

					z.close();

				if (vProgreso != null)

					vProgreso.procesoTerminado();

			} catch (IOException ignored) {

			}

		}

	}

	private String getFileExtension(String filename) {

		if (filename != null && filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {

			return filename.substring(filename.lastIndexOf(".") + 1);

		}

		return null;

	}

	public static boolean estaNombreEnArray(String[] nombresADescomprimir, String nombreBuscado) {

		for (String str : nombresADescomprimir) {

			if (str.toUpperCase().equals(nombreBuscado.toUpperCase())) {

				return true;

			}

		}

		return false;

	}

}
