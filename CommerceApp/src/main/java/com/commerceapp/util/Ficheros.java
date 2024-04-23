package com.commerceapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Ficheros {
	private static final Logger logger = Logger.getLogger(Ficheros.class.getName());

	public enum ExistFicheroEnum {
		Existe, NoExiste, NoHayAcceso
	}

	/**
	 * Indica si cierto fichero existe, no existe, o no hay acceso al mismo.
	 */
	public enum ExistDirectorioEnum {
		Existe, NoExiste, NoHayAcceso
	}

	public Ficheros() {

	}

	public static byte[] LeerFichero(String rutaFichero, int numBytes) throws IOException {
		FileInputStream fs = null;
		byte[] bufferLectura;
		int bytesLeidos;

		try {
			fs = new FileInputStream(new File(rutaFichero));

			if (numBytes > 0) {
				bufferLectura = new byte[numBytes];
				bytesLeidos = fs.read(bufferLectura, 0, numBytes);

				if (bytesLeidos < numBytes) {
					byte[] resizedBuffer = new byte[bytesLeidos];
					System.arraycopy(bufferLectura, 0, resizedBuffer, 0, bytesLeidos);
					bufferLectura = resizedBuffer;
				}
			} else {
				bufferLectura = new byte[(int) fs.available()];
				fs.read(bufferLectura, 0, bufferLectura.length);
			}

			return bufferLectura;

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IOException("[" + ex.getMessage() + " en leerFichero]");
		} finally {
			if (fs != null) {
				fs.close();
			}
		}
	}

	public static boolean DirectorioRenombra(String directorioInicial, String directorioFinal) {
		try {
			File initialDir = new File(directorioInicial);
			File finalDir = new File(directorioFinal);

			if (initialDir.isAbsolute() && finalDir.isAbsolute() && initialDir.exists() && !finalDir.exists()) {
				return initialDir.renameTo(finalDir);
			} else {
				return false;
			}

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean DecodificarBase64AFichero(String cadBase64, String rutaFicheroDestino) {
		try {
			if (cadBase64 == null || cadBase64.isEmpty()) {
				return false;
			}

			byte[] bytesFichero = java.util.Base64.getDecoder().decode(cadBase64);

			try (FileOutputStream escritor = new FileOutputStream(new File(rutaFicheroDestino))) {
				escritor.write(bytesFichero);
				escritor.flush();
				escritor.close();
			}

			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public static String ConvertirABase64(String inputString, Charset encoding) {
		if (encoding == null) {
			encoding = StandardCharsets.ISO_8859_1;
		}
		byte[] bytes = inputString.getBytes(encoding);
		return java.util.Base64.getEncoder().encodeToString(bytes);
	}

	public static String ConcatenaPathYFichero(Object path, Object file) {
		try {
			String pathStr = path.toString().trim();
			String fileStr = file.toString().trim();

			// We work only with one type of directory separator ("\")
			pathStr = pathStr.replace("/", "\\");

			// Check that there are no incorrect characters or multiple consecutive slashes
			/*if (pathStr.contains('\\') || fileStr.contains(File.separator)) {
				return "";
			}*/

			// Remove, if they exist, the final slash of the path and the initial one of the
			// file
			pathStr = pathStr.replaceAll("\\\\$", "");
			fileStr = fileStr.replaceAll("^\\\\", "");

			// Concatenate path and file
			if (pathStr != null) {
				if (fileStr != null) {
					return pathStr + "\\" + fileStr;
				} else {
					return pathStr;
				}
			} else {
				return fileStr;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String LeerFichero(String filePath, Charset encoding) {
		try {
			if (encoding != null) {
				return Files.readString(Path.of(filePath), encoding);
			} else {
				return Files.readString(Path.of(filePath));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("[" + ex.getMessage() + " in readFile]");
		}
	}

	public static boolean FicheroACadena(String filePath, Charset encoding, StringBuilder content) {
		content.setLength(0);

		try (FileInputStream fs = new FileInputStream(filePath)) {
			byte[] bytes = fs.readAllBytes();
			// bytes =
			// Charset.forName("ISO_8859_1").decode(java.nio.ByteBuffer.wrap(bytes)).array();
			content.append(new String(bytes, StandardCharsets.UTF_8)); // default charset
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	public static void EscribirFichero(String fileContent, String filePath) {
		try (FileOutputStream fs = new FileOutputStream(filePath, false)) {
			// byte[] bytesToWrite =
			// fileContent.getBytes(StandardCharsets.defaultCharset());
			// fs.write(bytesToWrite);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("[" + ex.getMessage() + " in writeFile]");
		}
	}

	public static ArrayList<String> BuscarDirectorios(String directorioBase, String expresionRegular) {
		ArrayList<String> dirEncontrados = new ArrayList<>();

		try {
			Pattern pattern = Pattern.compile(expresionRegular);

			if (!Paths.get(directorioBase).isAbsolute() || !DirectorioHayAcceso(directorioBase)) {
				return dirEncontrados;
			}

			for (File subDirectorio : new File(directorioBase).listFiles(File::isDirectory)) {
				if (pattern.matcher(subDirectorio.getName()).matches()) {
					dirEncontrados.add(subDirectorio.getAbsolutePath());
				}
				dirEncontrados.addAll(BuscarDirectorios(subDirectorio.getAbsolutePath(), expresionRegular));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unexpected error in findDirectories. Details: " + ex.getMessage());
		}

		return dirEncontrados;
	}

	public static boolean FicheroCrea(String fichero) {
		try {
			File file = new File(fichero);

			// Obtener el directorio en el que vamos a crear el archivo
			String directorio = file.getParent();

			// Comprobar que se ha proporcionado una ruta completa
			if (directorio == null) {
				return false;
			}

			// Si ya existe un directorio o archivo con el mismo nombre, no hacemos nada
			if (Files.exists(file.toPath())) {
				return false;
			}

			// Crear el directorio de destino del archivo (si no existe) y el propio archivo
			DirectorioCrea(directorio);

			// Crear el archivo
			Files.createFile(file.toPath());

			return true;
		} catch (IOException ex) {
			// Si no se consigue crear el archivo (por ejemplo, si la ruta introducida no es
			// válida)
			return false;
		}
	}

	public static boolean FicheroMueve(String ficheroOrigen, String ficheroDestino, boolean reemplazarDestino) {
		try {
			// Comprobar que se han pasado rutas absolutas
			if (!Paths.get(ficheroOrigen).isAbsolute() || !Paths.get(ficheroDestino).isAbsolute()) {
				return false;
			}

			// Comprobar que existe el archivo de origen
			if (!Files.exists(Paths.get(ficheroOrigen))) {
				return false;
			}

			// Comprobar si ya existe el archivo de destino, para eliminarlo
			// (replaceDestination=true)
			// o bien para detener la operación (replaceDestination=false)
			if (reemplazarDestino) {
				try {
					Files.deleteIfExists(Paths.get(ficheroDestino));
				} catch (DirectoryNotEmptyException ex) {
					// Tratar el caso en el que el destino sea un directorio no vacío
					return false;
				}
			} else {
				if (Files.exists(Paths.get(ficheroDestino))) {
					return false;
				}
			}

			// Mover el archivo
			Files.move(Paths.get(ficheroOrigen), Paths.get(ficheroDestino), StandardCopyOption.REPLACE_EXISTING);

			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	public static boolean FicheroBorra(String filePath) {
		
	try {
		
		
		Path path = Paths.get(filePath);

		if (Files.exists(path)) {
			try {
				// Desactivar el atributo solo lectura si está configurado
				FicheroSoloLectura(filePath, false);

				// Eliminar el archivo
				Files.delete(path);
			} catch (IOException ex) {
				// Si no se consigue borrar el archivo (por tema de permisos, por ejemplo)...
				return false;
			}
			return true;
		}else {
			return true;
		}
		
	}catch (Exception e) {
		return false;
	}
	
	}

	public static boolean DirectorioBorra(String directorio, boolean borrarElPropioDirectorio,
			boolean incluidosSubdirectorios, StringBuilder textoError) {

		try {
			textoError=new StringBuilder();

			File directorioFile = new File(directorio);

			if (directorioFile.exists()) {
				try {
					if (borrarElPropioDirectorio) {
// Quito el atributo de sólo lectura del directorio completo
// y lo elimino
						DirectorioSoloLectura(directorio, false, true);
						borrarDirectorio(directorioFile);
					} else {
// Quito el atributo de sólo lectura de los ficheros del directorio
						DirectorioSoloLectura(directorio, false, true);

// Elimino los ficheros que hay en el directorio
						for (File fichero : directorioFile.listFiles()) {
							if (fichero.isFile()) {
								fichero.delete();
							}
						}

						if (incluidosSubdirectorios) {
							// Elimino todos los subdirectorios
							for (File subDirectorio : directorioFile.listFiles()) {
								// Quito el atributo de sólo lectura de todo el subdirectorio (recursivo)
								// y lo elimino
								DirectorioSoloLectura(directorio, false, true);
								borrarDirectorio(subDirectorio);
							}
						}
					}

					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
					textoError.append(ex.getMessage());
					return false;
				}
			} else {
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			textoError.append(ex.getMessage());
			return false;
		}
	}

	private static void borrarDirectorio(File directorio) {
		File[] archivos = directorio.listFiles();
		if (archivos != null) {
			for (File archivo : archivos) {
				if (archivo.isDirectory()) {
					borrarDirectorio(archivo);
				} else {
					archivo.delete();
				}
			}
		}
		directorio.delete();
	}

	public static boolean FicheroSoloLectura(String fichero, boolean soloLectura) {
	
		try {
			File ficheroFile = new File(fichero);

			if (ficheroFile.exists()) {
				try {
					if (soloLectura) {						
						ficheroFile.setReadOnly();
						
					} else {
						ficheroFile.setWritable(true);
					
					}
				} catch (Exception ex) {
				
					return false;
				}
				
				
				return true;
			} else {
				
				return false;
			}
			
		} catch (Exception ex) {
			
			return false;
		}
	}

	public static boolean DirectorioSoloLectura(String directorio, boolean soloLectura,
			boolean modificarSoloFicherosDelDirectorio) {
		try {
			File directorioFile = new File(directorio);

			if (directorioFile.exists()) {
				try {
// Cambio el atributo de lectura a cada fichero
					for (File fichero : directorioFile.listFiles()) {
						if (fichero.isFile()) {
							if (!FicheroSoloLectura(fichero.getAbsolutePath(), soloLectura)) {
								return false;
							}
						}
					}

					if (!modificarSoloFicherosDelDirectorio) {
// Recorro los subdirectorios y establezco el atributo de lectura
// de todos los archivos en su interior
						for (File subDirectorio : directorioFile.listFiles()) {
							if (subDirectorio.isDirectory()) {
								if (!DirectorioSoloLectura(subDirectorio.getAbsolutePath(), soloLectura, false)) {
									return false;
								}
							}
						}
					}

				} catch (Exception ex) {
					// Si algo va mal...
					return false;
				}

				return true;
			} else {
				return false; // El directorio no existe
			}

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean DirectorioCrea(String directorio) {
		try {
			// Comprobamos que nos han pasado una ruta absoluta
			if (!new File(directorio).isAbsolute()) {
				return false;
			}

			// Si ya existe un directorio con el mismo nombre devolvemos true
			if (new File(directorio).isDirectory()) {
				return true;
			}

			// Si existe un archivo con el nombre del directorio que queremos crear
			// devolvemos false
			if (new File(directorio).isFile()) {
				return false;
			}

			// Creamos el directorio
			new File(directorio).mkdirs();

			return true;

		} catch (Exception ex) {
			// Si no se consigue crear el directorio (por ejemplo si la ruta introducida no
			// es válida)
			return false;
		}
	}

	public static boolean DirectorioHayAcceso(String directorio) {
		try {
			// La función exists devuelve false si no existe el directorio o bien
			// no se permite el acceso.
			return new File(directorio).exists();

		} catch (Exception ex) {
			// No debería ocurrir, pero si se produce un error al invocar exists
			// devolvemos falso
			return false;
		}
	}

	public static ExistDirectorioEnum DirectorioExiste(String directorio) {
		if (directorio == null || directorio.isEmpty()) {
			return ExistDirectorioEnum.NoExiste;
		}

		try {
			// Intento hacer un listFiles del directorio especificado y...
			File[] files = new File(directorio).listFiles();

			if (files != null) {
				// Si no se ha producido ningún error significa que el directorio existe
				return ExistDirectorioEnum.Existe;
			} else {
				// Si se produce una excepción de tipo SecurityException,
				// significa que no tenemos acceso al directorio
				return ExistDirectorioEnum.NoHayAcceso;
			}
		} catch (SecurityException exAcceso) {
			// Si se produce una excepción de este tipo significa que no
			// tenemos acceso al directorio
			return ExistDirectorioEnum.NoHayAcceso;
		} catch (Exception ex) {
			// Si se produce una excepción no mencionada anteriormente
			// (por ejemplo si se ha introducido una ruta de directorio sin sentido
			// como cadena vacía), devolvemos que el directorio no existe
			return ExistDirectorioEnum.NoExiste;
		}
	}

	public static ExistFicheroEnum FicheroExiste(String fichero) throws FileNotFoundException {
		
		try {
			
			// Intento obtener los atributos del fichero especificado y...
			Path rutaArchivo = Path.of(fichero);
			BasicFileAttributes atributos = Files.readAttributes(rutaArchivo, BasicFileAttributes.class);

			return ExistFicheroEnum.Existe;

		} catch (SecurityException exAcceso) {
			// Si se produce una excepción de este tipo significa que no
			// tenemos acceso al directorio donde está el fichero
			// exAcceso.printStackTrace();
			
			return ExistFicheroEnum.NoHayAcceso;
		} catch (Exception ex) {
			// Si se produce una excepción no mencionada anteriormente
			// (por ejemplo si se ha introducido una ruta de fichero sin sentido
			// como cadena vacía), devolvemos que el fichero no existe
			// ex.printStackTrace();
		
			return ExistFicheroEnum.NoExiste;
		}
	}

	public static boolean GuardarFichero(String ficheroDestino, String stringGuardar, String encoding,
			boolean soloLectura) {
		try {
			PrintWriter writer = new PrintWriter(ficheroDestino, encoding);
			writer.print(stringGuardar);
			writer.close();

			// Pongo el fichero guardado como solo lectura
			if (soloLectura) {
				return FicheroSoloLectura(ficheroDestino, soloLectura);
			} else {
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean HayAccesoExclusivo(String rutaFichero, double miliSegundosEspera) {
		try {
			long inicioTiempo = System.currentTimeMillis();

			while (true) {
				try (FileOutputStream os = new FileOutputStream(rutaFichero, true)) {
					// Intentamos abrir el fichero en modo lectura/escritura con acceso exclusivo
					break;
				} catch (IOException ex) {
					// Si no se puede abrir el fichero, esperamos un momento antes de intentar
					// nuevamente
					if (System.currentTimeMillis() - inicioTiempo > miliSegundosEspera) {
						return false;
					}
				}
			}

			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean DirectorioComprueba(String pathDirectorio) {
		
		try {
			if (Formato.ValorNulo(pathDirectorio)) {
				
				return false;
			}

			// Si ya existe un fichero con el nombre del directorio
			if (Files.exists(Paths.get(pathDirectorio))) {
			
				return true;
			}

			// Si no existe el directorio, lo creamos
			if (!Files.exists(Paths.get(pathDirectorio))) {
				
				Files.createDirectories(Paths.get(pathDirectorio));
			}

			String tmpFichero = Paths.get(pathDirectorio, "tmp" + System.currentTimeMillis() + ".tmp").toString();

			try (FileOutputStream os = new FileOutputStream(tmpFichero)) {
				// Intentamos crear y escribir en un fichero temporal en el directorio
				// para comprobar los permisos de escritura
			}

			Files.deleteIfExists(Paths.get(tmpFichero));
			
			return FicheroBorra(tmpFichero);

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean ContenidoDirectorioEsDeSoloLectura(String pathDirectorio) {
		try {
			if (DirectorioExiste(pathDirectorio) != ExistDirectorioEnum.Existe) {
				return true; // Si no existe, se toma como de solo lectura
			}

			File directorio = new File(pathDirectorio);
			File[] archivos = directorio.listFiles();

			if (archivos != null) {
				for (File archivo : archivos) {
					if (!archivo.canWrite()) {
						return false; // Si un fichero no es writable, se retorna false
					}
				}
			}

			return true;

		} catch (Exception ex) {
			return true; // En caso de error, se toma como de solo lectura
		}
	}

	public static boolean NombreValido(String nombre) {
		if (Formato.ValorNulo(nombre)) {
			return false;
		}

		if (nombre.contains("\\") || nombre.contains("/") || nombre.contains(":") || nombre.contains("<")
				|| nombre.contains(">") || nombre.contains("*") || nombre.contains("?") || nombre.contains("|")
				|| nombre.indexOf("\"") >= 0) {
			return false;
		}

		return true;
	}

	private static String CodificaPathLocalAUri(String pathLocal) {
		return new File(pathLocal).toURI().toString();
	}

	private static String CodificaPathLocalAWeb(String pathLocal) {
		return pathLocal.replace("\\", "/");
	}

	public static String CodificaPathLocal(String pathLocal) {
		return CodificaPathLocalAWeb(pathLocal);
		// TODO PATH URI: Pendiente de que permitan el cambio en el portal
		// return CodificaPathLocalAUri(pathLocal);
	}

}
